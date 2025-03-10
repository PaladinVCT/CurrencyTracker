package by.lebedev.core.data.datasource.remote

import by.lebedev.core.data.model.CurrenciesResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET("latest")
    suspend fun getCurrencies(
        @Query("base") baseCurrency: String
    ): CurrenciesResponse
}