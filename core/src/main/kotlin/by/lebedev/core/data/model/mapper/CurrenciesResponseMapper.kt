package by.lebedev.core.data.model.mapper

import by.lebedev.core.data.model.Currencies
import by.lebedev.core.data.model.CurrenciesResponse
import by.lebedev.core.data.model.Rate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrenciesResponseMapper @Inject constructor() :
    Mapper<CurrenciesResponse, Currencies> {
    override fun map(from: CurrenciesResponse): Currencies {
        return Currencies(
            base = from.base.orEmpty(),
            rates = from.rates?.map {
                Rate(
                    it.key,
                    it.value
                )
            }.orEmpty()
        )
    }
}