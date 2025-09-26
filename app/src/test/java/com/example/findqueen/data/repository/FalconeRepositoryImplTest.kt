package com.example.findqueen.data.repository

import com.example.findqueen.data.remote.api.FalconeApiService
import com.example.findqueen.data.remote.dto.FindFalconeRequest
import com.example.findqueen.data.remote.dto.FindFalconeResponse
import com.example.findqueen.data.remote.dto.PlanetDto
import com.example.findqueen.data.remote.dto.TokenDto
import com.example.findqueen.data.remote.dto.VehicleDto
import com.example.findqueen.domain.model.Planet
import com.example.findqueen.domain.model.Selection
import com.example.findqueen.domain.model.Vehicle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import org.mockito.kotlin.any

class FalconeRepositoryImplTest {
    
    @Mock
    private lateinit var apiService: FalconeApiService
    
    private lateinit var repository: FalconeRepositoryImpl
    
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = FalconeRepositoryImpl(apiService)
    }
    
    @Test
    fun `getPlanets should return success with mapped planets`() = runTest {
        // Given
        val planetDtos = listOf(
            PlanetDto("Donlon", 100),
            PlanetDto("Enchai", 200)
        )
        whenever(apiService.getPlanets()).thenReturn(planetDtos)
        
        // When
        val result = repository.getPlanets()
        
        // Then
        assertTrue(result.isSuccess)
        val planets = result.getOrNull()!!
        assertEquals(2, planets.size)
        assertEquals("Donlon", planets[0].name)
        assertEquals(100, planets[0].distance)
        assertEquals("Enchai", planets[1].name)
        assertEquals(200, planets[1].distance)
    }
    
    @Test
    fun `getVehicles should return success with mapped vehicles`() = runTest {
        // Given
        val vehicleDtos = listOf(
            VehicleDto("Space pod", 2, 200, 2),
            VehicleDto("Space rocket", 1, 300, 4)
        )
        whenever(apiService.getVehicles()).thenReturn(vehicleDtos)
        
        // When
        val result = repository.getVehicles()
        
        // Then
        assertTrue(result.isSuccess)
        val vehicles = result.getOrNull()!!
        assertEquals(2, vehicles.size)
        assertEquals("Space pod", vehicles[0].name)
        assertEquals(2, vehicles[0].totalNo)
        assertEquals(200, vehicles[0].maxDistance)
        assertEquals(2, vehicles[0].speed)
    }
    
    @Test
    fun `getToken should return success with token`() = runTest {
        // Given
        val tokenDto = TokenDto("test-token")
        whenever(apiService.getToken()).thenReturn(tokenDto)
        
        // When
        val result = repository.getToken()
        
        // Then
        assertTrue(result.isSuccess)
        assertEquals("test-token", result.getOrNull())
    }
    
    @Test
    fun `getToken should return failure when api throws exception`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        whenever(apiService.getToken()).thenThrow(exception)
        
        // When
        val result = repository.getToken()
        
        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
    
    @Test
    fun `findFalcone should return success with mapped result`() = runTest {
        // Given
        val token = "test-token"
        val selections = listOf(
            Selection(Planet("Donlon", 100), Vehicle("Space pod", 2, 200, 2)),
            Selection(Planet("Enchai", 200), Vehicle("Space rocket", 1, 300, 4))
        )
        val response = FindFalconeResponse("Donlon", "success", null)
        whenever(apiService.findFalcone(any<FindFalconeRequest>())).thenReturn(response)
        
        // When
        val result = repository.findFalcone(token, selections)
        
        // Then
        assertTrue(result.isSuccess)
        val findingResult = result.getOrNull()!!
        assertEquals("Donlon", findingResult.planetName)
        assertEquals("success", findingResult.status)
        assertNull(findingResult.error)
    }
}
