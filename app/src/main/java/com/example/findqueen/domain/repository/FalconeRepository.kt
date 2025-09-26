package com.example.findqueen.domain.repository

import com.example.findqueen.domain.model.FindingResult
import com.example.findqueen.domain.model.Planet
import com.example.findqueen.domain.model.Selection
import com.example.findqueen.domain.model.Vehicle

interface FalconeRepository {
    suspend fun getPlanets(): Result<List<Planet>>
    suspend fun getVehicles(): Result<List<Vehicle>>
    suspend fun getToken(): Result<String>
    suspend fun findFalcone(
        token: String,
        selections: List<Selection>
    ): Result<FindingResult>
}
