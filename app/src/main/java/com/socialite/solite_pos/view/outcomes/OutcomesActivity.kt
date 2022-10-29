package com.socialite.solite_pos.view.outcomes

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ProvideWindowInsets
import com.socialite.solite_pos.R
import com.socialite.solite_pos.compose.BasicAddButton
import com.socialite.solite_pos.compose.BasicTopBar
import com.socialite.solite_pos.compose.SpaceForFloatingButton
import com.socialite.solite_pos.data.source.local.entity.room.master.Outcome
import com.socialite.solite_pos.utils.config.DateUtils
import com.socialite.solite_pos.utils.config.thousand
import com.socialite.solite_pos.view.ui.theme.SolitePOSTheme
import com.socialite.solite_pos.view.viewModel.MainViewModel
import kotlinx.coroutines.launch

class OutcomesActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel

    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = MainViewModel.getMainViewModel(this)

        val date = DateUtils.currentDateTime

        setContent {
            SolitePOSTheme {
                ProvideWindowInsets(
                    windowInsetsAnimationsEnabled = true
                ) {
                    OutcomesContent(date = date)
                }
            }
        }
    }

    @Composable
    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    private fun OutcomesContent(
        date: String
    ) {

        val modalState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
        val scope = rememberCoroutineScope()

        ModalBottomSheetLayout(
            sheetState = modalState,
            sheetShape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp
            ),
            sheetContent = {
                NewOutcome(
                    date = date,
                    onCreateOutcome = {
                        scope.launch {
                            mainViewModel.addNewOutcome(it)
                            modalState.hide()
                        }
                    }
                )
            },
            content = {
                Scaffold(
                    topBar = {
                        val title = stringResource(id = R.string.outcome) + " - ${
                            DateUtils.convertDateFromDb(
                                date,
                                DateUtils.DATE_WITH_DAY_FORMAT
                            )
                        }"
                        BasicTopBar(
                            titleText = title,
                            onBackClicked = {
                                onBackPressed()
                            }
                        )
                    },
                    content = { padding ->
                        Outcomes(
                            modifier = Modifier
                                .padding(padding),
                            mainViewModel = mainViewModel,
                            date = date,
                            onAddClicked = {
                                scope.launch {
                                    modalState.show()
                                }
                            }
                        )
                    }
                )
            }
        )
    }

    @Composable
    private fun Outcomes(
        modifier: Modifier = Modifier,
        mainViewModel: MainViewModel,
        date: String,
        onAddClicked: () -> Unit
    ) {

        val outcomes = mainViewModel.getOutcome(date).collectAsState(initial = emptyList())

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {

            val listState = rememberLazyListState()

            LazyColumn(
                modifier = modifier
                    .align(Alignment.TopCenter),
                state = listState
            ) {
                items(outcomes.value) {
                    OutcomeItem(it)
                }

                item { SpaceForFloatingButton() }
            }

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
                    onAddClicked = onAddClicked
                )
            }
        }
    }

    @Composable
    private fun OutcomeItem(outcome: Outcome) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.surface)
                .padding(16.dp)
        ) {
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
}
