package com.example.findqueen.presentation.screen

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.findqueen.domain.model.FindingResult
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


class ResultScreenTest {

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
    fun resultScreen_displaysTitle() {
        composeTestRule.setContent {
            FindQueenTheme {
                ResultScreen(
                    navController = rememberNavController(),
                    viewModel = mockViewModel
                )
            }
        }

        composeTestRule
            .onNodeWithText("Mission Result")
            .assertIsDisplayed()
    }

    @Test
    fun resultScreen_showsLoadingState() {
        uiStateFlow.value = FindQueenUiState(isLoading = true)

        composeTestRule.setContent {
            FindQueenTheme {
                ResultScreen(
                    navController = rememberNavController(),
                    viewModel = mockViewModel
                )
            }
        }

        composeTestRule
            .onNodeWithText("Searching for Queen Al Falcone...")
            .assertIsDisplayed()
    }

    @Test
    fun resultScreen_showsSuccessResult() {
        val findingResult = FindingResult("Donlon", "success")
        val selections = listOf(
            Selection(Planet("Donlon", 100), Vehicle("Space pod", 2, 200, 2))
        )
        val gameState = GameState(selections = selections, totalTime = 50)

        uiStateFlow.value = FindQueenUiState(
            findingResult = findingResult,
            gameState = gameState
        )

        composeTestRule.setContent {
            FindQueenTheme {
                ResultScreen(
                    navController = rememberNavController(),
                    viewModel = mockViewModel
                )
            }
        }

        composeTestRule
            .onNodeWithText("Success!")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Congratulations! You found Queen Al Falcone on planet Donlon!")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Total time taken: 50")
            .assertIsDisplayed()
    }

    @Test
    fun resultScreen_showsFailureResult() {
        val findingResult = FindingResult(null, "false")
        val gameState = GameState(totalTime = 100)

        uiStateFlow.value = FindQueenUiState(
            findingResult = findingResult,
            gameState = gameState
        )

        composeTestRule.setContent {
            FindQueenTheme {
                ResultScreen(
                    navController = rememberNavController(),
                    viewModel = mockViewModel
                )
            }
        }

        composeTestRule
            .onNodeWithText("Not Found!")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Queen Al Falcone was not found in any of the selected planets. Better luck next time!")
            .assertIsDisplayed()
    }

    @Test
    fun resultScreen_showsErrorResult() {
        val findingResult = FindingResult(null, "error", "Something went wrong")

        uiStateFlow.value = FindQueenUiState(findingResult = findingResult)

        composeTestRule.setContent {
            FindQueenTheme {
                ResultScreen(
                    navController = rememberNavController(),
                    viewModel = mockViewModel
                )
            }
        }

        composeTestRule
            .onNodeWithText("Error!")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Something went wrong")
            .assertIsDisplayed()
    }

    @Test
    fun resultScreen_showsMissionSummary() {
        val selections = listOf(
            Selection(Planet("Donlon", 100), Vehicle("Space pod", 2, 200, 2)),
            Selection(Planet("Enchai", 200), Vehicle("Space rocket", 1, 300, 4))
        )
        val gameState = GameState(selections = selections)
        val findingResult = FindingResult("Donlon", "success")

        uiStateFlow.value = FindQueenUiState(
            findingResult = findingResult,
            gameState = gameState
        )

        composeTestRule.setContent {
            FindQueenTheme {
                ResultScreen(
                    navController = rememberNavController(),
                    viewModel = mockViewModel
                )
            }
        }

        composeTestRule
            .onNodeWithText("Mission Summary:")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("1. Donlon - Space pod (Time: 50)")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("2. Enchai - Space rocket (Time: 50)")
            .assertIsDisplayed()
    }

    @Test
    fun resultScreen_startNewGameButton_callsResetAndNavigates() {
        val findingResult = FindingResult("Donlon", "success")
        uiStateFlow.value = FindQueenUiState(findingResult = findingResult)

        composeTestRule.setContent {
            FindQueenTheme {
                ResultScreen(
                    navController = rememberNavController(),
                    viewModel = mockViewModel
                )
            }
        }

        composeTestRule
            .onNodeWithText("Start New Game")
            .performClick()

        verify { mockViewModel.resetGame() }
    }

    @Test
    fun resultScreen_showsNetworkError() {
        val errorMessage = "Network connection failed"
        uiStateFlow.value = FindQueenUiState(error = errorMessage)

        composeTestRule.setContent {
            FindQueenTheme {
                ResultScreen(
                    navController = rememberNavController(),
                    viewModel = mockViewModel
                )
            }
        }

        composeTestRule
            .onNodeWithText("Error!")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(errorMessage)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Go Back")
            .performClick()

        verify { mockViewModel.clearError() }
    }

    @Test
    fun resultScreen_displaysSuccessIcon() {
        val findingResult = FindingResult("Donlon", "success")
        uiStateFlow.value = FindQueenUiState(findingResult = findingResult)

        composeTestRule.setContent {
            FindQueenTheme {
                ResultScreen(
                    navController = rememberNavController(),
                    viewModel = mockViewModel
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Success")
            .assertIsDisplayed()
    }

    @Test
    fun resultScreen_displaysFailureIcon() {
        val findingResult = FindingResult(null, "false")
        uiStateFlow.value = FindQueenUiState(findingResult = findingResult)

        composeTestRule.setContent {
            FindQueenTheme {
                ResultScreen(
                    navController = rememberNavController(),
                    viewModel = mockViewModel
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Not found")
            .assertIsDisplayed()
    }
}
