package rrishty.covid19sepa

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import java.util.ArrayList

class RegionResolver {
    fun countiesForRegion(regionName: String): Map<String, Int> {
        val countiesInRegion = getCountiesInRegion(regionName)
        val allCounties = getCounties()
        return allCounties.filter { it.countyName in countiesInRegion }
            .associateBy({ it.uid }, { it.population })
    }

    private fun getCountiesInRegion(regionName: String): List<String> {
        val yamlMapper = YAMLMapper()
        val typeFactory = yamlMapper.typeFactory
        val regionMapType =
            typeFactory.constructMapType(HashMap::class.java, String::class.java, ArrayList::class.java)
        val regionsResource = Application::class.java.getResource("/regions.yml")
        val regionsCounties = yamlMapper.readValue<Map<String, List<*>>>(
            regionsResource.openStream(),
            regionMapType
        )
        return regionsCounties.getValue(regionName).map { it as String }
    }

    private fun getCounties() = Application::class.java.getResource("/counties.csv")
        .readText()
        .split("\n")
        .map { it.split(',') }
        .map { (uid, name, population) -> Application.County(uid, name, population.trim().toInt()) }
}