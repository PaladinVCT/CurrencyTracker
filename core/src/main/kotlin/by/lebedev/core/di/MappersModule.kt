package by.lebedev.core.di

import by.lebedev.core.data.model.mapper.CurrenciesResponseMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object MappersModule {

    @Provides
    @Singleton
    fun provideCurrencyResponseMapper(): CurrenciesResponseMapper {
        return CurrenciesResponseMapper()
    }
}