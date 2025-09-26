package com.example.findqueen.domain.model

data class FindingResult(
    val planetName: String?,
    val status: String,
    val error: String? = null
)
