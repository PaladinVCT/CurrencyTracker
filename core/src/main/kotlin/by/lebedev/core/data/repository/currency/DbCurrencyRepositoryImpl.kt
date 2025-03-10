package by.lebedev.core.data.repository.currency

import by.lebedev.core.data.datasource.local.db.AppDatabase
import by.lebedev.core.data.datasource.local.db.entity.CurrencyPairEntity
import by.lebedev.core.domain.repository.currency.DbCurrencyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DbCurrencyRepositoryImpl @Inject constructor(
    private val db: AppDatabase
) : DbCurrencyRepository {

    override fun observeCurrencyPairsDb(): Flow<MutableList<CurrencyPairEntity>> {
        return db.currencyDao().observeCurrencyPair()
    }

    override suspend fun insertCurrencyPairDb(currencyPair: CurrencyPairEntity): Long {
        return db.currencyDao().insertCurrencyPair(currencyPair)
    }

    override suspend fun deleteCurrencyPairDb(currencyPair: CurrencyPairEntity): Int {
        return db.currencyDao()
            .deleteCurrencyPair(currencyPair.baseCurrency.orEmpty(), currencyPair.secondaryCurrency.orEmpty())
    }
}