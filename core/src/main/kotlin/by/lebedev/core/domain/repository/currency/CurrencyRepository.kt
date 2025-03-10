package by.lebedev.core.domain.repository.currency

import by.lebedev.core.data.model.AppliedFilter
import by.lebedev.core.data.model.BaseCurrency
import by.lebedev.core.data.model.Currencies
import by.lebedev.core.data.model.FilterOption
import kotlinx.coroutines.flow.Flow


interface CurrencyRepository {
    suspend fun getCurrencies(baseCurrency: BaseCurrency): Flow<Currencies>
    suspend fun getCurrenciesFilter(): Flow<AppliedFilter>
    suspend fun saveCurrenciesFilter(filterOption: FilterOption)
}