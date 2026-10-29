package au.edu.jcu.assessment.utilityapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.edu.jcu.assessment.utilityapp.data.AsteroidRepository
import au.edu.jcu.assessment.utilityapp.model.AppSettings
import au.edu.jcu.assessment.utilityapp.model.Asteroid
import au.edu.jcu.assessment.utilityapp.model.DistanceUnit
import au.edu.jcu.assessment.utilityapp.model.SizeUnit
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

sealed interface AsteroidUiState {
    data object Loading : AsteroidUiState
    data class Success(val asteroids: List<Asteroid>) : AsteroidUiState
    data class Error(val message: String) : AsteroidUiState
}

class AsteroidViewModel(private val repository: AsteroidRepository = AsteroidRepository()) : ViewModel() {
    var date by mutableStateOf(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE))
        private set

    private val _uiState = MutableStateFlow<AsteroidUiState>(AsteroidUiState.Loading)
    val uiState: StateFlow<AsteroidUiState> = _uiState.asStateFlow()

    private val _settings = MutableStateFlow(AppSettings())
    val settings: StateFlow<AppSettings> = _settings.asStateFlow()

    private var refreshJob: Job? = null

    init { refresh() }

    fun refresh() {
        refreshJob?.cancel()
        date = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        refreshJob = viewModelScope.launch {
            _uiState.value = AsteroidUiState.Loading
            try {
                _uiState.value = AsteroidUiState.Success(repository.fetchForDate(date))
            } catch (e: CancellationException) {
                throw e
            } catch (e: HttpException) {
                val message = if (e.code() == 429) {
                    "NASA's request limit has been reached. Try again later or use your own API key."
                } else {
                    "NASA could not return the data (HTTP ${e.code()}). Please try again."
                }
                _uiState.value = AsteroidUiState.Error(message)
            } catch (e: IOException) {
                _uiState.value = AsteroidUiState.Error("Could not connect to NASA. Check your Internet connection.")
            } catch (e: Exception) {
                _uiState.value = AsteroidUiState.Error("Could not read today's asteroid data. Please try again.")
            }
        }
    }

    fun setDistanceUnit(unit: DistanceUnit) {
        _settings.update { it.copy(distanceUnit = unit) }
    }

    fun setSizeUnit(unit: SizeUnit) {
        _settings.update { it.copy(sizeUnit = unit) }
    }

    fun setHazardousOnly(enabled: Boolean) {
        _settings.update { it.copy(showHazardousOnly = enabled) }
    }
}
