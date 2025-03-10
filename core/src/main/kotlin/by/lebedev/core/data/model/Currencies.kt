package by.lebedev.core.data.model


data class Currencies(
    val base: String,
    val rates: List<Rate>
)