package com.example.findqueen.presentation.viewmodel

import com.example.findqueen.domain.model.FindingResult
import com.example.findqueen.domain.model.Planet
import com.example.findqueen.domain.model.Selection
import com.example.findqueen.domain.model.Vehicle
import com.example.findqueen.domain.usecase.FindFalconeUseCase
import com.example.findqueen.domain.usecase.GetPlanetsUseCase
import com.example.findqueen.domain.usecase.GetVehiclesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import org.mockito.kotlin.any
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalCoroutinesApi::class)
class FindQueenViewModelTest {
    
    @Mock
    private lateinit var getPlanetsUseCase: GetPlanetsUseCase
    
    @Mock
    private lateinit var getVehiclesUseCase: GetVehiclesUseCase
    
    @Mock
    private lateinit var findFalconeUseCase: FindFalconeUseCase
    
    private lateinit var viewModel: FindQueenViewModel
    
    private val testDispatcher = StandardTestDispatcher()
    
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    
    @Test
    fun `should load planets and vehicles successfully`() = runTest {
        // Given
        val planets = listOf(Planet("Donlon", 100), Planet("Enchai", 200))
        val vehicles = listOf(Vehicle("Space pod", 2, 200, 2))
        whenever(getPlanetsUseCase()).thenReturn(Result.success(planets))
        whenever(getVehiclesUseCase()).thenReturn(Result.success(vehicles))
        
        // When
        viewModel = FindQueenViewModel(getPlanetsUseCase, getVehiclesUseCase, findFalconeUseCase)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertEquals(planets, state.planets)
        assertEquals(vehicles, state.vehicles)
        assertEquals(mapOf("Space pod" to 2), state.gameState.availableVehicles)
    }
    
    @Test
    fun `should handle error when loading fails`() = runTest {
        // Given
        val exception = Exception("Network error")
        whenever(getPlanetsUseCase()).thenReturn(Result.failure(exception))
        whenever(getVehiclesUseCase()).thenReturn(Result.success(emptyList()))
        
        // When
        viewModel = FindQueenViewModel(getPlanetsUseCase, getVehiclesUseCase, findFalconeUseCase)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Network error", state.error)
    }
    
    @Test
    fun `selectPlanetAndVehicle should add selection when valid`() = runTest {
        // Given
        val planets = listOf(Planet("Donlon", 100))
        val vehicles = listOf(Vehicle("Space pod", 2, 200, 2))
        whenever(getPlanetsUseCase()).thenReturn(Result.success(planets))
        whenever(getVehiclesUseCase()).thenReturn(Result.success(vehicles))
        
        viewModel = FindQueenViewModel(getPlanetsUseCase, getVehiclesUseCase, findFalconeUseCase)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val planet = Planet("Donlon", 100)
        val vehicle = Vehicle("Space pod", 2, 200, 2)
        
        // When
        viewModel.selectPlanetAndVehicle(planet, vehicle)
        
        // Then
        val state = viewModel.uiState.value
        assertEquals(1, state.gameState.selections.size)
        assertEquals(planet, state.gameState.selections[0].planet)
        assertEquals(vehicle, state.gameState.selections[0].vehicle)
        assertEquals(1, state.gameState.availableVehicles["Space pod"])
        assertEquals(50, state.gameState.totalTime) // 100 / 2 = 50
        assertEquals(1, state.currentStep)
    }
    
    @Test
    fun `selectPlanetAndVehicle should show error when vehicle not available`() = runTest {
        // Given
        val planets = listOf(Planet("Donlon", 100))
        val vehicles = listOf(Vehicle("Space pod", 0, 200, 2)) // No vehicles available
        whenever(getPlanetsUseCase()).thenReturn(Result.success(planets))
        whenever(getVehiclesUseCase()).thenReturn(Result.success(vehicles))
        
        viewModel = FindQueenViewModel(getPlanetsUseCase, getVehiclesUseCase, findFalconeUseCase)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val planet = Planet("Donlon", 100)
        val vehicle = Vehicle("Space pod", 0, 200, 2)
        
        // When
        viewModel.selectPlanetAndVehicle(planet, vehicle)
        
        // Then
        val state = viewModel.uiState.value
        assertEquals("Vehicle Space pod is not available", state.error)
        assertEquals(0, state.gameState.selections.size)
    }
    
