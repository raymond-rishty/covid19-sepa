package rrishty.covid19sepa

import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import rrishty.covid19sepa.DataParser.parse
import java.util.*

class Application(
    private val covidCasesDao: CovidCasesDao,
    private val s3Writer: S3Writer,
    private val state: String,
    private val regionalCounties: Set<String>
) {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val covidCasesDao = CovidCasesDao()
            val (bucketName: String, keyName: String, regionName: String) = getS3Properties()
            val s3Writer = S3Writer(bucketName, keyName, regionName)

            val counties = getCounties()

            val application = Application(
                covidCasesDao = covidCasesDao,
                s3Writer = s3Writer,
                state = "Pennsylvania",
                regionalCounties = counties
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

        private fun getCounties(): Set<String> = Application::class.java.getResource("/regioncounties.txt")
            .readText()
            .split(System.lineSeparator())
            .toSet()
    }

    fun run() {
        val sequence = covidCasesDao.getRawData()
        val regionalTotals = getRegionalTotals(sequence)
        val csvContents = formatResults(regionalTotals)
        s3Writer.writeCsvContentsToS3(csvContents)
    }

    private fun formatResults(regionalTotals: List<Int>): String {
        return regionalTotals
            .mapIndexed { index, value -> startingDate.plusDays(index.toLong()) to value }
            .dropWhile { it.second == 0 }
            .joinToString("\n") { it.first.format(DateTimeFormatter.ISO_DATE) + "," + it.second }
    }

    private fun getRegionalTotals(sequence: Sequence<String>): List<Int> {
        return sequence.drop(1)
            .map { it.parse() }
            .filter { it.provinceState == state }
            .filter { it.admin2 in regionalCounties }
            .map { it.dailyData }
            .reduce { regionTotals, countyData -> regionTotals.zip(countyData) { left, right -> left + right } }
    }

    private val startingDate = LocalDate.of(2020, Month.JANUARY, 22)
}