package by.lebedev.core.data.datasource.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "table_currency")
data class CurrencyPairEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    @ColumnInfo(name = "base_currency")
    val baseCurrency: String? = null,

    @ColumnInfo(name = "secondary_currency")
    val secondaryCurrency: String? = null,

    @ColumnInfo(name = "price")
    val price: Double? = null
)
