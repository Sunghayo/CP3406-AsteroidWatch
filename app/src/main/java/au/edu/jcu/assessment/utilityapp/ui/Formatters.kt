package au.edu.jcu.assessment.utilityapp.ui

import au.edu.jcu.assessment.utilityapp.model.DistanceUnit
import au.edu.jcu.assessment.utilityapp.model.SizeUnit
import java.text.NumberFormat
import java.util.Locale

private fun number(value: Double, digits: Int = 0): String =
    NumberFormat.getNumberInstance(Locale.US).apply { maximumFractionDigits = digits }.format(value)

fun formatDistance(km: Double, unit: DistanceUnit): String = when (unit) {
    DistanceUnit.KILOMETERS -> "${number(km)} km"
    DistanceUnit.MILES -> "${number(km * 0.621371)} mi"
}

fun formatDiameter(minMeters: Double, maxMeters: Double, unit: SizeUnit): String = when (unit) {
    SizeUnit.METERS -> "${number(minMeters)}–${number(maxMeters)} m"
    SizeUnit.FEET -> "${number(minMeters * 3.28084)}–${number(maxMeters * 3.28084)} ft"
}

fun formatSpeed(kmPerHour: Double, unit: DistanceUnit): String = when (unit) {
    DistanceUnit.KILOMETERS -> "${number(kmPerHour)} km/h"
    DistanceUnit.MILES -> "${number(kmPerHour * 0.621371)} mph"
}
