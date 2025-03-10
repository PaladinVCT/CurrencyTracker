package by.lebedev.features.ui.navigation.model

import by.lebedev.core.R



sealed class BottomBarScreen(val route: String) {
    object Currencies : BottomBar(
        route = "currencies",
        titleResId = R.string.currencies,
        icon = R.drawable.ic_currencies,
        iconFocused = R.drawable.ic_currencies
    )

    object Favourite : BottomBar(
        route = "favourites",
        titleResId = R.string.favourites,
        icon = R.drawable.ic_favorites,
        iconFocused = R.drawable.ic_favorites
    )
}