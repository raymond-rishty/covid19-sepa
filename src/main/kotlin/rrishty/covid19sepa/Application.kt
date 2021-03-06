package rrishty.covid19sepa

import com.amazonaws.services.lambda.runtime.LambdaLogger
import rrishty.covid19sepa.DataParser.parse
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import java.util.*

class Application(
    private val covidCasesDao: CovidCasesDao,
    private val s3Writer: S3Writer,
    private val regionalCounties: Map<String, Int>,
    private val logger: LambdaLogger?
) {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = execute("Southeast")

        fun execute(regionName: String, logger: LambdaLogger? = null) {
            val covidCasesDao = CovidCasesDao()
            val (bucketName: String, keyNameTemplate: String, awsRegionName: String) = getS3Properties()
            val keyName = keyNameTemplate.replace("{region}", regionName.toLowerCase())
            val s3Writer = S3Writer(bucketName, keyName, awsRegionName)

            val counties = RegionResolver().countiesForRegion(regionName)

            logger?.log("Running for ${counties.size} counties in the $regionName region; population ${counties.values.sum()}")
            logger?.log("Will write report to s3://$bucketName/$keyName")

            val application = Application(
                covidCasesDao = covidCasesDao,
                s3Writer = s3Writer,
                regionalCounties = counties,
                logger = logger
            )

            application.run()
        }

        private fun getS3Properties(): Triple<String, String, String> {
            val properties = Properties()
            properties.load(Application::class.java.getResourceAsStream("/s3.properties"))
            val bucketName: String = properties.getProperty("bucketName")
            val keyName: String = properties.getProperty("keyName")
            val regionName: String = properties.getProperty("regionName")
            return Triple(bucketName, keyName, regionName)
        }
    }

    data class County(val uid: String, val countyName: String, val population: Int)

    fun run() {
        val sequence = covidCasesDao.getRawData()
        logger?.log("Found data\n")
        val regionalTotals = getRegionalTotals(sequence)
        logger?.log("Found ${regionalTotals.size} data points\n")
        val csvContents = formatResults(regionalTotals)
        logger?.log("Created CSV data\n")
        s3Writer.writeCsvContentsToS3(csvContents)
        logger?.log("Wrote contents to S3\n")
    }

    private fun formatResults(regionalTotals: List<Int>): String {
        val resultsWithDate = regionalTotals
            .mapIndexed { index, value -> startingDate.plusDays(index.toLong()) to value }

        val resultsByDate = resultsWithDate.associateBy { it.first }

        val regionalPopulation: Int = regionalCounties.values.sum()

        val dataContents = resultsWithDate
            .dropWhile { it.second == 0 }
            .map { (date, value) ->
                val newCases = value - (resultsByDate[date.minusDays(14L)]?.second ?: 0)
                Result(
                    date,
                    value,
                    newCases,
                    newCasesAdjusted = (newCases * 100000L) / regionalPopulation
                )
            }
            .joinToString("\n") { (date, totalConfirmed, newCases, newCasesAdjusted) ->
                "${date.format(DateTimeFormatter.ISO_DATE)},$totalConfirmed,$newCases,$newCasesAdjusted"
            }

        return "date,total confirmed,14-day new cases,population-adjusted new cases" +
                System.lineSeparator() +
                dataContents
    }

    data class Result(
        val date: LocalDate,
        val totalConfirmed: Int,
        val newCases: Int,
        val newCasesAdjusted: Long
    )

    private fun getRegionalTotals(sequence: Sequence<String>): List<Int> {
        val dataForCounties = sequence.drop(1)
            .filter { (it.split(',', limit = 2).first()).uidWithSuffix() in regionalCounties }
            .map { it.parse() }
            .filter { it.uid.uidWithSuffix() in regionalCounties }
            .toList()
        return dataForCounties
            .map { it.dailyData }
            .reduce { regionTotals, countyData -> regionTotals.zip(countyData) { left, right -> left + right } }
    }

    private fun String.uidWithSuffix() = if (!endsWith(".0")) "${this}.0" else this

    private val startingDate = LocalDate.of(2020, Month.JANUARY, 22)
}