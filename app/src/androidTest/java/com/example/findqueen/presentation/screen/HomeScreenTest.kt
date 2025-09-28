package com.example.findqueen.presentation.screen

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.findqueen.domain.model.GameState
import com.example.findqueen.domain.model.Planet
import com.example.findqueen.domain.model.Selection
import com.example.findqueen.domain.model.Vehicle
import com.example.findqueen.presentation.viewmodel.FindQueenUiState
import com.example.findqueen.presentation.viewmodel.FindQueenViewModel
import com.example.findqueen.ui.theme.FindQueenTheme
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var mockViewModel: FindQueenViewModel
    private lateinit var uiStateFlow: MutableStateFlow<FindQueenUiState>

    @Before
    fun setUp() {
        mockViewModel = mockk(relaxed = true)
        uiStateFlow = MutableStateFlow(FindQueenUiState())
        every { mockViewModel.uiState } returns uiStateFlow
    }

    @Test
    fun homeScreen_displaysTitle() {
        composeTestRule.setContent {
            FindQueenTheme {
                HomeScreen(
                    navController = rememberNavController(),
                    viewModel = mockViewModel
                )
            }
        }

        composeTestRule
            .onNodeWithText("Finding Falcone!")
            .assertIsDisplayed()
    }

    @Test
    fun homeScreen_displaysInstructions() {
        composeTestRule.setContent {
            FindQueenTheme {
                HomeScreen(
                    navController = rememberNavController(),
                    viewModel = mockViewModel
                )
            }
        }

        composeTestRule
            .onNodeWithText("Select planets you want to search in:")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("You need to select 4 planets and vehicles to search for Queen Al Falcone")
            .assertIsDisplayed()
    }

    @Test
    fun homeScreen_showsLoadingState() {
        uiStateFlow.value = FindQueenUiState(isLoading = true)

        composeTestRule.setContent {
            FindQueenTheme {
                HomeScreen(
                    navController = rememberNavController(),
                    viewModel = mockViewModel
                )
            }
        }

        composeTestRule
            .onNodeWithText("Loading planets and vehicles...")
            .assertIsDisplayed()
    }

    @Test
    fun homeScreen_showsErrorState() {
        val errorMessage = "Network error occurred"
        uiStateFlow.value = FindQueenUiState(error = errorMessage)

        composeTestRule.setContent {
            FindQueenTheme {
                HomeScreen(
                    navController = rememberNavController(),
                    viewModel = mockViewModel
                )
            }
        }

        composeTestRule
            .onNodeWithText("Error")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Retry")
            .assertIsDisplayed()
    }

    @Test
    fun homeScreen_showsGameProgress() {
        val planets = listOf(Planet("Donlon", 100), Planet("Enchai", 200))
        val vehicles = listOf(Vehicle("Space pod", 2, 200, 2))
        val selections = listOf(
            Selection(planets[0], vehicles[0]),
            Selection(planets[1], vehicles[0])
        )
        val gameState = GameState(
            selections = selections,
            availableVehicles = mapOf("Space pod" to 0),
            totalTime = 150
        )

        uiStateFlow.value = FindQueenUiState(
            planets = planets,
            vehicles = vehicles,
            gameState = gameState,
            currentStep = 2
        )

        composeTestRule.setContent {
            FindQueenTheme {
                HomeScreen(
                    navController = rememberNavController(),
                    viewModel = mockViewModel
                )
            }
        }

        composeTestRule
            .onNodeWithText("Progress: 2/4 destinations selected")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Total time: 150")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("1. Donlon (Space pod)")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("2. Enchai (Space pod)")
            .assertIsDisplayed()
    }

    @Test
    fun homeScreen_showsStartSelectionButton_whenNoSelections() {
        val gameState = GameState(availableVehicles = mapOf("Space pod" to 2))
        uiStateFlow.value = FindQueenUiState(gameState = gameState)

        composeTestRule.setContent {
            FindQueenTheme {
                HomeScreen(
                    navController = rememberNavController(),
                    viewModel = mockViewModel
                )
            }
        }

        composeTestRule
            .onNodeWithText("Start Selection")
            .assertIsDisplayed()
            .assertIsEnabled()
    }

    @Test
    fun homeScreen_showsFindFalconeButton_whenGameComplete() {
        val selections = listOf(
            Selection(Planet("Donlon", 100), Vehicle("Space pod", 2, 200, 2)),
            Selection(Planet("Enchai", 200), Vehicle("Space rocket", 1, 300, 4)),
            Selection(Planet("Jebing", 300), Vehicle("Space shuttle", 1, 400, 5)),
            Selection(Planet("Sapir", 400), Vehicle("Space ship", 1, 600, 10))
        )
        val gameState = GameState(selections = selections)
        uiStateFlow.value = FindQueenUiState(gameState = gameState)

        composeTestRule.setContent {
            FindQueenTheme {
                HomeScreen(
                    navController = rememberNavController(),
                    viewModel = mockViewModel
                )
            }
        }

        composeTestRule
            .onNodeWithText("Find Falcone!")
            .assertIsDisplayed()
            .assertIsEnabled()
    }

    @Test
    fun homeScreen_clickRetryButton_callsClearError() {
        uiStateFlow.value = FindQueenUiState(error = "Network error")

        composeTestRule.setContent {
            FindQueenTheme {
                HomeScreen(
                    navController = rememberNavController(),
                    viewModel = mockViewModel
                )
            }
        }

        composeTestRule
            .onNodeWithText("Retry")
            .performClick()

        verify { mockViewModel.clearError() }
    }

    @Test
    fun homeScreen_displaysInstructionsCard() {
        composeTestRule.setContent {
            FindQueenTheme {
                HomeScreen(
                    navController = rememberNavController(),
                    viewModel = mockViewModel
                )
            }
        }

        composeTestRule
            .onNodeWithText("Instructions:")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("• Select 4 different planets to search")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("• Choose appropriate vehicles for each planet")
            .assertIsDisplayed()
    }
}
