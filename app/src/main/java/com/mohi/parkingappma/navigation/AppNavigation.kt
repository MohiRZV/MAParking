package com.mohi.parkingappma.navigation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mohi.parkingappma.components.AddEntityScreen
import com.mohi.parkingappma.components.DisplayAllScreen
import com.mohi.parkingappma.components.DisplayAvailableScreen
import com.mohi.parkingappma.components.DisplayStatsScreen
import com.mohi.parkingappma.model.viewmodel.EntitiesViewModel

@ExperimentalMaterialApi
@Composable
fun AppNavigation(
    viewModel: EntitiesViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Screen1.route) {
        composable(
            route = Screen.Screen1.route
        ) {
            DisplayAllScreen(
                onClick = {
                    navController.navigate(Screen.Screen2.route)
                },
                onSwitchToUserView = {
                    navController.navigate(Screen.Screen3.route)
                },
                onSwitchToStatsView = {
                    navController.navigate(Screen.Screen4.route)
                }
            )
        }
        composable(
            route = Screen.Screen2.route
        ) {
            AddEntityScreen(
                onClick =  { number: String, address: String, status: String, count: String ->
                    viewModel.add(number, address, status, count)
                    navController.navigate(Screen.Screen1.route)
                }
            )
        }
        composable(
            route = Screen.Screen3.route
        ) {
            DisplayAvailableScreen(
                onTakeClick = {
                    viewModel.take(it)
                }
            )
        }
        composable(
            route = Screen.Screen4.route
        ) {
            DisplayStatsScreen(
            )
        }
    }
}