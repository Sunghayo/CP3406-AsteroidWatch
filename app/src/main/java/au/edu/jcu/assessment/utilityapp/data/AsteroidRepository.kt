package au.edu.jcu.assessment.utilityapp.data

import au.edu.jcu.assessment.utilityapp.BuildConfig
import au.edu.jcu.assessment.utilityapp.model.Asteroid
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AsteroidRepository(
    private val api: NasaApi = Retrofit.Builder()
        .baseUrl("https://api.nasa.gov/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(NasaApi::class.java)
) {
    suspend fun fetchForDate(date: String): List<Asteroid> {
        val response = api.getCloseApproaches(date, date, BuildConfig.NASA_API_KEY)
        return response.nearEarthObjects[date].orEmpty().mapNotNull { dto ->
            // If an approach record is missing, there is no distance to display.
            val approach = dto.closeApproaches.firstOrNull { it.orbitingBody == "Earth" }
                ?: return@mapNotNull null
            val missDistance = approach.missDistance?.kilometers?.toDoubleOrNull()
                ?: return@mapNotNull null
            Asteroid(
                id = dto.id,
                name = dto.name,
                diameterMinMeters = dto.estimatedDiameter?.meters?.min ?: 0.0,
                diameterMaxMeters = dto.estimatedDiameter?.meters?.max ?: 0.0,
                missDistanceKm = missDistance,
                speedKmPerHour = approach.relativeVelocity?.kilometersPerHour?.toDoubleOrNull() ?: 0.0,
                potentiallyHazardous = dto.potentiallyHazardous
            )
        }.sortedBy { it.missDistanceKm }
    }
}
