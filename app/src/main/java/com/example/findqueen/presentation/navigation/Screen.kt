package com.example.findqueen.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object SelectionStep : Screen("selection/{step}") {
        fun createRoute(step: Int) = "selection/$step"
    }
    object Result : Screen("result")
}
