package com.socialite.solite_pos.view.screens.store.outcomes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.domain.domain.GetOutcomes
import com.socialite.domain.domain.IsUserStaff
import com.socialite.domain.domain.NewOutcome
import com.socialite.domain.schema.Outcome
import com.socialite.solite_pos.utils.tools.helper.ReportParameter
import com.socialite.solite_pos.utils.tools.mapper.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OutcomesViewModel @Inject constructor(
    private val newOutcome: NewOutcome,
    private val getOutcomes: GetOutcomes,
    private val isUserStaff: IsUserStaff
) : ViewModel() {

    private val _viewState = MutableStateFlow(OutcomesViewState.idle())
    val viewState = _viewState.asStateFlow()

    init {
        _viewState.onEach {
            getOutcomes(it.parameters.toDomain())
                .onEach { outcomes ->
                    _viewState.emit(
                        _viewState.value.copy(
                            outcomes = outcomes
                        )
                    )
                }.launchIn(viewModelScope)
        }.launchIn(viewModelScope)
    }

    fun loadUserAuthority() = viewModelScope.launch {
        _viewState.emitAll(
            isUserStaff().map {
                _viewState.value.copy(
                    isUserStaff = it
                )
            }
        )
    }

    fun addNewOutcome(outcome: Outcome) {
        viewModelScope.launch {
            newOutcome(outcome)
        }
    }

    fun setNewParameters(parameters: ReportParameter) {
        viewModelScope.launch {
            _viewState.emit(
                _viewState.value.copy(
                    parameters = parameters
                )
            )
        }
    }

    fun setDatesParameters(date: String) {
        viewModelScope.launch {
            val currentParameters = _viewState.value.parameters
            _viewState.emit(
                _viewState.value.copy(
                    parameters = currentParameters.copy(
                        start = date,
                        end = date
                    )
                )
            )
        }
    }
}
