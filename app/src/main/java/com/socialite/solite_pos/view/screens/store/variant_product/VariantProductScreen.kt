package com.socialite.solite_pos.view.screens.store.variant_product

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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.socialite.solite_pos.R
import com.socialite.solite_pos.compose.BasicTopBar
import com.socialite.solite_pos.compose.OptionItem
import com.socialite.solite_pos.compose.SpaceForFloatingButton
import com.socialite.domain.schema.helper.VariantWithOptions
import com.socialite.data.schema.room.new_bridge.VariantProduct
import com.socialite.data.schema.room.new_master.Product
import com.socialite.data.schema.room.new_master.Variant
import com.socialite.data.schema.room.new_master.VariantOption

@Composable
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
fun VariantProductScreen(
    currentViewModel: VariantProductViewModel = hiltViewModel(),
    productId: String,
    onBackClicked: () -> Unit
) {
    LaunchedEffect(key1 = productId) {
        currentViewModel.initialLoad(productId)
    }
    val state = currentViewModel.viewState.collectAsState().value

    state.product?.let {
        VariantProductView(
            product = state.product,
            variants = state.variants,
            selectedProductOptions = state.selectedProductOptions,
            onBackClicked = onBackClicked,
            onOptionProductSwitched = { option, variantProduct ->
                if (variantProduct != null) {
                    currentViewModel.removeVariantProduct(variantProduct)
                } else {
                    val newVariantProduct = VariantProduct.createNewVariantProduct(
                        variant = option.variant,
                        variantOption = option.id,
                        product = productId
                    )
                    currentViewModel.insertVariantProduct(newVariantProduct)
                }
            }
        )
    }
}

@Composable
private fun VariantProductView(
    variants: List<VariantWithOptions>,
    selectedProductOptions: List<VariantProduct>,
    product: Product,
    onBackClicked: () -> Unit,
    onOptionProductSwitched: (option: VariantOption, variantProduct: VariantProduct?) -> Unit,
) {
    Scaffold(
        topBar = {
            val title = "${stringResource(R.string.variants)} - ${product.name}"
            BasicTopBar(
                titleText = title,
                onBackClicked = onBackClicked
            )
        },
        content = { padding ->
            VariantsContent(
                modifier = Modifier
                    .padding(padding),
                selectedProductOptions = selectedProductOptions,
                variants = variants,
                onOptionProductSwitched = onOptionProductSwitched,
            )
        }
    )
}

@Composable
private fun VariantsContent(
    modifier: Modifier = Modifier,
    variants: List<VariantWithOptions>,
    selectedProductOptions: List<VariantProduct>,
    onOptionClicked: ((Variant, VariantOption) -> Unit)? = null,
    onOptionProductSwitched: (option: VariantOption, variantProduct: VariantProduct?) -> Unit,
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
                    variant = it.variant,
                    options = it.options,
                    selectedProductOptions = selectedProductOptions.filter { variantProduct ->
                        variantProduct.variant == it.variant.id
                    },
                    isExpanded = expandedItem == it.variant,
                    onOptionClicked = { optVariant, option ->
                        onOptionClicked?.invoke(optVariant, option)
                    },
                    onOptionProductSwitched = { option, variantProduct ->
                        onOptionProductSwitched(option, variantProduct)
                    },
                    onExpand = { variant ->
                        expandedItem = if (expandedItem != variant) variant else null
                    }
                )
            }

            item { SpaceForFloatingButton() }
        }
    }
}

@Composable
private fun VariantItem(
    variant: Variant,
    options: List<VariantOption>,
    selectedProductOptions: List<VariantProduct>,
    isExpanded: Boolean,
    onOptionClicked: (Variant, VariantOption) -> Unit,
    onOptionProductSwitched: (option: VariantOption, variantProduct: VariantProduct?) -> Unit,
    onExpand: (Variant) -> Unit
) {
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
                text = variant.name,
                style = MaterialTheme.typography.body1.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(
                    id = R.string.option_selected,
                    selectedProductOptions.size.toString()
                ),
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
        options.forEach {
            val variantProduct = selectedProductOptions.find { variantProduct ->
                variantProduct.variantOption == it.id
            }

            OptionItem(
                isProductVariant = true,
                optionName = it.name,
                isOptionActive = variantProduct != null,
                onOptionClicked = {
                    onOptionClicked(
                        variant,
                        it
                    )
                },
                onOptionSwitch = { _ ->
                    onOptionProductSwitched(
                        it,
                        variantProduct
                    )
                }
            )
            Spacer(modifier = Modifier.height(2.dp))
        }
        Spacer(modifier = Modifier.height(2.dp))
    }
}
