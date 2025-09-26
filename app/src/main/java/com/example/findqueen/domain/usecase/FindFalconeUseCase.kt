package com.example.findqueen.domain.usecase

import com.example.findqueen.domain.model.FindingResult
import com.example.findqueen.domain.model.Selection
import com.example.findqueen.domain.repository.FalconeRepository
import javax.inject.Inject

class FindFalconeUseCase @Inject constructor(
    private val repository: FalconeRepository
) {
    suspend operator fun invoke(selections: List<Selection>): Result<FindingResult> {
        return try {
            val tokenResult = repository.getToken()
            if (tokenResult.isFailure) {
                return Result.failure(tokenResult.exceptionOrNull() ?: Exception("Failed to get token"))
            }
            
            val token = tokenResult.getOrThrow()
            repository.findFalcone(token, selections)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
