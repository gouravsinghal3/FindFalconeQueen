package com.example.findqueen.domain.model

data class Selection(
    val planet: Planet,
    val vehicle: Vehicle
)

data class GameState(
    val selections: List<Selection> = emptyList(),
    val availableVehicles: Map<String, Int> = emptyMap(),
    val totalTime: Int = 0
) {
    val isComplete: Boolean
        get() = selections.size == 4
    
    fun canSelectVehicle(vehicle: Vehicle): Boolean {
        return availableVehicles[vehicle.name] ?: 0 > 0
    }
    
    fun getAvailableCount(vehicleName: String): Int {
        return availableVehicles[vehicleName] ?: 0
    }
}
