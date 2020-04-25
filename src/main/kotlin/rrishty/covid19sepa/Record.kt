package rrishty.covid19sepa

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