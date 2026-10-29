package au.edu.jcu.assessment.utilityapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import au.edu.jcu.assessment.utilityapp.ui.theme.MobileTech_UtilityAppTheme
import au.edu.jcu.assessment.utilityapp.AsteroidUiState
import au.edu.jcu.assessment.utilityapp.model.AppSettings
import au.edu.jcu.assessment.utilityapp.model.Asteroid

@Composable
fun UtilityScreen(
    date: String,
    state: AsteroidUiState,
    settings: AppSettings,
    onRefresh: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 18.dp, vertical = 16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Asteroid Watch", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text("Close approaches • $date", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            OutlinedButton(onClick = onRefresh) { Text("Refresh") }
        }
        Spacer(Modifier.height(18.dp))
        when (state) {
            AsteroidUiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(Modifier.height(12.dp))
                    Text("Loading NASA data…")
                }
            }
            is AsteroidUiState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(state.message, style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = onRefresh) { Text("Try again") }
                }
            }
            is AsteroidUiState.Success -> {
                val all = state.asteroids
                val visible = if (settings.showHazardousOnly) all.filter { it.potentiallyHazardous } else all
                val potentiallyHazardous = all.count { it.potentiallyHazardous }
                SummaryCard(all.size, potentiallyHazardous)
                Spacer(Modifier.height(18.dp))
                Text("Today's objects", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(4.dp))
                Text(
                    if (settings.showHazardousOnly) "Showing potentially hazardous objects" else "Sorted by closest approach",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(12.dp))
                if (visible.isEmpty()) {
                    Text(
                        if (settings.showHazardousOnly && all.isNotEmpty())
                            "No potentially hazardous objects in today's results."
                        else "No close approaches found for this date.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(visible, key = { it.id }) { asteroid ->
                            AsteroidCard(asteroid, settings)
                        }
                        item { Spacer(Modifier.height(10.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryCard(total: Int, hazardous: Int) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
            Text("AT A GLANCE", style = MaterialTheme.typography.labelMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("$total", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                    Text("Close approaches", style = MaterialTheme.typography.bodySmall)
                }
                Column(Modifier.weight(1f)) {
                    Text("$hazardous", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                    Text("Potentially hazardous", style = MaterialTheme.typography.bodySmall)
                }
            }
            HorizontalDivider()
            Text(
                "Potentially hazardous does not mean an impact is predicted.",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun AsteroidCard(asteroid: Asteroid, settings: AppSettings) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(asteroid.name, modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                if (asteroid.potentiallyHazardous) {
                    Text("PHA", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }
            }
            InfoRow("Miss distance", formatDistance(asteroid.missDistanceKm, settings.distanceUnit))
            InfoRow("Estimated diameter", formatDiameter(asteroid.diameterMinMeters, asteroid.diameterMaxMeters, settings.sizeUnit))
            InfoRow("Relative speed", formatSpeed(asteroid.speedKmPerHour, settings.distanceUnit))
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.width(8.dp))
        Text(value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
    }
}


@Preview(showBackground = true)
@Composable
private fun TodayScreenPreview() {
    MobileTech_UtilityAppTheme {
        UtilityScreen(
            date = "2026-10-29",
            state = AsteroidUiState.Success(listOf(
                Asteroid("preview-1", "(2026 AB)", 25.0, 54.0, 360000.0, 12500.0, false),
                Asteroid("preview-2", "(2026 CD)", 135.0, 240.0, 870000.0, 35000.0, true)
            )),
            settings = AppSettings(),
            onRefresh = {}
        )
    }
}