    @Test
    fun `selectPlanetAndVehicle should show error when vehicle cannot reach planet`() = runTest {
        // Given
        val planets = listOf(Planet("Donlon", 300))
        val vehicles = listOf(Vehicle("Space pod", 2, 200, 2)) // Max distance 200, planet distance 300
        whenever(getPlanetsUseCase()).thenReturn(Result.success(planets))
        whenever(getVehiclesUseCase()).thenReturn(Result.success(vehicles))
        
        viewModel = FindQueenViewModel(getPlanetsUseCase, getVehiclesUseCase, findFalconeUseCase)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val planet = Planet("Donlon", 300)
        val vehicle = Vehicle("Space pod", 2, 200, 2)
        
        // When
        viewModel.selectPlanetAndVehicle(planet, vehicle)
        
        // Then
        val state = viewModel.uiState.value
        assertEquals("Vehicle Space pod cannot reach Donlon", state.error)
        assertEquals(0, state.gameState.selections.size)
    }
    
    @Test
    fun `findFalcone should succeed when game is complete`() = runTest {
        // Given
        val planets = listOf(Planet("Donlon", 100))
        val vehicles = listOf(Vehicle("Space pod", 4, 200, 2))
        whenever(getPlanetsUseCase()).thenReturn(Result.success(planets))
        whenever(getVehiclesUseCase()).thenReturn(Result.success(vehicles))
        
        val findingResult = FindingResult("Donlon", "success")
        whenever(findFalconeUseCase(any())).thenReturn(Result.success(findingResult))
        
        viewModel = FindQueenViewModel(getPlanetsUseCase, getVehiclesUseCase, findFalconeUseCase)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Add 4 selections to complete the game
        repeat(4) {
            viewModel.selectPlanetAndVehicle(Planet("Donlon", 100), Vehicle("Space pod", 4, 200, 2))
        }
        
        var navigated = false
        
        // When
        viewModel.findFalcone { navigated = true }
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(findingResult, state.findingResult)
        assertTrue(navigated)
    }
    
    @Test
    fun `findFalcone should show error when game is not complete`() = runTest {
        // Given
        val planets = listOf(Planet("Donlon", 100))
        val vehicles = listOf(Vehicle("Space pod", 4, 200, 2))
        whenever(getPlanetsUseCase()).thenReturn(Result.success(planets))
        whenever(getVehiclesUseCase()).thenReturn(Result.success(vehicles))
        
        viewModel = FindQueenViewModel(getPlanetsUseCase, getVehiclesUseCase, findFalconeUseCase)
        testDispatcher.scheduler.advanceUntilIdle()
        
        var navigated = false
        
        // When
        viewModel.findFalcone { navigated = true }
        
        // Then
        val state = viewModel.uiState.value
        assertEquals("Please select all 4 destinations", state.error)
        assertFalse(navigated)
    }
    
    @Test
    fun `resetGame should reset to initial state`() = runTest {
        // Given
        val planets = listOf(Planet("Donlon", 100))
        val vehicles = listOf(Vehicle("Space pod", 2, 200, 2))
        whenever(getPlanetsUseCase()).thenReturn(Result.success(planets))
        whenever(getVehiclesUseCase()).thenReturn(Result.success(vehicles))
        
        viewModel = FindQueenViewModel(getPlanetsUseCase, getVehiclesUseCase, findFalconeUseCase)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Add a selection
        viewModel.selectPlanetAndVehicle(Planet("Donlon", 100), Vehicle("Space pod", 2, 200, 2))
        
        // When
        viewModel.resetGame()
        
        // Then
        val state = viewModel.uiState.value
        assertEquals(0, state.gameState.selections.size)
        assertEquals(mapOf("Space pod" to 2), state.gameState.availableVehicles)
        assertEquals(0, state.gameState.totalTime)
        assertEquals(0, state.currentStep)
        assertNull(state.findingResult)
        assertNull(state.error)
    }
    
    @Test
    fun `clearError should clear error message`() = runTest {
        // Given
        val planets = listOf(Planet("Donlon", 100))
        val vehicles = listOf(Vehicle("Space pod", 0, 200, 2))
        whenever(getPlanetsUseCase()).thenReturn(Result.success(planets))
        whenever(getVehiclesUseCase()).thenReturn(Result.success(vehicles))
        
        viewModel = FindQueenViewModel(getPlanetsUseCase, getVehiclesUseCase, findFalconeUseCase)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Generate an error
        viewModel.selectPlanetAndVehicle(Planet("Donlon", 100), Vehicle("Space pod", 0, 200, 2))
        assertNotNull(viewModel.uiState.value.error)
        
        // When
        viewModel.clearError()
        
        // Then
        assertNull(viewModel.uiState.value.error)
    }
}
