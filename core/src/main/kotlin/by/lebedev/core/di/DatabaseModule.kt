package by.lebedev.core.di

import android.content.Context
import androidx.room.Room
import by.lebedev.core.data.datasource.local.db.AppDatabase
import by.lebedev.core.util.UtilConstants.DB_CURRENCIES_FAVOURITE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, DB_CURRENCIES_FAVOURITE).build()
    }
}