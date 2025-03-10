package by.lebedev.core.domain.repository.currency

import by.lebedev.core.data.datasource.local.db.entity.CurrencyPairEntity
import kotlinx.coroutines.flow.Flow



interface DbCurrencyRepository {
    fun observeCurrencyPairsDb(): Flow<MutableList<CurrencyPairEntity>>
    suspend fun insertCurrencyPairDb(currencyPair: CurrencyPairEntity): Long
    suspend fun deleteCurrencyPairDb(currencyPair: CurrencyPairEntity): Int
}