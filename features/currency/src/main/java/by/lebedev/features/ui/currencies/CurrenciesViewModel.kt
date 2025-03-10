package by.lebedev.features.ui.currencies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.lebedev.core.data.UiState
import by.lebedev.core.data.datasource.local.db.entity.CurrencyPairEntity
import by.lebedev.core.data.model.AppliedFilter
import by.lebedev.core.data.model.BaseCurrency
import by.lebedev.core.data.model.Currencies
import by.lebedev.core.data.model.Rate
import by.lebedev.core.domain.usecase.currency.GetCurrenciesUseCase
import by.lebedev.core.domain.usecase.currency.GetFilterUseCase
import by.lebedev.core.domain.usecase.currency.db.DeleteCurrencyPairDbUseCase
import by.lebedev.core.domain.usecase.currency.db.InsertCurrencyPairDbUseCase
import by.lebedev.core.domain.usecase.currency.db.ObserveCurrencyPairsDbUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CurrenciesViewModel @Inject constructor(
    private val getCurrenciesUseCase: GetCurrenciesUseCase,
    private val insertCurrencyPairDbUseCase: InsertCurrencyPairDbUseCase,
    private val deleteCurrencyPairDbUseCase: DeleteCurrencyPairDbUseCase,
    private val observeCurrencyPairsDbUseCase: ObserveCurrencyPairsDbUseCase,
    private val getFilterUseCase: GetFilterUseCase,
) : ViewModel() {

    init {
        observeCurrencyPairsDbCall()
    }

    private val _uiStateCurrencies: MutableStateFlow<UiState<Currencies>> =
        MutableStateFlow(UiState.Loading)
    val uiStateCurrencies: StateFlow<UiState<Currencies>> = _uiStateCurrencies

    private val _baseCurrency: MutableStateFlow<BaseCurrency> = MutableStateFlow(BaseCurrency.USD)
    val baseCurrency: StateFlow<BaseCurrency> = _baseCurrency

    private val _appliedFilter: MutableStateFlow<AppliedFilter?> = MutableStateFlow(null)
    val appliedFilter: StateFlow<AppliedFilter?> = _appliedFilter

    private val _favourites: MutableStateFlow<List<CurrencyPairEntity>> = MutableStateFlow(listOf())
    val favourites: StateFlow<List<CurrencyPairEntity>> = _favourites

    fun getCurrenciesApiCall(baseCurrency: BaseCurrency) {
        viewModelScope.launch {
            try {
                getCurrenciesUseCase.execute(baseCurrency).catch {
                    _uiStateCurrencies.value = UiState.Error(it.message.toString())
                }.collect { currencies ->
                    _uiStateCurrencies.value = UiState.Success(currencies)
                }
            } catch (e: Exception) {
                _uiStateCurrencies.value = UiState.Error(e.message.toString())
            }
        }
    }

    fun getAppliedFiltersCall() {
        viewModelScope.launch {
            try {
                getFilterUseCase.execute(Unit).catch {
                    _appliedFilter.value = null
                }.collect {
                    _appliedFilter.value = it
                }
            } catch (e: Exception) {
                _appliedFilter.value = null
            }
        }
    }

    private fun observeCurrencyPairsDbCall() {
        viewModelScope.launch {
            try {
                observeCurrencyPairsDbUseCase.execute(Unit).catch {
                    it.message
                    _favourites.value = emptyList()
                }.collect {
                    if (uiStateCurrencies.value is UiState.Success) {
                        val currencies =
                            (uiStateCurrencies.value as UiState.Success<Currencies>).data
                        _uiStateCurrencies.value = UiState.Success(Currencies(
                            base = currencies.base,
                            rates = currencies.rates.map {
                                Rate(
                                    it.secondaryCurrency,
                                    it.price,
                                    isInFavourites(it)
                                )
                            }
                        ))
                    }
                    _favourites.value = it
                }
            } catch (e: Exception) {
                _favourites.value = emptyList()
            }
        }
    }

    fun setBaseCurrency(baseCurrency: BaseCurrency) {
        if (_baseCurrency.value != baseCurrency) {
            _baseCurrency.value = baseCurrency
            _uiStateCurrencies.value = UiState.Loading
        }
    }

    fun onFavouriteClick(rate: Rate) {
        viewModelScope.launch {
            try {
                if (isInFavourites(rate)) {
                    deleteCurrencyPairDbUseCase.execute(
                        CurrencyPairEntity(
                            baseCurrency = baseCurrency.value.name,
                            secondaryCurrency = rate.secondaryCurrency,
                            price = rate.price
                        )
                    )
                } else {
                    insertCurrencyPairDbUseCase.execute(
                        CurrencyPairEntity(
                            baseCurrency = baseCurrency.value.name,
                            secondaryCurrency = rate.secondaryCurrency,
                            price = rate.price
                        )
                    )
                }
            } catch (e: Exception) {

            }
        }
    }

    fun isInFavourites(rate: Rate): Boolean {
        return favourites.value.any { entity ->
            entity.baseCurrency == baseCurrency.value.name && entity.secondaryCurrency == rate.secondaryCurrency
        }
    }
}
