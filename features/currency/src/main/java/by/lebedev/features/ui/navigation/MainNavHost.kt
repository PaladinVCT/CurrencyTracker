package by.lebedev.features.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import by.lebedev.features.ui.favourites.FavouritesScreen
import by.lebedev.features.ui.currencies.CurrenciesScreen
import by.lebedev.features.ui.currencies.filter.FilterScreen
import by.lebedev.features.ui.navigation.model.BottomBarScreen
import by.lebedev.features.ui.navigation.model.CurrenciesScreenRoutes


@Composable
fun MainNavHost(
    navController: NavHostController,
    innerPadding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Currencies.route,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(BottomBarScreen.Currencies.route) {
            CurrenciesScreen(
                navController = navController
            )
        }
        composable(BottomBarScreen.Favourite.route) {
            FavouritesScreen()
        }
        composable(
            route = CurrenciesScreenRoutes.Filter.route
        ) {
            FilterScreen (
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
    }
}