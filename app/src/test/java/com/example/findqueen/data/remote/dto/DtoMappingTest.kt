package com.example.findqueen.data.remote.dto

import org.junit.Test
import org.junit.Assert.*

class DtoMappingTest {
    
    @Test
    fun `PlanetDto toDomain should map correctly`() {
        // Given
        val planetDto = PlanetDto("Donlon", 100)
        
        // When
        val planet = planetDto.toDomain()
        
        // Then
        assertEquals("Donlon", planet.name)
        assertEquals(100, planet.distance)
    }
    
    @Test
    fun `VehicleDto toDomain should map correctly`() {
        // Given
        val vehicleDto = VehicleDto("Space pod", 2, 200, 2)
        
        // When
        val vehicle = vehicleDto.toDomain()
        
        // Then
        assertEquals("Space pod", vehicle.name)
        assertEquals(2, vehicle.totalNo)
        assertEquals(200, vehicle.maxDistance)
        assertEquals(2, vehicle.speed)
    }
    
    @Test
    fun `FindFalconeResponse toDomain should map correctly for success`() {
        // Given
        val response = FindFalconeResponse("Donlon", "success", null)
        
        // When
        val result = response.toDomain()
        
        // Then
        assertEquals("Donlon", result.planetName)
        assertEquals("success", result.status)
        assertNull(result.error)
    }
    
    @Test
    fun `FindFalconeResponse toDomain should map correctly for failure`() {
        // Given
        val response = FindFalconeResponse(null, "false", null)
        
        // When
        val result = response.toDomain()
        
        // Then
        assertNull(result.planetName)
        assertEquals("false", result.status)
        assertNull(result.error)
    }
    
    @Test
    fun `FindFalconeResponse toDomain should map correctly for error`() {
        // Given
        val response = FindFalconeResponse(null, "error", "Something went wrong")
        
        // When
        val result = response.toDomain()
        
        // Then
        assertNull(result.planetName)
        assertEquals("error", result.status)
        assertEquals("Something went wrong", result.error)
    }
}
