package by.lebedev.features.ui.currencies.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.lebedev.core.data.UiState
import by.lebedev.core.data.model.FilterOption
import by.lebedev.core.domain.usecase.currency.GetFilterUseCase
import by.lebedev.core.domain.usecase.currency.SaveFilterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FilterViewModel @Inject constructor(
    private val getFilterUseCase: GetFilterUseCase,
    private val saveFilterUseCase: SaveFilterUseCase
) : ViewModel() {

    private val _uiStateFilter: MutableStateFlow<UiState<FilterOption>> =
        MutableStateFlow(UiState.Loading)
    val uiStateFilter: StateFlow<UiState<FilterOption>> = _uiStateFilter

    fun getAppliedFiltersCall() {
        viewModelScope.launch {
            try {
                getFilterUseCase.execute(Unit)
                    .catch {
                        _uiStateFilter.value = UiState.Error(it.message.toString())
                    }
                    .collect {
                        _uiStateFilter.value = UiState.Success(
                            it.filter ?: FilterOption.A_Z
                        )
                    }
            } catch (e: Exception) {
                _uiStateFilter.value = UiState.Error(e.message.toString())
            }
        }
    }

    fun saveAppliedFiltersCall(filter: FilterOption) {
        viewModelScope.launch {
            _uiStateFilter.value = UiState.Success(filter)
            try {
                saveFilterUseCase.execute(filter)
            } catch (e: Exception) {
                _uiStateFilter.value = UiState.Error(e.message.toString())
            }
        }
    }
}