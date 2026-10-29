package au.edu.jcu.assessment.utilityapp

import au.edu.jcu.assessment.utilityapp.data.AsteroidRepository
import au.edu.jcu.assessment.utilityapp.data.CloseApproachDto
import au.edu.jcu.assessment.utilityapp.data.DiameterDto
import au.edu.jcu.assessment.utilityapp.data.DistanceDto
import au.edu.jcu.assessment.utilityapp.data.NasaApi
import au.edu.jcu.assessment.utilityapp.data.NeoDto
import au.edu.jcu.assessment.utilityapp.data.NeoFeedResponse
import au.edu.jcu.assessment.utilityapp.data.RangeDto
import au.edu.jcu.assessment.utilityapp.data.VelocityDto
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AsteroidRepositoryTest {
    private val date = "2026-10-29"

    private fun objectWithApproach(id: String, distance: String, hazardous: Boolean): NeoDto = NeoDto(
        id = id,
        name = "Test asteroid $id",
        potentiallyHazardous = hazardous,
        estimatedDiameter = DiameterDto(RangeDto(12.0, 20.0)),
        closeApproaches = listOf(CloseApproachDto(
            orbitingBody = "Earth",
            missDistance = DistanceDto(distance),
            relativeVelocity = VelocityDto("18000")
        ))
    )

    private fun repoFor(objects: List<NeoDto>) = AsteroidRepository(object : NasaApi {
        override suspend fun getCloseApproaches(
            startDate: String,
            endDate: String,
            apiKey: String
        ): NeoFeedResponse {
            assertEquals(date, startDate)
            assertEquals(date, endDate)
            return NeoFeedResponse(mapOf(date to objects))
        }
    })

    @Test fun sortsByNearestDistanceAndKeepsHazardFlag() = runBlocking {
        val results = repoFor(listOf(
            objectWithApproach("far", "25000", false),
            objectWithApproach("near", "1500", true)
        )).fetchForDate(date)

        assertEquals(listOf("near", "far"), results.map { it.id })
        assertEquals(1500.0, results.first().missDistanceKm, 0.0)
        assertEquals(18000.0, results.first().speedKmPerHour, 0.0)
        assertTrue(results.first().potentiallyHazardous)
        assertFalse(results.last().potentiallyHazardous)
    }

    @Test fun skipsObjectsWithMissingOrInvalidEarthDistance() = runBlocking {
        val noEarthApproach = NeoDto(
            id = "other", name = "Other",
            closeApproaches = listOf(CloseApproachDto(orbitingBody = "Mars"))
        )
        val results = repoFor(listOf(
            noEarthApproach,
            objectWithApproach("invalid", "not-a-number", false),
            objectWithApproach("valid", "1234", false)
        )).fetchForDate(date)

        assertEquals(listOf("valid"), results.map { it.id })
    }

    @Test fun returnsEmptyListWhenDateHasNoObjects() = runBlocking {
        assertTrue(repoFor(emptyList()).fetchForDate(date).isEmpty())
    }
}
