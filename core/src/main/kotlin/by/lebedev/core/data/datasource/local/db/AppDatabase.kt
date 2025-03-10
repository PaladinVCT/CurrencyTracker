package by.lebedev.core.data.datasource.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import by.lebedev.core.data.datasource.local.db.dao.CurrencyDao
import by.lebedev.core.data.datasource.local.db.entity.CurrencyPairEntity



@Database(
    entities = [CurrencyPairEntity::class],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
}