package au.edu.jcu.assessment.utilityapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import au.edu.jcu.assessment.utilityapp.ui.theme.MobileTech_UtilityAppTheme
import au.edu.jcu.assessment.utilityapp.model.AppSettings
import au.edu.jcu.assessment.utilityapp.model.DistanceUnit
import au.edu.jcu.assessment.utilityapp.model.SizeUnit

@Composable
fun SettingsScreen(
    settings: AppSettings,
    onDistanceChange: (DistanceUnit) -> Unit,
    onSizeChange: (SizeUnit) -> Unit,
    onHazardFilterChange: (Boolean) -> Unit
) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Display preferences", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Distance and speed", fontWeight = FontWeight.SemiBold)
                UnitChoice("Kilometers (km)", settings.distanceUnit == DistanceUnit.KILOMETERS) { onDistanceChange(DistanceUnit.KILOMETERS) }
                UnitChoice("Miles (mi)", settings.distanceUnit == DistanceUnit.MILES) { onDistanceChange(DistanceUnit.MILES) }
            }
        }
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Estimated diameter", fontWeight = FontWeight.SemiBold)
                UnitChoice("Meters (m)", settings.sizeUnit == SizeUnit.METERS) { onSizeChange(SizeUnit.METERS) }
                UnitChoice("Feet (ft)", settings.sizeUnit == SizeUnit.FEET) { onSizeChange(SizeUnit.FEET) }
            }
        }
        Card(Modifier.fillMaxWidth()) {
            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Hazardous objects only", fontWeight = FontWeight.SemiBold)
                    Text("Filter the list, not the daily totals", style = MaterialTheme.typography.bodySmall)
                }
                Switch(checked = settings.showHazardousOnly, onCheckedChange = onHazardFilterChange)
            }
        }
        Text("Settings reset when you close the app.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun UnitChoice(label: String, selected: Boolean, onClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        RadioButton(selected = selected, onClick = null)
        Text(label)
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    MobileTech_UtilityAppTheme {
        SettingsScreen(
            settings = AppSettings(),
            onDistanceChange = {},
            onSizeChange = {},
            onHazardFilterChange = {}
        )
    }
}
