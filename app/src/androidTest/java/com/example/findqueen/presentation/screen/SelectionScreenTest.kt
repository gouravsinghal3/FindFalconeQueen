package com.example.findqueen.presentation.screen

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.findqueen.domain.model.GameState
import com.example.findqueen.domain.model.Planet
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


class SelectionScreenTest {

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
    fun selectionScreen_displaysTitle() {
        composeTestRule.setContent {
            FindQueenTheme {
                SelectionScreen(
                    navController = rememberNavController(),
                    viewModel = mockViewModel,
                    step = 0
                )
            }
        }

        composeTestRule
            .onNodeWithText("Destination 1 of 4")
            .assertIsDisplayed()
    }

    @Test
    fun selectionScreen_displaysPlanets() {
        val planets = listOf(
            Planet("Donlon", 100),
            Planet("Enchai", 200),
            Planet("Jebing", 300)
        )
        uiStateFlow.value = FindQueenUiState(planets = planets)

        composeTestRule.setContent {
            FindQueenTheme {
                SelectionScreen(
                    navController = rememberNavController(),
                    viewModel = mockViewModel,
                    step = 0
                )
            }
        }

        composeTestRule
            .onNodeWithText("Select Planet:")
            .assertIsDisplayed()

        planets.forEach { planet ->
            composeTestRule
                .onNodeWithText(planet.name)
                .assertIsDisplayed()

            composeTestRule
                .onNodeWithText("Distance: ${planet.distance} megamiles")
                .assertIsDisplayed()
        }
    }

    @Test
    fun selectionScreen_displaysVehicles_afterPlanetSelection() {
        val planets = listOf(Planet("Donlon", 100))
        val vehicles = listOf(
            Vehicle("Space pod", 2, 200, 2),
            Vehicle("Space rocket", 1, 300, 4)
        )
        val gameState = GameState(availableVehicles = mapOf(
            "Space pod" to 2,
            "Space rocket" to 1
        ))

        uiStateFlow.value = FindQueenUiState(
            planets = planets,
            vehicles = vehicles,
            gameState = gameState
        )

        composeTestRule.setContent {
            FindQueenTheme {
                SelectionScreen(
                    navController = rememberNavController(),
                    viewModel = mockViewModel,
                    step = 0
                )
            }
        }

        // Select planet first
        composeTestRule
            .onNodeWithText("Donlon")
            .performClick()

        // Check vehicles are displayed
        composeTestRule
            .onNodeWithText("Select Vehicle:")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Space pod")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Available: 2")
            .assertIsDisplayed()
    }

    @Test
    fun selectionScreen_showsConfirmButton_afterBothSelections() {
        val planets = listOf(Planet("Donlon", 100))
        val vehicles = listOf(Vehicle("Space pod", 2, 200, 2))
        val gameState = GameState(availableVehicles = mapOf("Space pod" to 2))

        uiStateFlow.value = FindQueenUiState(
            planets = planets,
            vehicles = vehicles,
            gameState = gameState
        )

        composeTestRule.setContent {
            FindQueenTheme {
                SelectionScreen(
                    navController = rememberNavController(),
                    viewModel = mockViewModel,
                    step = 0
                )
            }
        }

        // Select planet
        composeTestRule
            .onNodeWithText("Donlon")
            .performClick()

        // Select vehicle
        composeTestRule
            .onNodeWithText("Space pod")
            .performClick()

        // Check confirm button appears
        composeTestRule
            .onNodeWithText("Confirm Selection")
            .assertIsDisplayed()
            .assertIsEnabled()
    }

    @Test
    fun selectionScreen_showsError() {
        val errorMessage = "Vehicle not available"
        uiStateFlow.value = FindQueenUiState(error = errorMessage)

        composeTestRule.setContent {
            FindQueenTheme {
                SelectionScreen(
                    navController = rememberNavController(),
                    viewModel = mockViewModel,
                    step = 0
                )
            }
        }

        composeTestRule
            .onNodeWithText("Error")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(errorMessage)
            .assertIsDisplayed()
    }

    @Test
    fun selectionScreen_vehicleOutOfRange_showsWarning() {
        val planets = listOf(Planet("FarPlanet", 500))
        val vehicles = listOf(Vehicle("Short Range", 1, 200, 2))
        val gameState = GameState(availableVehicles = mapOf("Short Range" to 1))

        uiStateFlow.value = FindQueenUiState(
            planets = planets,
            vehicles = vehicles,
            gameState = gameState
        )

        composeTestRule.setContent {
            FindQueenTheme {
                SelectionScreen(
                    navController = rememberNavController(),
                    viewModel = mockViewModel,
                    step = 0
                )
            }
        }

        // Select planet
        composeTestRule
            .onNodeWithText("FarPlanet")
            .performClick()

        // Check vehicle shows "Cannot reach this planet"
        composeTestRule
            .onNodeWithText("Cannot reach this planet")
            .assertIsDisplayed()
    }

    @Test
    fun selectionScreen_showsVehicleDetails() {
        val planets = listOf(Planet("Donlon", 100))
        val vehicles = listOf(Vehicle("Space pod", 2, 200, 2))
        val gameState = GameState(availableVehicles = mapOf("Space pod" to 2))

        uiStateFlow.value = FindQueenUiState(
            planets = planets,
            vehicles = vehicles,
            gameState = gameState
        )

        composeTestRule.setContent {
            FindQueenTheme {
                SelectionScreen(
                    navController = rememberNavController(),
                    viewModel = mockViewModel,
                    step = 0
                )
            }
        }

        // Select planet first
        composeTestRule
            .onNodeWithText("Donlon")
            .performClick()

        // Check vehicle details
        composeTestRule
            .onNodeWithText("Max Distance: 200 | Speed: 2")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Time: 50")
            .assertIsDisplayed()
    }

}
