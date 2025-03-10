package by.lebedev.core.domain.usecase.currency

import by.lebedev.core.data.model.BaseCurrency
import by.lebedev.core.data.model.Currencies
import by.lebedev.core.domain.repository.currency.CurrencyRepository
import by.lebedev.core.domain.usecase.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrenciesUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : BaseUseCase<BaseCurrency, Flow<Currencies>>() {
    override suspend fun execute(params: BaseCurrency): Flow<Currencies> {
        return currencyRepository.getCurrencies(params)
    }
}