package com.example.findqueen.data.repository

import com.example.findqueen.data.remote.api.FalconeApiService
import com.example.findqueen.data.remote.dto.FindFalconeRequest
import com.example.findqueen.data.remote.dto.toDomain
import com.example.findqueen.domain.model.FindingResult
import com.example.findqueen.domain.model.Planet
import com.example.findqueen.domain.model.Selection
import com.example.findqueen.domain.model.Vehicle
import com.example.findqueen.domain.repository.FalconeRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FalconeRepositoryImpl @Inject constructor(
    private val apiService: FalconeApiService
) : FalconeRepository {
    
    override suspend fun getPlanets(): Result<List<Planet>> {
        return try {
            val planets = apiService.getPlanets().map { it.toDomain() }
            Result.success(planets)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getVehicles(): Result<List<Vehicle>> {
        return try {
            val vehicles = apiService.getVehicles().map { it.toDomain() }
            Result.success(vehicles)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getToken(): Result<String> {
        return try {
            val token = apiService.getToken().token
            Result.success(token)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun findFalcone(
        token: String,
        selections: List<Selection>
    ): Result<FindingResult> {
        return try {
            val request = FindFalconeRequest(
                token = token,
                planetNames = selections.map { it.planet.name },
                vehicleNames = selections.map { it.vehicle.name }
            )
            val response = apiService.findFalcone(request)
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
