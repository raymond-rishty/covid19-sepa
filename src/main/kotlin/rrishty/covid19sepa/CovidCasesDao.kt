package rrishty.covid19sepa

import java.net.HttpURLConnection
import java.net.URL

class CovidCasesDao {
    fun getRawData(): Sequence<String> {
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

}