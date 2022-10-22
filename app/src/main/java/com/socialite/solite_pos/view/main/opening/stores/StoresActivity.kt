package com.socialite.solite_pos.view.main.opening.stores

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ProvideWindowInsets
import com.socialite.solite_pos.R
import com.socialite.solite_pos.compose.BasicAddButton
import com.socialite.solite_pos.compose.BasicAlertDialog
import com.socialite.solite_pos.compose.BasicEditText
import com.socialite.solite_pos.compose.BasicTopBar
import com.socialite.solite_pos.compose.PrimaryButtonView
import com.socialite.solite_pos.data.source.local.entity.room.master.Store
import com.socialite.solite_pos.view.main.opening.ui.theme.SolitePOSTheme
import com.socialite.solite_pos.view.viewModel.MainViewModel
import kotlinx.coroutines.launch

class StoresActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel

    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = MainViewModel.getMainViewModel(this)

        setContent {
            SolitePOSTheme {
                ProvideWindowInsets(
                    windowInsetsAnimationsEnabled = true
                ) {
                    StoresView()
                }
            }
        }
    }

    @Composable
    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    fun StoresView() {
        val modalState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
        val scope = rememberCoroutineScope()

        ModalBottomSheetLayout(
            sheetState = modalState,
            sheetShape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp
            ),
            sheetContent = {
                NewStore(
                    onCreateStore = {
                        scope.launch {
                            mainViewModel.insertStore(it)
                            modalState.hide()
                        }
                    }
                )
            },
            content = {
                Scaffold(
                    topBar = {
                        BasicTopBar(
                            titleText = stringResource(id = R.string.stores),
                            onBackClicked = {
                                onBackPressed()
                            }
                        )
                    },
                    content = { padding ->
                        StoresContent(
                            modifier = Modifier
                                .padding(padding),
                            mainViewModel = mainViewModel,
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
    private fun StoresContent(
        mainViewModel: MainViewModel,
        modifier: Modifier = Modifier,
        onAddClicked: () -> Unit
    ) {

        var alertSelectStore by remember { mutableStateOf<Long?>(null) }

        val stores = mainViewModel.getStores().collectAsState(initial = emptyList())
        val selected = mainViewModel.selectedStore.collectAsState(initial = 0L)

        Box(
            modifier = modifier
                .fillMaxSize()
        ) {

            LazyColumn(
                modifier = Modifier
                    .align(Alignment.TopCenter)
            ) {
                items(stores.value) {
                    StoreItem(
                        store = it,
                        selected = selected.value,
                        onStoreClicked = {
                            alertSelectStore = it.id
                        }
                    )
                }
            }

            BasicAddButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd),
                onAddClicked = onAddClicked
            )
        }

        alertSelectStore?.let {
            BasicAlertDialog(
                titleText = stringResource(R.string.select_store),
                descText = stringResource(R.string.are_you_sure_select_this_store_for_next_orders),
                positiveAction = {
                    mainViewModel.selectStore(it)
                    alertSelectStore = null
                },
                positiveText = stringResource(R.string.yes),
                negativeAction = {
                    alertSelectStore = null
                },
                negativeText = stringResource(R.string.no)
            )
        }
    }

    @Composable
    private fun StoreItem(
        store: Store,
        selected: Long,
        onStoreClicked: () -> Unit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onStoreClicked()
                }
                .background(color = Color.White)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = store.name,
                    style = MaterialTheme.typography.body1.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = store.address,
                    style = MaterialTheme.typography.body2
                )
            }
            if (store.id == selected) {
                Icon(
                    modifier = Modifier
                        .size(18.dp)
                        .align(Alignment.CenterVertically),
                    painter = painterResource(id = R.drawable.ic_done_all),
                    tint = MaterialTheme.colors.primary,
                    contentDescription = null
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
    }

    @Composable
    @ExperimentalComposeUiApi
    @ExperimentalMaterialApi
    private fun NewStore(
        onCreateStore: (Store) -> Unit
    ) {
        var name by remember { mutableStateOf("") }
        var address by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            BasicEditText(
                value = name,
                placeHolder = stringResource(R.string.store_name),
                onValueChange = {
                    name = it
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            BasicEditText(
                value = address,
                placeHolder = stringResource(R.string.address),
                onValueChange = {
                    address = it
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            PrimaryButtonView(
                modifier = Modifier
                    .fillMaxWidth(),
                buttonText = stringResource(R.string.add_store),
                onClick = {
                    onCreateStore(
                        Store(
                            name = name,
                            address = address
                        )
                    )
                    name = ""
                    address = ""
                }
            )
        }
    }
}
