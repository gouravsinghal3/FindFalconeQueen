package com.example.findqueen.domain.model

import org.junit.Test
import org.junit.Assert.*

class GameStateTest {
    
    @Test
    fun `isComplete should return true when 4 selections are made`() {
        // Given
        val selections = listOf(
            Selection(Planet("A", 100), Vehicle("V1", 1, 200, 2)),
            Selection(Planet("B", 150), Vehicle("V2", 1, 300, 3)),
            Selection(Planet("C", 200), Vehicle("V3", 1, 400, 4)),
            Selection(Planet("D", 250), Vehicle("V4", 1, 500, 5))
        )
        val gameState = GameState(selections = selections)
        
        // Then
        assertTrue(gameState.isComplete)
    }
    
    @Test
    fun `isComplete should return false when less than 4 selections are made`() {
        // Given
        val selections = listOf(
            Selection(Planet("A", 100), Vehicle("V1", 1, 200, 2)),
            Selection(Planet("B", 150), Vehicle("V2", 1, 300, 3))
        )
        val gameState = GameState(selections = selections)
        
        // Then
        assertFalse(gameState.isComplete)
    }
    
    @Test
    fun `canSelectVehicle should return true when vehicle is available`() {
        // Given
        val availableVehicles = mapOf("Space pod" to 2, "Space rocket" to 1)
        val gameState = GameState(availableVehicles = availableVehicles)
        val vehicle = Vehicle("Space pod", 2, 200, 2)
        
        // Then
        assertTrue(gameState.canSelectVehicle(vehicle))
    }
    
    @Test
    fun `canSelectVehicle should return false when vehicle is not available`() {
        // Given
        val availableVehicles = mapOf("Space pod" to 0, "Space rocket" to 1)
        val gameState = GameState(availableVehicles = availableVehicles)
        val vehicle = Vehicle("Space pod", 2, 200, 2)
        
        // Then
        assertFalse(gameState.canSelectVehicle(vehicle))
    }
    
    @Test
    fun `canSelectVehicle should return false when vehicle is not in available map`() {
        // Given
        val availableVehicles = mapOf("Space rocket" to 1)
        val gameState = GameState(availableVehicles = availableVehicles)
        val vehicle = Vehicle("Space pod", 2, 200, 2)
        
        // Then
        assertFalse(gameState.canSelectVehicle(vehicle))
    }
    
    @Test
    fun `getAvailableCount should return correct count when vehicle exists`() {
        // Given
        val availableVehicles = mapOf("Space pod" to 3, "Space rocket" to 1)
        val gameState = GameState(availableVehicles = availableVehicles)
        
        // Then
        assertEquals(3, gameState.getAvailableCount("Space pod"))
        assertEquals(1, gameState.getAvailableCount("Space rocket"))
    }
    
    @Test
    fun `getAvailableCount should return 0 when vehicle does not exist`() {
        // Given
        val availableVehicles = mapOf("Space pod" to 3)
        val gameState = GameState(availableVehicles = availableVehicles)
        
        // Then
        assertEquals(0, gameState.getAvailableCount("Space rocket"))
    }
}
