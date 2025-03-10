package by.lebedev.core.di

import by.lebedev.core.data.datasource.local.db.AppDatabase
import by.lebedev.core.data.datasource.remote.ApiService
import by.lebedev.core.data.model.mapper.CurrenciesResponseMapper
import by.lebedev.core.data.repository.currency.CurrencyRepositoryImpl
import by.lebedev.core.data.repository.currency.DbCurrencyRepositoryImpl
import by.lebedev.core.domain.repository.currency.CurrencyRepository
import by.lebedev.core.domain.repository.currency.DbCurrencyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideCurrencyRepository(
        apiService: ApiService,
        mapper: CurrenciesResponseMapper
    ): CurrencyRepository {
        return CurrencyRepositoryImpl(apiService, mapper)
    }

    @Provides
    @Singleton
    fun provideDbCurrencyRepository(db: AppDatabase): DbCurrencyRepository {
        return DbCurrencyRepositoryImpl(db)
    }
}