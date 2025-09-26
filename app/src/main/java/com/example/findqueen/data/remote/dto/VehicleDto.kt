package com.example.findqueen.data.remote.dto

import com.example.findqueen.domain.model.Vehicle
import com.google.gson.annotations.SerializedName

data class VehicleDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("total_no")
    val totalNo: Int,
    @SerializedName("max_distance")
    val maxDistance: Int,
    @SerializedName("speed")
    val speed: Int
)

fun VehicleDto.toDomain(): Vehicle {
    return Vehicle(
        name = name,
        totalNo = totalNo,
        maxDistance = maxDistance,
        speed = speed
    )
}
