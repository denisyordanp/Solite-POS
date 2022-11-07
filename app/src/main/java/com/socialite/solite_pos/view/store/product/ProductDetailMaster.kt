package com.socialite.solite_pos.view.store.product

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.socialite.solite_pos.R
import com.socialite.solite_pos.compose.BasicEditText
import com.socialite.solite_pos.compose.BasicTopBar
import com.socialite.solite_pos.compose.PrimaryButtonView
import com.socialite.solite_pos.compose.basicDropdown
import com.socialite.solite_pos.data.source.local.entity.helper.VariantWithOptions
import com.socialite.solite_pos.data.source.local.entity.room.helper.ProductWithCategory
import com.socialite.solite_pos.data.source.local.entity.room.master.Category
import com.socialite.solite_pos.data.source.local.entity.room.master.Product
import com.socialite.solite_pos.utils.config.thousand
import com.socialite.solite_pos.view.ui.ThousandAndSuggestionVisualTransformation
import com.socialite.solite_pos.view.viewModel.ProductViewModel

@Composable
@ExperimentalComposeUiApi
fun ProductDetailMaster(
    productViewModel: ProductViewModel,
    productId: Long,
    onBackClicked: () -> Unit
) {

    val product = productViewModel.getProductWithCategory(productId)
        .collectAsState(initial = null)

    var isEditMode by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            BasicTopBar(
                titleText = product.value?.product?.name ?: "",
                onBackClicked = onBackClicked,
                endIcon = R.drawable.ic_edit_24,
                endAction = {
                    isEditMode = !isEditMode
                }
            )
        },
        content = { padding ->
            product.value?.let {
                DetailContent(
                    modifier = Modifier.padding(padding),
                    productViewModel = productViewModel,
                    product = it,
                    isEditMode = isEditMode
                )
            }
        }
    )
}

@Composable
@ExperimentalComposeUiApi
private fun DetailContent(
    modifier: Modifier = Modifier,
    productViewModel: ProductViewModel,
    product: ProductWithCategory,
    isEditMode: Boolean,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
        ) {

            val query = Category.getFilter(Category.ALL)
            val categories =
                productViewModel.getCategories(query).collectAsState(initial = emptyList())
            val variants =
                productViewModel.getProductVariantOptions(product.product.id)
                    .collectAsState(initial = null)

            var categoryExpanded by remember {
                mutableStateOf(false)
            }

            var selectedCategory by remember {
                mutableStateOf(product.category)
            }

            LazyColumn {

                basicDropdown(
                    isExpanded = categoryExpanded,
                    title = R.string.select_category,
                    selectedItem = selectedCategory.name,
                    items = categories.value,
                    isEnable = isEditMode,
                    onHeaderClicked = {
                        categoryExpanded = !categoryExpanded
                    },
                    onSelectedItem = {
                        selectedCategory = it as Category
                        categoryExpanded = false
                    }
                )

                item {
                    TextContent(
                        product = product.product,
                        isEditMode = isEditMode
                    )
                }

                item {
                    VariantSelected(
                        variants = variants.value
                    )
                }
            }
        }

        if (isEditMode) {
            EditButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
@ExperimentalComposeUiApi
private fun TextContent(
    product: Product,
    isEditMode: Boolean
) {
    Spacer(modifier = Modifier.height(4.dp))
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.surface)
            .padding(16.dp)
    ) {

        var name by remember { mutableStateOf(product.name) }
        var desc by remember { mutableStateOf(product.desc) }
        var sellPrice by remember { mutableStateOf(product.sellPrice) }

        BasicEditText(
            value = name,
            placeHolder = stringResource(R.string.name),
            isEnabled = isEditMode,
            onValueChange = {
                name = it
            }
        )
        BasicEditText(
            value = desc,
            placeHolder = stringResource(R.string.description),
            isEnabled = isEditMode,
            onValueChange = {
                desc = it
            }
        )
        val price = if (sellPrice == 0L) "" else sellPrice.toString()
        BasicEditText(
            value = if (isEditMode) price else "Rp. ${price.toLong().thousand()}",
            placeHolder = stringResource(R.string.sell_price),
            isEnabled = isEditMode,
            visualTransformation = ThousandAndSuggestionVisualTransformation(false),
            keyboardType = KeyboardType.Number,
            onValueChange = {
                sellPrice = it.toLongOrNull() ?: 0L
            }
        )
    }
}

@Composable
private fun VariantSelected(
    variants: List<VariantWithOptions>?
) {
    Spacer(modifier = Modifier.padding(bottom = 4.dp))
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.surface)
            .clickable {

            }
            .padding(16.dp)
    ) {
        if (!variants.isNullOrEmpty()) {
            variants.forEach {
                VariantItem(it)
            }
        } else {
            SelectVariantButton()
        }
    }
}

@Composable
private fun SelectVariantButton() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            text = stringResource(id = R.string.select_variant),
            style = MaterialTheme.typography.body2.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_right),
            tint = MaterialTheme.colors.onSurface,
            contentDescription = null
        )
    }
}

@Composable
private fun VariantItem(
    variant: VariantWithOptions
) {
    Text(
        text = variant.variant.name,
        style = MaterialTheme.typography.body2.copy(
            fontWeight = FontWeight.Bold
        )
    )
    Text(
        text = variant.optionsString(),
        style = MaterialTheme.typography.body2
    )
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
private fun EditButton(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp),
        elevation = 8.dp,
        color = MaterialTheme.colors.surface
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
        ) {
            PrimaryButtonView(
                modifier = Modifier
                    .fillMaxWidth(),
                buttonText = stringResource(R.string.save),
                onClick = {}
            )
        }
    }
}
