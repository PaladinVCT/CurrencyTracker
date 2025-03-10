package by.lebedev.core.data.model


import com.google.gson.annotations.SerializedName

data class CurrenciesResponse(
    @SerializedName("base")
    val base: String? = null,
    @SerializedName("rates")
    val rates: Map<String, Double>? = null
)