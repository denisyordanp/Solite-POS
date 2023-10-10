package com.socialite.solite_pos.feature.customerorder.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.socialite.common.ui.component.SoliteContent
import com.socialite.core.ui.extension.paddings
import com.socialite.feature.customerorder.R
import com.socialite.schema.ui.dummy.DummySchema
import com.socialite.schema.ui.helper.ProductWithCategory
import com.socialite.solite_pos.feature.customerorder.component.ProductItem
import com.socialite.solite_pos.feature.customerorder.component.SearchTopBar

@Composable
fun SearchMenuScreen(
    modifier: Modifier = Modifier,
    products: List<ProductWithCategory>,
    onBackClick: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        var searchValue by remember { mutableStateOf("") }

        SearchTopBar(
            searchValue = searchValue,
            searchPlaceHolder = stringResource(id = R.string.search_menu_placeholder),
            onSearchValueChange = { newValue ->
                searchValue = newValue
            },
            onBackClick = onBackClick
        )

        LazyColumn(
            modifier = Modifier
                .padding(horizontal = MaterialTheme.paddings.medium),
            contentPadding = PaddingValues(top = MaterialTheme.paddings.medium)
        ) {
            items(
                items = products,
                key = { p -> p.product.id }
            ) {productWithCategory ->
                ProductItem(product = productWithCategory.product, categoryItem = productWithCategory.category)
                Spacer(modifier = Modifier.height(MaterialTheme.paddings.smallMedium))
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    SoliteContent {
        SearchMenuScreen(
            modifier = it,
            products = DummySchema.productWithCategories,
            onBackClick = {}
        )
    }
}