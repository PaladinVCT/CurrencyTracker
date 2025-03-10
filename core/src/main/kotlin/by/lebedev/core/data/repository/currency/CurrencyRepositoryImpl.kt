package by.lebedev.core.data.repository.currency

import by.lebedev.core.data.datasource.remote.ApiService
import by.lebedev.core.data.model.AppliedFilter
import by.lebedev.core.data.model.BaseCurrency
import by.lebedev.core.data.model.Currencies
import by.lebedev.core.data.model.FilterOption
import by.lebedev.core.data.model.mapper.CurrenciesResponseMapper
import by.lebedev.core.domain.repository.currency.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CurrencyRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val currenciesResponseMapper: CurrenciesResponseMapper
) : CurrencyRepository {

    override suspend fun getCurrencies(baseCurrency: BaseCurrency): Flow<Currencies> {
        return flow {
            emit(currenciesResponseMapper.map(apiService.getCurrencies(baseCurrency.name)))
        }
    }

    override suspend fun getCurrenciesFilter(): Flow<AppliedFilter> {
        return flow {
            emit(AppliedFilter)
        }
    }

    override suspend fun saveCurrenciesFilter(filterOption: FilterOption) {
        AppliedFilter.filter = filterOption
    }
}