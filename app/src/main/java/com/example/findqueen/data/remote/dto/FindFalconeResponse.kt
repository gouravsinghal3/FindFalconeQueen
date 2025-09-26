package com.example.findqueen.data.remote.dto

import com.example.findqueen.domain.model.FindingResult
import com.google.gson.annotations.SerializedName

data class FindFalconeResponse(
    @SerializedName("planet_name")
    val planetName: String?,
    @SerializedName("status")
    val status: String,
    @SerializedName("error")
    val error: String?
)

fun FindFalconeResponse.toDomain(): FindingResult {
    return FindingResult(
        planetName = planetName,
        status = status,
        error = error
    )
}
