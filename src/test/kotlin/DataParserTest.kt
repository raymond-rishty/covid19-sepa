import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import rrishty.covid19sepa.DataParser.parse

class DataParserTest {
    @Test
    fun testParse() {
        val countyUids = setOf(
            "84042001.0",
            "84042003.0",
            "84042005.0"
        )
        val records = DataParserTest::class.java.getResource("/time_series_covid19_confirmed_US.csv")
            .openStream()
            .bufferedReader()
            .lineSequence()
            .drop(1)
            .map { it.parse() }
            .filter { if (!it.uid.endsWith(".0")) { it.uid + ".0" } else { it.uid } in countyUids }
            .toList()

        assertThat(records).hasSize(3)
    }
}