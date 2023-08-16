package com.socialite.solite_pos.view.screens.store.outcomes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentManager
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.socialite.solite_pos.R
import com.socialite.solite_pos.compose.BasicAddButton
import com.socialite.solite_pos.compose.BasicEmptyList
import com.socialite.solite_pos.compose.BasicTopBar
import com.socialite.solite_pos.compose.SpaceForFloatingButton
import com.socialite.data.schema.room.new_master.Outcome
import com.socialite.solite_pos.utils.config.DateUtils
import com.socialite.solite_pos.utils.config.thousand
import com.socialite.solite_pos.utils.tools.helper.ReportParameter
import com.socialite.solite_pos.view.screens.store.outcomes.components.OutcomeDetail
import kotlinx.coroutines.launch

@Composable
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
fun OutComesScreen(
    currentViewModel: OutcomesViewModel = hiltViewModel(),
    timePicker: MaterialTimePicker.Builder,
    datePicker: MaterialDatePicker.Builder<Long>,
    fragmentManager: FragmentManager,
    parameters: ReportParameter,
    onBackClicked: () -> Unit
) {
    LaunchedEffect(key1 = parameters) {
        currentViewModel.setNewParameters(parameters)
    }
    val modalState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val state = currentViewModel.viewState.collectAsState().value
    var selectedOutcome by remember { mutableStateOf<Outcome?>(null) }

    if (modalState.currentValue == ModalBottomSheetValue.Hidden) {
        LocalSoftwareKeyboardController.current?.hide()
    }

    fun onAddClicked() {
        scope.launch {
            selectedOutcome = null
            modalState.show()
        }
    }

    fun onDateClicked() {
        val picker =
            datePicker.setSelection(DateUtils.strToDate(state.parameters.start).time)
                .build()
        picker.addOnPositiveButtonClickListener {
            val newDate = DateUtils.millisToDate(
                millis = it,
                isWithTime = true
            )
            currentViewModel.setDatesParameters(newDate)
            picker.dismiss()
        }
        picker.show(fragmentManager, "")
    }

    if (state.parameters.isTodayOnly()) {
        ModalBottomSheetLayout(
            sheetState = modalState,
            sheetShape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp
            ),
            sheetContent = {
                OutcomeDetail(
                    date = state.parameters.start,
                    isTodayDate = DateUtils.isDateTimeIsToday(state.parameters.start),
                    timePicker = timePicker,
                    datePicker = datePicker,
                    outcome = selectedOutcome,
                    fragmentManager = fragmentManager,
                    onSubmitOutcome = {
                        scope.launch {
                            currentViewModel.addNewOutcome(it)
                            modalState.hide()
                        }
                    }
                )
            },
            content = {
                OutcomesContent(
                    outcomes = state.outcomes,
                    parameters = state.parameters,
                    onAddClicked = {
                        onAddClicked()
                    },
                    onDateClicked = {
                        onDateClicked()
                    },
                    onBackClicked = onBackClicked,
                    onItemClicked = {
                        scope.launch {
                            selectedOutcome = it
                            modalState.show()
                        }
                    }
                )
            }
        )
    } else {
        OutcomesContent(
            outcomes = state.outcomes,
            parameters = state.parameters,
            onBackClicked = onBackClicked
        )
    }
}

@Composable
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
private fun OutcomesContent(
    outcomes: List<Outcome>,
    parameters: ReportParameter,
    onAddClicked: (() -> Unit)? = null,
    onDateClicked: (() -> Unit)? = null,
    onBackClicked: () -> Unit,
    onItemClicked: ((Outcome) -> Unit)? = null
) {
    Scaffold(
        topBar = {
            BasicTopBar(
                titleText = stringResource(id = R.string.outcome),
                onBackClicked = onBackClicked
            )
        },
        content = { padding ->
            Column {
                OutcomeHeader(
                    parameters = parameters,
                    onDateClicked = onDateClicked
                )
                Outcomes(
                    modifier = Modifier
                        .padding(padding),
                    outcomes = outcomes,
                    isTodayOnly = parameters.isTodayOnly(),
                    onAddClicked = onAddClicked,
                    onItemClicked = onItemClicked
                )
            }
        }
    )
}

@Composable
private fun Outcomes(
    modifier: Modifier = Modifier,
    outcomes: List<Outcome>,
    isTodayOnly: Boolean,
    onAddClicked: (() -> Unit)? = null,
    onItemClicked: ((Outcome) -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        val listState = rememberLazyListState()

        if (outcomes.isNotEmpty()) {

            LazyColumn(
                modifier = modifier
                    .align(Alignment.TopCenter),
                state = listState
            ) {

                items(outcomes) {
                    OutcomeItem(
                        outcome = it,
                        isTodayOnly = isTodayOnly,
                        onItemClicked = onItemClicked
                    )
                }

                item { SpaceForFloatingButton() }
            }
        } else {
            BasicEmptyList(imageId = R.drawable.ic_no_data, text = R.string.no_data_found)
        }

        onAddClicked?.let {
            AnimatedVisibility(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp),
                visible = !listState.isScrollInProgress,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                BasicAddButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd),
                    onAddClicked = it
                )
            }
        }
    }
}

@Composable
private fun OutcomeHeader(
    parameters: ReportParameter,
    onDateClicked: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.surface)
            .run {
                return@run if (onDateClicked != null) {
                    clickable {
                        onDateClicked()
                    }
                } else {
                    this
                }
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(
                    if (parameters.isTodayOnly()) R.string.date else R.string.from
                ),
                style = MaterialTheme.typography.body1.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            if (!parameters.isTodayOnly()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.until),
                    style = MaterialTheme.typography.body1.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = DateUtils.convertDateFromDate(
                    parameters.start,
                    DateUtils.DATE_WITH_DAY_FORMAT
                ),
                style = MaterialTheme.typography.body1
            )
            if (!parameters.isTodayOnly()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = DateUtils.convertDateFromDate(
                        parameters.end,
                        DateUtils.DATE_WITH_DAY_FORMAT
                    ),
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun OutcomeItem(
    outcome: Outcome,
    isTodayOnly: Boolean,
    onItemClicked: ((Outcome) -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.surface)
            .run {
                return@run if (onItemClicked != null) {
                    clickable {
                        onItemClicked(outcome)
                    }
                } else {
                    this
                }
            }
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
        ) {
            if (!isTodayOnly) {
                Text(
                    text = DateUtils.convertDateFromDb(
                        date = outcome.date,
                        DateUtils.DATE_WITH_DAY_WITHOUT_YEAR_FORMAT
                    ),
                    style = MaterialTheme.typography.body2.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                text = DateUtils.convertDateFromDb(
                    date = outcome.date,
                    DateUtils.HOUR_AND_TIME_FORMAT
                ),
                style = MaterialTheme.typography.body2
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = outcome.name,
                style = MaterialTheme.typography.body2.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = outcome.desc,
                style = MaterialTheme.typography.body2
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            text = "Rp. ${outcome.total.thousand()}",
            style = MaterialTheme.typography.body1.copy(
                fontWeight = FontWeight.Bold
            )
        )
    }
    Spacer(modifier = Modifier.height(4.dp))
}
