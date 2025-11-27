package com.example.petbulance.common.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.presentation.screen.home.homeDestination
import com.example.presentation.utils.nav.ScreenDestinations

@Composable
fun AppNavGraph(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ScreenDestinations.Home.route,
        modifier = modifier
    ) {
        homeDestination(navController = navController)
    }
}