import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import rrishty.covid19sepa.RegionResolver

class RegionResolverTest {
    @Test
    fun allRegionsResolvable() {
        val regions = listOf(
            "Northwest",
            "Northcentral",
            "Northeast",
            "Southwest",
            "Southcentral",
            "Southeast"
        )
        val regionResolver = RegionResolver()
        val allCounties = regions.flatMap { regionResolver.countiesForRegion(it).keys }
        assertThat(allCounties).hasSize(67)
    }
}