package com.example.findqueen.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.findqueen.presentation.screen.HomeScreen
import com.example.findqueen.presentation.screen.ResultScreen
import com.example.findqueen.presentation.screen.SelectionScreen
import com.example.findqueen.presentation.viewmodel.FindQueenViewModel

@Composable
fun FindQueenNavigation(
    navController: NavHostController,
    viewModel: FindQueenViewModel = hiltViewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        
        composable(
            Screen.SelectionStep.route,
            arguments = listOf(navArgument("step") { type = NavType.IntType })
        ) { backStackEntry ->
            val step = backStackEntry.arguments?.getInt("step") ?: 0
            SelectionScreen(
                navController = navController,
                viewModel = viewModel,
                step = step
            )
        }
        
        composable(Screen.Result.route) {
            ResultScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}
