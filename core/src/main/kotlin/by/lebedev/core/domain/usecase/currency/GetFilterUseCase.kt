package by.lebedev.core.domain.usecase.currency

import by.lebedev.core.data.model.AppliedFilter
import by.lebedev.core.domain.repository.currency.CurrencyRepository
import by.lebedev.core.domain.usecase.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFilterUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : BaseUseCase<Unit, Flow<AppliedFilter>>() {
    override suspend fun execute(params: Unit): Flow<AppliedFilter> {
        return currencyRepository.getCurrenciesFilter()
    }
}