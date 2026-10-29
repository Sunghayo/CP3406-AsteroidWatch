package au.edu.jcu.assessment.utilityapp

import au.edu.jcu.assessment.utilityapp.model.DistanceUnit
import au.edu.jcu.assessment.utilityapp.model.SizeUnit
import au.edu.jcu.assessment.utilityapp.ui.formatDiameter
import au.edu.jcu.assessment.utilityapp.ui.formatDistance
import au.edu.jcu.assessment.utilityapp.ui.formatSpeed
import org.junit.Assert.assertEquals
import org.junit.Test

class FormattingTest {
    @Test fun distanceIsConvertedToMiles() {
        assertEquals("621 mi", formatDistance(1000.0, DistanceUnit.MILES))
    }

    @Test fun diameterIsConvertedToFeet() {
        assertEquals("3–7 ft", formatDiameter(1.0, 2.0, SizeUnit.FEET))
    }

    @Test fun speedUsesTheSelectedUnit() {
        assertEquals("62 mph", formatSpeed(100.0, DistanceUnit.MILES))
    }
}
