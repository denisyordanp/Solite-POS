package com.socialite.solite_pos.view.screens.store.outcomes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.solite_pos.data.source.domain.NewOutcome
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Outcome
import com.socialite.solite_pos.data.source.repository.OutcomesRepository
import com.socialite.solite_pos.utils.tools.helper.ReportParameter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OutcomesViewModel @Inject constructor(
    private val outcomesRepository: OutcomesRepository,
    private val newOutcome: NewOutcome,
) : ViewModel() {

    private val _viewState = MutableStateFlow(OutcomesViewState.idle())
    val viewState = _viewState.asStateFlow()

    init {
        _viewState.onEach {
            outcomesRepository.getOutcomes(it.parameters)
                .onEach { outcomes ->
                    _viewState.emit(
                        _viewState.value.copy(
                            outcomes = outcomes
                        )
                    )
                }.launchIn(viewModelScope)
        }.launchIn(viewModelScope)
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
