import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.S3ClientBuilder
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter

fun main() {
    val sequence = getDataFromHttp()
    val regionalTotals = getRegionalTotals(sequence)


    val csvContents = regionalTotals
        .mapIndexed { index, value -> startingDate.plusDays(index.toLong()) to value }
        .dropWhile { it.second == 0 }
        .joinToString("\n") { it.first.format(DateTimeFormatter.ISO_DATE) + "," + it.second }

    writeCsvContentsToS3(csvContents)
}

private fun writeCsvContentsToS3(contents: String) {
    val bucketName = "covid19.data.rrishty"
    val keyName = "regional-sepa-totalconfirmed.csv"
    val s3Client = S3Client.create()
    val putObjectRequest = PutObjectRequest.builder()
        .bucket(bucketName)
        .key(keyName)
        .contentLength(contents.length.toLong())
        .build()
    s3Client.putObject(putObjectRequest, contents.toRequestBody())
}

fun String.toRequestBody(): RequestBody = RequestBody.fromString(this)

private fun getRegionalTotals(sequence: Sequence<String>): List<Int> {
    return sequence.drop(1).map { it.parse() }
        .filter { it.provinceState == "Pennsylvania" }
        .filter { it.admin2 in southeastCounties }
        .map { it.dailyData }
        .reduce { regionTotals, countyData -> regionTotals.zip(countyData) { left, right -> left + right } }
}

private fun getDataFromHttp(): Sequence<String> {
    val url =
        "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_US.csv"

    return sequence<String> {
        (URL(url)
            .openConnection() as HttpURLConnection)
            .inputStream
            .bufferedReader()
            .use {
                val reader = it
                var line: String?
                do {
                    line = reader.readLine()
                    if (line != null) {
                        yield(line)
                    }
                } while (line != null)
            }
    }
}

private val startingDate = LocalDate.of(2020, Month.JANUARY, 22)

private val southeastCounties = setOf(
    "Schuylkill", // 142067
    "Berks", // 420152
    "Lancaster", // 543557
    "Chester", // 522046
    "Montgomery", // 828604
    "Bucks", // 628195
    "Delaware", // 564751
    "Philadelphia" // 1584138
)

private val splitPattern = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)".toRegex()
private fun String.parse(): Record {
    val fields = split(regex = splitPattern, limit = 12)
    return Record(
        uid = fields[0],
        iso2 = fields[1],
        iso3 = fields[2],
        code3 = fields[3],
        fips = fields[4],
        admin2 = fields[5],
        provinceState = fields[6],
        countryRegion = fields[7],
        lat = fields[8],
        long = fields[9],
        combinedKey = fields[10],
        dailyData = fields.last().split(',').map { it.toInt() }
    )
}

data class Record(
    val uid: String,
    val iso2: String,
    val iso3: String,
    val code3: String,
    val fips: String,
    val admin2: String,
    val provinceState: String,
    val countryRegion: String,
    val lat: String,
    val long: String,
    val combinedKey: String,
    val dailyData: List<Int>
)