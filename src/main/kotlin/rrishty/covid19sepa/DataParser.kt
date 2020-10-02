package rrishty.covid19sepa

object DataParser {
    private val splitPattern = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)".toRegex()

    fun String.parse(): Record {
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
            dailyData = fields.last().split(',').map { it.toDouble().toInt() }
        )
    }
}