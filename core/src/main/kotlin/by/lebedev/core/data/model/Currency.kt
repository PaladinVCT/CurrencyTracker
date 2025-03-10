package by.lebedev.core.data.model


data class Currency(
    val base: String,
    val rate: Pair<String, Double>
)