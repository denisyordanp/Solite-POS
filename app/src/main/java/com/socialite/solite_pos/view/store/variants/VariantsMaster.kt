package com.socialite.solite_pos.view.store.variants

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.socialite.solite_pos.R
import com.socialite.solite_pos.compose.BasicAddButton
import com.socialite.solite_pos.compose.BasicEditText
import com.socialite.solite_pos.compose.BasicRadioButton
import com.socialite.solite_pos.compose.BasicTopBar
import com.socialite.solite_pos.compose.PrimaryButtonView
import com.socialite.solite_pos.compose.SpaceForFloatingButton
import com.socialite.solite_pos.data.source.local.entity.room.master.Variant
import com.socialite.solite_pos.data.source.local.entity.room.master.VariantOption
import com.socialite.solite_pos.view.ui.ModalContent
import com.socialite.solite_pos.view.viewModel.ProductViewModel
import kotlinx.coroutines.launch

@Composable
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
fun VariantsMaster(
    productViewModel: ProductViewModel,
    onBackClicked: () -> Unit
) {
    val modalState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    if (modalState.currentValue == ModalBottomSheetValue.Hidden) {
        LocalSoftwareKeyboardController.current?.hide()
    }

    var sheetContent by remember {
        mutableStateOf(ModalContent.VARIANT_DETAIL)
    }

    var selectedVariant by remember {
        mutableStateOf<Variant?>(null)
    }

    var selectedOption by remember {
        mutableStateOf<Pair<Variant, VariantOption?>?>(null)
    }

    ModalBottomSheetLayout(
        sheetState = modalState,
        sheetShape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp
        ),
        sheetContent = {
            when (sheetContent) {
                ModalContent.VARIANT_DETAIL -> VariantDetail(
                    variant = selectedVariant,
                    onVariantSubmitted = {
                        if (it.isAdd())
                            productViewModel.insertVariant(it)
                        else
                            productViewModel.updateVariant(it)

                        scope.launch {
                            modalState.hide()
                        }
                        selectedVariant = null
                    }
                )

                ModalContent.VARIANT_OPTION_DETAIL -> VariantOptionDetail(
                    variant = selectedOption?.first,
                    option = selectedOption?.second,
                    onOptionSubmitted = {
                        if (it.isAdd())
                            productViewModel.insertVariantOption(it)
                        else
                            productViewModel.updateVariantOption(it)

                        scope.launch {
                            modalState.hide()
                        }
                        selectedVariant = null
                    }
                )

                else -> {
                    // Do nothing
                }
            }
        },
        content = {
            Scaffold(
                topBar = {
                    BasicTopBar(
                        titleText = stringResource(R.string.variants),
                        onBackClicked = onBackClicked
                    )
                },
                content = { padding ->
                    VariantsContent(
                        modifier = Modifier
                            .padding(padding),
                        productViewModel = productViewModel,
                        onAddVariantClicked = {
                            selectedVariant = null
                            scope.launch {
                                sheetContent = ModalContent.VARIANT_DETAIL
                                modalState.show()
                            }
                        },
                        onVariantClicked = {
                            selectedVariant = it
                            scope.launch {
                                sheetContent = ModalContent.VARIANT_DETAIL
                                modalState.show()
                            }
                        },
                        onAddOptionClicked = {
                            selectedOption = Pair(
                                it,
                                null
                            )
                            scope.launch {
                                sheetContent = ModalContent.VARIANT_OPTION_DETAIL
                                modalState.show()
                            }
                        },
                        onOptionClicked = { variant, option ->
                            selectedOption = Pair(
                                variant,
                                option
                            )
                            scope.launch {
                                sheetContent = ModalContent.VARIANT_OPTION_DETAIL
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
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
private fun VariantDetail(
    variant: Variant?,
    onVariantSubmitted: (variant: Variant) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var isMust by remember { mutableStateOf(false) }
    var isSingleOption by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = variant) {
        name = variant?.name ?: ""
        isMust = variant?.isMust ?: false
        isSingleOption = variant?.isSingleOption() ?: true
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        BasicEditText(
            value = name,
            placeHolder = stringResource(R.string.variant_name),
            onValueChange = {
                name = it
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Checkbox(
                checked = isMust,
                onCheckedChange = {
                    isMust = it
                }
            )
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically),
                text = stringResource(id = R.string.must_choice),
                style = MaterialTheme.typography.body2
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            BasicRadioButton(
                isSelected = isSingleOption,
                text = stringResource(R.string.single_option),
                onClicked = {
                    isSingleOption = true
                }
            )
            Spacer(modifier = Modifier.width(16.dp))
            BasicRadioButton(
                isSelected = !isSingleOption,
                text = stringResource(R.string.multiple_option),
                onClicked = {
                    isSingleOption = false
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        PrimaryButtonView(
            modifier = Modifier
                .fillMaxWidth(),
            buttonText = stringResource(
                id = if (variant == null) R.string.adding else R.string.save
            ),
            onClick = {
                val newVariant = variant?.copy(
                    name = name,
                    type = if (isSingleOption) Variant.ONE_OPTION else Variant.MULTIPLE_OPTION,
                    isMust = isMust,
                ) ?: Variant(
                    name = name,
                    type = if (isSingleOption) Variant.ONE_OPTION else Variant.MULTIPLE_OPTION,
                    isMust = isMust,
                    isMix = false
                )
                onVariantSubmitted(newVariant)
                name = ""
                isMust = false
                isSingleOption = true
            }
        )
    }
}

@Composable
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
private fun VariantOptionDetail(
    variant: Variant?,
    option: VariantOption?,
    onOptionSubmitted: (VariantOption) -> Unit
) {
    var name by remember { mutableStateOf("") }

    LaunchedEffect(key1 = option) {
        name = option?.name ?: ""
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Text(
            text = variant?.name ?: "",
            style = MaterialTheme.typography.body1.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        BasicEditText(
            value = name,
            placeHolder = stringResource(R.string.option_name),
            onValueChange = {
                name = it
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        PrimaryButtonView(
            modifier = Modifier
                .fillMaxWidth(),
            buttonText = stringResource(
                id = if (option == null) R.string.adding else R.string.save
            ),
            onClick = {
                val newOption = option?.copy(
                    name = name
                ) ?: VariantOption(
                    idVariant = variant?.id ?: 0L,
                    name = name,
                    desc = "",
                    isCount = false,
                    isActive = true
                )
                onOptionSubmitted(newOption)
                name = ""
            }
        )
    }
}

@Composable
private fun VariantsContent(
    productViewModel: ProductViewModel,
    modifier: Modifier = Modifier,
    onAddVariantClicked: () -> Unit,
    onAddOptionClicked: (Variant) -> Unit,
    onVariantClicked: (Variant) -> Unit,
    onOptionClicked: (Variant, VariantOption) -> Unit
) {

    val variants = productViewModel.variants.collectAsState(initial = emptyList())

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {

        val listState = rememberLazyListState()
        var expandedItem by remember { mutableStateOf<Variant?>(null) }

        LazyColumn(
            modifier = Modifier
                .align(Alignment.TopCenter),
            state = listState
        ) {
            items(variants.value) {

                val query = VariantOption.getFilter(it.id, VariantOption.ALL)
                val options =
                    productViewModel.getVariantOptions(query).collectAsState(initial = emptyList())

                VariantItem(
                    variant = it,
                    options = options.value,
                    isExpanded = expandedItem == it,
                    onVariantClicked = {
                        onVariantClicked(it)
                    },
                    onAddOptionClicked = onAddOptionClicked,
                    onOptionClicked = onOptionClicked,
                    onOptionSwitched = { option ->
                        productViewModel.updateVariantOption(option)
                    },
                    onExpand = { variant ->
                        expandedItem = if (expandedItem != variant) variant else null
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
                onAddClicked = onAddVariantClicked
            )
        }
    }
}

@Composable
private fun VariantItem(
    variant: Variant,
    options: List<VariantOption>,
    isExpanded: Boolean,
    onVariantClicked: () -> Unit,
    onAddOptionClicked: (Variant) -> Unit,
    onOptionClicked: (Variant, VariantOption) -> Unit,
    onOptionSwitched: (VariantOption) -> Unit,
    onExpand: (Variant) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onVariantClicked()
            }
            .background(color = MaterialTheme.colors.surface)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = variant.name,
                style = MaterialTheme.typography.body1.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            val descText = "${
                if (variant.isMust == true) stringResource(id = R.string.must_choice) else stringResource(
                    id = R.string.optional_choice
                )
            }, ${
                if (variant.isSingleOption()) stringResource(id = R.string.single_option) else stringResource(
                    id = R.string.multiple_option
                )
            }"
            Text(
                text = descText,
                style = MaterialTheme.typography.body1
            )
        }
        Icon(
            modifier = Modifier
                .clickable {
                    onExpand(variant)
                }
                .align(Alignment.CenterVertically)
                .padding(16.dp),
            painter = painterResource(
                id = if (isExpanded) R.drawable.ic_expand_less_24 else R.drawable.ic_expand_more_24
            ),
            tint = MaterialTheme.colors.onSurface,
            contentDescription = null
        )
    }
    Spacer(modifier = Modifier.height(4.dp))
    if (isExpanded) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.surface)
                .clickable {
                    onAddOptionClicked(variant)
                }
                .padding(16.dp)
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = stringResource(R.string.add_new_option),
                style = MaterialTheme.typography.body2.copy(
                    fontStyle = FontStyle.Italic
                )
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        options.forEach {
            OptionItem(
                variantOption = it,
                onOptionClicked = {
                    onOptionClicked(
                        variant,
                        it
                    )
                },
                onOptionSwitch = { isActive ->
                    onOptionSwitched(
                        it.copy(
                            isActive = isActive
                        )
                    )
                }
            )
            Spacer(modifier = Modifier.height(2.dp))
        }
    }
}

@Composable
private fun OptionItem(
    variantOption: VariantOption,
    onOptionClicked: () -> Unit,
    onOptionSwitch: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.surface)
            .clickable {
                onOptionClicked()
            }
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f),
            text = variantOption.name,
            style = MaterialTheme.typography.body2.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Switch(
            checked = variantOption.isActive,
            onCheckedChange = {
                onOptionSwitch(it)
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colors.primary,
                uncheckedThumbColor = MaterialTheme.colors.primaryVariant
            )
        )
    }
}
