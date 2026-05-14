package com.amirgoyri.musicapp.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.amirgoyri.musicapp.ui.screens.DetailScreen
import com.amirgoyri.musicapp.ui.screens.HomeScreen
import com.amirgoyri.musicapp.viewmodel.DetailViewModel
import com.amirgoyri.musicapp.viewmodel.HomeViewModel
import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

@Serializable
data class DetailRoute(val albumId: Int)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val homeViewModel: HomeViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                viewModel = homeViewModel,
                onAlbumClick = { albumId ->
                    navController.navigate("detail/$albumId")
                }
            )
        }
        composable(
            route = "detail/{albumId}",
            arguments = listOf(navArgument("albumId") { type = NavType.IntType })
        ) { backStackEntry ->
            val albumId = backStackEntry.arguments?.getInt("albumId") ?: return@composable
            val detailViewModel: DetailViewModel = viewModel()
            DetailScreen(
                albumId = albumId,
                viewModel = detailViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
