package by.lebedev.core.di.usecase

import by.lebedev.core.domain.repository.currency.CurrencyRepository
import by.lebedev.core.domain.repository.currency.DbCurrencyRepository
import by.lebedev.core.domain.usecase.currency.GetCurrenciesUseCase
import by.lebedev.core.domain.usecase.currency.db.DeleteCurrencyPairDbUseCase
import by.lebedev.core.domain.usecase.currency.db.InsertCurrencyPairDbUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
object CurrenciesUseCaseModule {

    @Provides
    fun provideGetCurrenciesUseCase(currencyRepository: CurrencyRepository): GetCurrenciesUseCase {
        return GetCurrenciesUseCase(currencyRepository)
    }

    @Provides
    fun provideInsertCurrencyPairDbUseCase(dbCurrencyRepository: DbCurrencyRepository): InsertCurrencyPairDbUseCase {
        return InsertCurrencyPairDbUseCase(dbCurrencyRepository)
    }

    @Provides
    fun provideDeleteCurrencyPairDbUseCase(dbCurrencyRepository: DbCurrencyRepository): DeleteCurrencyPairDbUseCase {
        return DeleteCurrencyPairDbUseCase(dbCurrencyRepository)
    }
}