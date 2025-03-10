package by.lebedev.currencytracker.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import by.lebedev.core.ui.theme.CurrencyTrackerTheme
import by.lebedev.features.ui.navigation.BottomNav
import by.lebedev.features.ui.navigation.model.BottomBarScreen


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val navigationItemContentList = listOf(
        BottomBarScreen.Currencies,
        BottomBarScreen.Favourite
    )

    BottomNav(
        modifier = modifier,
        navigationItemContentList = navigationItemContentList,
        navController = navController,
        currentDestination = currentDestination,
    )
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun DefaultPreview() {
    CurrencyTrackerTheme { }
}