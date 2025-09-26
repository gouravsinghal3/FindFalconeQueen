package com.example.findqueen.data.remote.api

import com.example.findqueen.data.remote.dto.FindFalconeRequest
import com.example.findqueen.data.remote.dto.FindFalconeResponse
import com.example.findqueen.data.remote.dto.PlanetDto
import com.example.findqueen.data.remote.dto.TokenDto
import com.example.findqueen.data.remote.dto.VehicleDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface FalconeApiService {
    
    @GET("planets")
    suspend fun getPlanets(): List<PlanetDto>
    
    @GET("vehicles")
    suspend fun getVehicles(): List<VehicleDto>

    @Headers("Accept: application/json")
    @POST("token")
    suspend fun getToken(): TokenDto

    @Headers("Accept: application/json", "Content-Type: Application/json")
    @POST("find")
    suspend fun findFalcone(@Body request: FindFalconeRequest): FindFalconeResponse
    
    companion object {
        // Using HTTP instead of HTTPS to avoid SSL certificate issues in development
        const val BASE_URL = "https://findfalcone.geektrust.com/"
    }
}
