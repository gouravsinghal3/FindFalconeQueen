package com.example.findqueen.domain.usecase

import com.example.findqueen.domain.model.Vehicle
import com.example.findqueen.domain.repository.FalconeRepository
import javax.inject.Inject

class GetVehiclesUseCase @Inject constructor(
    private val repository: FalconeRepository
) {
    suspend operator fun invoke(): Result<List<Vehicle>> {
        return repository.getVehicles()
    }
}
