package au.edu.jcu.assessment.utilityapp.model

data class Asteroid(
    val id: String,
    val name: String,
    val diameterMinMeters: Double,
    val diameterMaxMeters: Double,
    val missDistanceKm: Double,
    val speedKmPerHour: Double,
    val potentiallyHazardous: Boolean
)

enum class DistanceUnit { KILOMETERS, MILES }
enum class SizeUnit { METERS, FEET }

data class AppSettings(
    val distanceUnit: DistanceUnit = DistanceUnit.KILOMETERS,
    val sizeUnit: SizeUnit = SizeUnit.METERS,
    val showHazardousOnly: Boolean = false
)
