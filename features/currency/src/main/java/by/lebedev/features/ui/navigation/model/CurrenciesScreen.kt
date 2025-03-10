package by.lebedev.features.ui.navigation.model


sealed class CurrenciesScreenRoutes(val route: String) {

    object Filter : CurrenciesScreenRoutes(route = "home/filter")
}