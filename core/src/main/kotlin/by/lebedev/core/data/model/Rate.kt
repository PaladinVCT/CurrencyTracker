package by.lebedev.core.data.model

data class Rate(
    val secondaryCurrency: String,
    val price: Double,
    val isFavourite: Boolean = false
)
