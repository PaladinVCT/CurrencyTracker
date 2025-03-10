package by.lebedev.core.data.datasource.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.lebedev.core.data.datasource.local.db.entity.CurrencyPairEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface CurrencyDao {

    @Query("SELECT * FROM table_currency")
    fun observeCurrencyPair(): Flow<MutableList<CurrencyPairEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencyPair(pair: CurrencyPairEntity): Long

    @Query("DELETE FROM table_currency WHERE base_currency=:baseCurrency AND secondary_currency=:secondaryCurrency ")
    suspend fun deleteCurrencyPair(baseCurrency: String, secondaryCurrency: String): Int
}