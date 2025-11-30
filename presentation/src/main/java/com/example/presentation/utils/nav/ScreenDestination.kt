package com.example.presentation.utils.nav

sealed class ScreenDestinations(val route: String) {

    data object Home : ScreenDestinations("home")

    data object Diagnosis : ScreenDestinations("diagnosis")

    data object Report : ScreenDestinations("report")

    data object Hospital : ScreenDestinations("Hospital")

    data object Progress : ScreenDestinations("Progress")
}