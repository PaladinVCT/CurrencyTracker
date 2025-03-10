package by.lebedev.core.data.model.mapper

import by.lebedev.core.data.datasource.local.db.entity.CurrencyPairEntity
import by.lebedev.core.data.model.Currency
import javax.inject.Inject


class CurrencyToEntityMapper @Inject constructor() :
    Mapper<Currency, CurrencyPairEntity> {
    override fun map(from: Currency): CurrencyPairEntity {
        return CurrencyPairEntity(
            baseCurrency = from.base,
            secondaryCurrency = from.rate.first,
            price = from.rate.second
        )
    }
}