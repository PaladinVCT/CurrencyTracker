package by.lebedev.core.domain.usecase.currency.db

import by.lebedev.core.data.datasource.local.db.entity.CurrencyPairEntity
import by.lebedev.core.domain.repository.currency.DbCurrencyRepository
import by.lebedev.core.domain.usecase.BaseUseCaseSuspend
import javax.inject.Inject



class DeleteCurrencyPairDbUseCase @Inject constructor(
    private val dbCurrencyRepository: DbCurrencyRepository
) : BaseUseCaseSuspend<CurrencyPairEntity, Int>() {
    override suspend fun execute(params: CurrencyPairEntity): Int {
        return dbCurrencyRepository.deleteCurrencyPairDb(params)
    }
}