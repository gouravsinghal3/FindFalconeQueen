package com.example.findqueen.domain.usecase

import com.example.findqueen.domain.model.Planet
import com.example.findqueen.domain.repository.FalconeRepository
import javax.inject.Inject

class GetPlanetsUseCase @Inject constructor(
    private val repository: FalconeRepository
) {
    suspend operator fun invoke(): Result<List<Planet>> {
        return repository.getPlanets()
    }
}
