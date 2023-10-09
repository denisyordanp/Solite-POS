package com.socialite.solite_pos.view.screens.store.variant_master

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
import androidx.hilt.navigation.compose.hiltViewModel
import com.socialite.solite_pos.R
import com.socialite.solite_pos.compose.BasicAddButton
import com.socialite.solite_pos.compose.BasicEditText
import com.socialite.solite_pos.compose.BasicRadioButton
import com.socialite.solite_pos.compose.BasicTopBar
import com.socialite.solite_pos.compose.OptionItem
import com.socialite.solite_pos.compose.PrimaryButtonView
import com.socialite.solite_pos.compose.SpaceForFloatingButton
import com.socialite.schema.ui.helper.VariantWithOptions
import com.socialite.schema.ui.main.Variant
import com.socialite.schema.ui.main.VariantOption
import com.socialite.solite_pos.view.ui.ModalContent
import kotlinx.coroutines.launch

@Composable
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
fun VariantMasterScreen(
    currentViewModel: VariantMasterViewModel = hiltViewModel(),
    onBackClicked: () -> Unit
) {
    val isUserStaff = currentViewModel.isUserStaff().collectAsState(initial = false).value
    val variants =
        currentViewModel.getVariants().collectAsState(initial = emptyList()).value

    VariantMasterView(
        isUserStaff = isUserStaff,
        variants = variants,
        onBackClicked = onBackClicked,
        onVariant = {
            if (it.isAdd()) {
                currentViewModel.insertVariant(it)
            } else {
                currentViewModel.updateVariant(it)
            }
        },
        onVariantOption = {
            if (it.isAdd()) {
                currentViewModel.insertVariantOption(it)
            } else {
                currentViewModel.updateVariantOption(it)
            }
        },
    )
}

@Composable
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
private fun VariantMasterView(
    isUserStaff: Boolean,
    variants: List<VariantWithOptions>,
    onBackClicked: () -> Unit,
    onVariant: (variant: Variant) -> Unit,
    onVariantOption: (option: VariantOption) -> Unit,
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
                        onVariant(it)
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
                        onVariantOption(it)
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
                    VariantsMasterContent(
                        modifier = Modifier
                            .padding(padding),
                        isUserStaff = isUserStaff,
                        variants = variants,
                        onAddVariantClicked = {
                            selectedVariant = null
                            scope.launch {
                                sheetContent = ModalContent.VARIANT_DETAIL
                                modalState.show()
                            }
                        },
                        onVariantClicked = {
                            if (!isUserStaff) {
                                selectedVariant = it
                                scope.launch {
                                    sheetContent = ModalContent.VARIANT_DETAIL
                                    modalState.show()
                                }
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
                            if (!isUserStaff) {
                                selectedOption = Pair(
                                    variant,
                                    option
                                )
                                scope.launch {
                                    sheetContent = ModalContent.VARIANT_OPTION_DETAIL
                                    modalState.show()
                                }
                            }
                        },
                        onUpdateVariantOption = {
                            onVariantOption(it)
                        },
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
                ) ?: Variant.createNew(
                    name = name,
                    type = if (isSingleOption) Variant.ONE_OPTION else Variant.MULTIPLE_OPTION,
                    isMust = isMust
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
                ) ?: VariantOption.createNew(
                    variant = variant?.id ?: "",
                    name = name,
                    desc = "",
                    isActive = true
                )
                onOptionSubmitted(newOption)
                name = ""
            }
        )
    }
}

@Composable
private fun VariantsMasterContent(
    modifier: Modifier = Modifier,
    isUserStaff: Boolean,
    variants: List<VariantWithOptions>,
    onAddVariantClicked: (() -> Unit)? = null,
    onAddOptionClicked: ((Variant) -> Unit)? = null,
    onVariantClicked: ((Variant) -> Unit)? = null,
    onOptionClicked: ((Variant, VariantOption) -> Unit)? = null,
    onUpdateVariantOption: (option: VariantOption) -> Unit,
) {
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
            items(variants) {
                VariantItem(
                    isUserStaff = isUserStaff,
                    variant = it.variant,
                    options = it.options,
                    isExpanded = expandedItem == it.variant,
                    onVariantClicked = {
                        onVariantClicked?.invoke(it.variant)
                    },
                    onAddOptionClicked = { addVariant ->
                        onAddOptionClicked?.invoke(addVariant)
                    },
                    onOptionClicked = { optVariant, option ->
                        onOptionClicked?.invoke(optVariant, option)
                    },
                    onOptionMasterSwitched = { option ->
                        onUpdateVariantOption(option)
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
            visible = !listState.isScrollInProgress && !isUserStaff,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            BasicAddButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd),
                onAddClicked = {
                    onAddVariantClicked?.invoke()
                }
            )
        }
    }
}

@Composable
private fun VariantItem(
    isUserStaff: Boolean,
    variant: Variant,
    options: List<VariantOption>,
    isExpanded: Boolean,
    onVariantClicked: () -> Unit,
    onAddOptionClicked: (Variant) -> Unit,
    onOptionClicked: (Variant, VariantOption) -> Unit,
    onOptionMasterSwitched: (option: VariantOption) -> Unit,
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
            Text(
                text = "${
                    if (variant.isMust == true) stringResource(id = R.string.must_choice) else stringResource(
                        id = R.string.optional_choice
                    )
                }, ${
                    if (variant.isSingleOption()) stringResource(id = R.string.single_option) else stringResource(
                        id = R.string.multiple_option
                    )
                }",
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
        if (!isUserStaff) {
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
        }
        options.forEach {
            OptionItem(
                isProductVariant = false,
                optionName = it.name,
                isOptionActive = it.isActive,
                onOptionClicked = {
                    onOptionClicked(
                        variant,
                        it
                    )
                },
                onOptionSwitch = { isActive ->
                    onOptionMasterSwitched(
                        it.copy(
                            isActive = isActive
                        )
                    )
                }
            )
            Spacer(modifier = Modifier.height(2.dp))
        }
        Spacer(modifier = Modifier.height(2.dp))
    }
}
