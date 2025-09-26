package com.example.findqueen.domain.usecase

import com.example.findqueen.domain.model.Vehicle
import com.example.findqueen.domain.repository.FalconeRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class GetVehiclesUseCaseTest {
    
    @Mock
    private lateinit var repository: FalconeRepository
    
    private lateinit var useCase: GetVehiclesUseCase
    
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        useCase = GetVehiclesUseCase(repository)
    }
    
    @Test
    fun `invoke should return success with vehicles when repository returns success`() = runTest {
        // Given
        val expectedVehicles = listOf(
            Vehicle("Space pod", 2, 200, 2),
            Vehicle("Space rocket", 1, 300, 4),
            Vehicle("Space shuttle", 1, 400, 5)
        )
        whenever(repository.getVehicles()).thenReturn(Result.success(expectedVehicles))
        
        // When
        val result = useCase()
        
        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedVehicles, result.getOrNull())
    }
    
    @Test
    fun `invoke should return failure when repository returns failure`() = runTest {
        // Given
        val exception = Exception("Network error")
        whenever(repository.getVehicles()).thenReturn(Result.failure(exception))
        
        // When
        val result = useCase()
        
        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
