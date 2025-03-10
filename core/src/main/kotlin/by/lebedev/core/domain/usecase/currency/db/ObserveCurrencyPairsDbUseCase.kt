package by.lebedev.core.domain.usecase.currency.db

import by.lebedev.core.data.datasource.local.db.entity.CurrencyPairEntity
import by.lebedev.core.domain.repository.currency.DbCurrencyRepository
import by.lebedev.core.domain.usecase.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class ObserveCurrencyPairsDbUseCase @Inject constructor(
    private val dbCurrencyRepository: DbCurrencyRepository
) : BaseUseCase<Unit, Flow<MutableList<CurrencyPairEntity>>>() {
    override suspend fun execute(params: Unit): Flow<MutableList<CurrencyPairEntity>> {
        return dbCurrencyRepository.observeCurrencyPairsDb()
    }
}