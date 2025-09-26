package com.example.findqueen.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findqueen.domain.model.FindingResult
import com.example.findqueen.domain.model.GameState
import com.example.findqueen.domain.model.Planet
import com.example.findqueen.domain.model.Selection
import com.example.findqueen.domain.model.Vehicle
import com.example.findqueen.domain.usecase.FindFalconeUseCase
import com.example.findqueen.domain.usecase.GetPlanetsUseCase
import com.example.findqueen.domain.usecase.GetVehiclesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FindQueenUiState(
    val isLoading: Boolean = false,
    val planets: List<Planet> = emptyList(),
    val vehicles: List<Vehicle> = emptyList(),
    val gameState: GameState = GameState(),
    val findingResult: FindingResult? = null,
    val error: String? = null,
    val currentStep: Int = 0
)

@HiltViewModel
class FindQueenViewModel @Inject constructor(
    private val getPlanetsUseCase: GetPlanetsUseCase,
    private val getVehiclesUseCase: GetVehiclesUseCase,
    private val findFalconeUseCase: FindFalconeUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(FindQueenUiState())
    val uiState: StateFlow<FindQueenUiState> = _uiState.asStateFlow()
    
    init {
        loadInitialData()
    }
    
    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                val planetsResult = getPlanetsUseCase()
                val vehiclesResult = getVehiclesUseCase()
                
                if (planetsResult.isSuccess && vehiclesResult.isSuccess) {
                    val planets = planetsResult.getOrThrow()
                    val vehicles = vehiclesResult.getOrThrow()
                    val availableVehicles = vehicles.associate { it.name to it.totalNo }
                    
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            planets = planets,
                            vehicles = vehicles,
                            gameState = GameState(availableVehicles = availableVehicles)
                        )
                    }
                } else {
                    val error = planetsResult.exceptionOrNull()?.message 
                        ?: vehiclesResult.exceptionOrNull()?.message 
                        ?: "Unknown error occurred"
                    _uiState.update { 
                        it.copy(isLoading = false, error = error) 
                    }
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(isLoading = false, error = e.message ?: "Unknown error occurred") 
                }
            }
        }
    }
    
    fun selectPlanetAndVehicle(planet: Planet, vehicle: Vehicle) {
        val currentState = _uiState.value
        val gameState = currentState.gameState
        
        if (!gameState.canSelectVehicle(vehicle)) {
            _uiState.update { it.copy(error = "Vehicle ${vehicle.name} is not available") }
            return
        }
        
        if (planet.distance > vehicle.maxDistance) {
            _uiState.update { it.copy(error = "Vehicle ${vehicle.name} cannot reach ${planet.name}") }
            return
        }
        
        val selection = Selection(planet, vehicle)
        val newSelections = gameState.selections + selection
        val newAvailableVehicles = gameState.availableVehicles.toMutableMap().apply {
            this[vehicle.name] = (this[vehicle.name] ?: 0) - 1
        }
        val timeTaken = planet.distance / vehicle.speed
        val newTotalTime = gameState.totalTime + timeTaken
        
        val newGameState = GameState(
            selections = newSelections,
            availableVehicles = newAvailableVehicles,
            totalTime = newTotalTime
        )
        
        _uiState.update {
            it.copy(
                gameState = newGameState,
                currentStep = newSelections.size,
                error = null
            )
        }
    }
    
    fun findFalcone(onNavigateToResult: () -> Unit) {
        val currentState = _uiState.value
        if (!currentState.gameState.isComplete) {
            _uiState.update { it.copy(error = "Please select all 4 destinations") }
            return
        }
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                val result = findFalconeUseCase(currentState.gameState.selections)
                if (result.isSuccess) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            findingResult = result.getOrThrow()
                        )
                    }
                    onNavigateToResult()
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.exceptionOrNull()?.message ?: "Failed to find Falcone"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error occurred"
                    )
                }
            }
        }
    }
    
    fun resetGame() {
        val currentState = _uiState.value
        val initialAvailableVehicles = currentState.vehicles.associate { it.name to it.totalNo }
        
        _uiState.update {
            it.copy(
                gameState = GameState(availableVehicles = initialAvailableVehicles),
                findingResult = null,
                error = null,
                currentStep = 0
            )
        }
    }
    
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
