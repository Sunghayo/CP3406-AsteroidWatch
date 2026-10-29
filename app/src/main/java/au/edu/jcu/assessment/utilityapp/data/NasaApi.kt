package au.edu.jcu.assessment.utilityapp.data

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaApi {
    @GET("neo/rest/v1/feed")
    suspend fun getCloseApproaches(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String
    ): NeoFeedResponse
}

data class NeoFeedResponse(
    @SerializedName("near_earth_objects") val nearEarthObjects: Map<String, List<NeoDto>> = emptyMap()
)

data class NeoDto(
    val id: String = "",
    val name: String = "",
    @SerializedName("is_potentially_hazardous_asteroid") val potentiallyHazardous: Boolean = false,
    @SerializedName("estimated_diameter") val estimatedDiameter: DiameterDto? = null,
    @SerializedName("close_approach_data") val closeApproaches: List<CloseApproachDto> = emptyList()
)

data class DiameterDto(
    val meters: RangeDto? = null
)

data class RangeDto(
    @SerializedName("estimated_diameter_min") val min: Double = 0.0,
    @SerializedName("estimated_diameter_max") val max: Double = 0.0
)

data class CloseApproachDto(
    @SerializedName("orbiting_body") val orbitingBody: String = "",
    @SerializedName("miss_distance") val missDistance: DistanceDto? = null,
    @SerializedName("relative_velocity") val relativeVelocity: VelocityDto? = null
)

data class DistanceDto(val kilometers: String = "0")

data class VelocityDto(
    @SerializedName("kilometers_per_hour") val kilometersPerHour: String = "0"
)
