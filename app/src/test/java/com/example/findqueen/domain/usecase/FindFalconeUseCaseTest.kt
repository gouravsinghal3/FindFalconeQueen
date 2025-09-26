package com.example.findqueen.domain.usecase

import com.example.findqueen.domain.model.FindingResult
import com.example.findqueen.domain.model.Planet
import com.example.findqueen.domain.model.Selection
import com.example.findqueen.domain.model.Vehicle
import com.example.findqueen.domain.repository.FalconeRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class FindFalconeUseCaseTest {
    
    @Mock
    private lateinit var repository: FalconeRepository
    
    private lateinit var useCase: FindFalconeUseCase
    
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        useCase = FindFalconeUseCase(repository)
    }
    
    @Test
    fun `invoke should return success when token and finding are successful`() = runTest {
        // Given
        val token = "test-token"
        val selections = listOf(
            Selection(Planet("Donlon", 100), Vehicle("Space pod", 2, 200, 2)),
            Selection(Planet("Enchai", 200), Vehicle("Space rocket", 1, 300, 4))
        )
        val expectedResult = FindingResult("Donlon", "success")
        
        whenever(repository.getToken()).thenReturn(Result.success(token))
        whenever(repository.findFalcone(token, selections)).thenReturn(Result.success(expectedResult))
        
        // When
        val result = useCase(selections)
        
        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedResult, result.getOrNull())
    }
    
    @Test
    fun `invoke should return failure when token request fails`() = runTest {
        // Given
        val selections = listOf(
            Selection(Planet("Donlon", 100), Vehicle("Space pod", 2, 200, 2))
        )
        val tokenException = Exception("Token error")
        
        whenever(repository.getToken()).thenReturn(Result.failure(tokenException))
        
        // When
        val result = useCase(selections)
        
        // Then
        assertTrue(result.isFailure)
        assertEquals(tokenException, result.exceptionOrNull())
    }
    
    @Test
    fun `invoke should return failure when finding request fails`() = runTest {
        // Given
        val token = "test-token"
        val selections = listOf(
            Selection(Planet("Donlon", 100), Vehicle("Space pod", 2, 200, 2))
        )
        val findingException = Exception("Finding error")
        
        whenever(repository.getToken()).thenReturn(Result.success(token))
        whenever(repository.findFalcone(token, selections)).thenReturn(Result.failure(findingException))
        
        // When
        val result = useCase(selections)
        
        // Then
        assertTrue(result.isFailure)
        assertEquals(findingException, result.exceptionOrNull())
    }
}
