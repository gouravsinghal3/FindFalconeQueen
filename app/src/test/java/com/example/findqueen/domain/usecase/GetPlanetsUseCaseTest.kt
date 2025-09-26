package com.example.findqueen.domain.usecase

import com.example.findqueen.domain.model.Planet
import com.example.findqueen.domain.repository.FalconeRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class GetPlanetsUseCaseTest {
    
    @Mock
    private lateinit var repository: FalconeRepository
    
    private lateinit var useCase: GetPlanetsUseCase
    
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        useCase = GetPlanetsUseCase(repository)
    }
    
    @Test
    fun `invoke should return success with planets when repository returns success`() = runTest {
        // Given
        val expectedPlanets = listOf(
            Planet("Donlon", 100),
            Planet("Enchai", 200),
            Planet("Jebing", 300)
        )
        whenever(repository.getPlanets()).thenReturn(Result.success(expectedPlanets))
        
        // When
        val result = useCase()
        
        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedPlanets, result.getOrNull())
    }
    
    @Test
    fun `invoke should return failure when repository returns failure`() = runTest {
        // Given
        val exception = Exception("Network error")
        whenever(repository.getPlanets()).thenReturn(Result.failure(exception))
        
        // When
        val result = useCase()
        
        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
