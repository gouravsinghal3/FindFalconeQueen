package com.example.findqueen.data.remote.dto

import com.example.findqueen.domain.model.Planet
import com.google.gson.annotations.SerializedName

data class PlanetDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("distance")
    val distance: Int
)

fun PlanetDto.toDomain(): Planet {
    return Planet(
        name = name,
        distance = distance
    )
}
