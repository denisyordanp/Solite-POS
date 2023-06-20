package com.socialite.solite_pos.view.store.stores

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.socialite.solite_pos.R
import com.socialite.solite_pos.compose.BasicAddButton
import com.socialite.solite_pos.compose.BasicAlertDialog
import com.socialite.solite_pos.compose.BasicEditText
import com.socialite.solite_pos.compose.BasicTopBar
import com.socialite.solite_pos.compose.PrimaryButtonView
import com.socialite.solite_pos.compose.SpaceForFloatingButton
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Store
import kotlinx.coroutines.launch

@Composable
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
fun StoresScreen(
    currentViewModel: StoresViewModel = viewModel(
        factory = StoresViewModel.getFactory(
            LocalContext.current
        )
    ),
    onBackClicked: () -> Unit
) {
    val modalState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    val stores = currentViewModel.getStores().collectAsState(initial = emptyList())
    val selectedActiveStore = currentViewModel.selectedStore.collectAsState(initial = "")
    var selectedStoreForDetail by remember { mutableStateOf<Store?>(null) }
    if (modalState.currentValue == ModalBottomSheetValue.Hidden) {
        LocalSoftwareKeyboardController.current?.hide()
    }

    ModalBottomSheetLayout(
        sheetState = modalState,
        sheetShape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp
        ),
        sheetContent = {
            StoreDetail(
                store = selectedStoreForDetail,
                onSubmitStore = {
                    scope.launch {
                        if (it.isNewStore()) {
                            currentViewModel.insertStore(it)
                        } else {
                            currentViewModel.updateStore(it)
                        }
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
                        onBackClicked = onBackClicked
                    )
                },
                content = { padding ->
                    StoresContent(
                        modifier = Modifier
                            .padding(padding),
                        stores = stores.value,
                        selectedStore = selectedActiveStore.value,
                        onAddClicked = {
                            scope.launch {
                                selectedStoreForDetail = null
                                modalState.show()
                            }
                        },
                        onStoreClicked = {
                            scope.launch {
                                selectedStoreForDetail = it
                                modalState.show()
                            }
                        },
                        onSelectStore = {
                            currentViewModel.selectStore(it)
                        }
                    )
                }
            )
        }
    )
}

@Composable
private fun StoresContent(
    modifier: Modifier = Modifier,
    stores: List<Store>,
    selectedStore: String,
    onAddClicked: () -> Unit,
    onStoreClicked: (Store) -> Unit,
    onSelectStore: (storeId: String) -> Unit
) {
    var alertSelectStore by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {

        val listState = rememberLazyListState()

        LazyColumn(
            modifier = Modifier
                .align(Alignment.TopCenter),
            state = listState
        ) {
            items(stores) {
                StoreItem(
                    store = it,
                    selected = selectedStore,
                    onStoreClicked = onStoreClicked,
                    onStoreSwitched = { isActive ->
                        if (isActive) alertSelectStore = it.id
                    }
                )
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

    alertSelectStore?.let {
        BasicAlertDialog(
            titleText = stringResource(R.string.select_store),
            descText = stringResource(R.string.are_you_sure_select_this_store_for_next_orders),
            positiveAction = {
                onSelectStore(it)
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
    selected: String,
    onStoreClicked: (Store) -> Unit,
    onStoreSwitched: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onStoreClicked(store)
            }
            .background(color = MaterialTheme.colors.surface)
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
        Switch(
            checked = store.id == selected,
            onCheckedChange = onStoreSwitched,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colors.primary,
                uncheckedThumbColor = MaterialTheme.colors.primaryVariant
            )
        )
    }
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
private fun StoreDetail(
    store: Store?,
    onSubmitStore: (Store) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    LaunchedEffect(key1 = store) {
        name = store?.name ?: ""
        address = store?.address ?: ""
    }

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
            buttonText = stringResource(
                id = if (store != null) R.string.save else R.string.adding
            ),
            onClick = {
                if (name.isNotEmpty() && address.isNotEmpty()) {
                    onSubmitStore(
                        store?.copy(
                            name = name,
                            address = address
                        ) ?: Store.add(
                            name = name,
                            address = address
                        )
                    )
                    name = ""
                    address = ""
                }
            }
        )
    }
}
