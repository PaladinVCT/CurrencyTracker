package by.lebedev.core.domain.usecase.currency

import by.lebedev.core.data.model.FilterOption
import by.lebedev.core.domain.repository.currency.CurrencyRepository
import by.lebedev.core.domain.usecase.BaseUseCase
import javax.inject.Inject

class SaveFilterUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : BaseUseCase<FilterOption, Unit>() {
    override suspend fun execute(params: FilterOption) {
         currencyRepository.saveCurrenciesFilter(params)
    }
}