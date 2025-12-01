package com.example.presentation.utils.nav

sealed class ScreenDestinations(val route: String) {

    data object Home : ScreenDestinations("home")

    data object Diagnosis : ScreenDestinations("diagnosis")

    data object Report : ScreenDestinations("report")

    data object Hospital : ScreenDestinations("hospital/{hospitalId}") {
        fun createRoute(hospitalId: Long) = "hospital/$hospitalId"
    }

    data object Search : ScreenDestinations("search/{hospitalId}") {
        fun createRoute(hospitalId: Long) = "search/$hospitalId"
    }
}