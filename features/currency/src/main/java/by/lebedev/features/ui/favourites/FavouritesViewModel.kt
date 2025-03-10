package by.lebedev.features.ui.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.lebedev.core.data.UiState
import by.lebedev.core.data.datasource.local.db.entity.CurrencyPairEntity
import by.lebedev.core.domain.usecase.currency.db.DeleteCurrencyPairDbUseCase
import by.lebedev.core.domain.usecase.currency.db.ObserveCurrencyPairsDbUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val deleteCurrencyPairDbUseCase: DeleteCurrencyPairDbUseCase,
    private val observeCurrencyPairsDbUseCase: ObserveCurrencyPairsDbUseCase,
) : ViewModel() {

    private val _uiStateFavourites: MutableStateFlow<UiState<List<CurrencyPairEntity>>> =
        MutableStateFlow(UiState.Loading)
    val uiStateFavourites: StateFlow<UiState<List<CurrencyPairEntity>>> = _uiStateFavourites

    fun observeCurrencyPairsDbCall() {
        viewModelScope.launch {
            try {
                observeCurrencyPairsDbUseCase.execute(Unit).catch {
                    it.message
                    _uiStateFavourites.value = UiState.Error(it.message.orEmpty())
                }.collect {
                    _uiStateFavourites.value = UiState.Success(it)
                }
            } catch (e: Exception) {
                _uiStateFavourites.value = UiState.Error(e.message.orEmpty())
            }
        }
    }


    fun onDeleteFavouriteClick(pair: CurrencyPairEntity) {
        viewModelScope.launch {
            try {
                deleteCurrencyPairDbUseCase.execute(
                    CurrencyPairEntity(
                        baseCurrency = pair.baseCurrency,
                        secondaryCurrency = pair.secondaryCurrency,
                        price = pair.price
                    )
                )
            } catch (e: Exception) {
                _uiStateFavourites.value = UiState.Error(e.message.orEmpty())
            }
        }
    }
}