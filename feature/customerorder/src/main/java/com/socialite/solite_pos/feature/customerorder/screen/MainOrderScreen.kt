package com.socialite.solite_pos.feature.customerorder.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.socialite.common.ui.component.MainBackground
import com.socialite.common.ui.component.SoliteContent
import com.socialite.common.ui.extension.contentSpace
import com.socialite.common.utility.constant.SuppressConstant
import com.socialite.core.ui.extension.paddings
import com.socialite.core.ui.extension.round8
import com.socialite.core.ui.extension.size14SemiBold
import com.socialite.core.ui.extension.size16Normal
import com.socialite.core.ui.extension.size24Bold
import com.socialite.feature.customerorder.R
import com.socialite.schema.ui.dummy.DummySchema
import com.socialite.schema.ui.main.Category
import com.socialite.schema.ui.main.Product
import com.socialite.solite_pos.feature.customerorder.component.BucketWidget
import com.socialite.solite_pos.feature.customerorder.component.FavoriteMenuItem
import com.socialite.solite_pos.feature.customerorder.component.ProductItem
import com.socialite.solite_pos.feature.customerorder.component.SearchBar
import com.socialite.solite_pos.feature.customerorder.component.TabItem
import kotlinx.coroutines.launch

@Composable
fun MainOrderScreen(
    modifier: Modifier = Modifier,
    mainStoreName: String,
    currentStoreName: String,
    bucketBottomPadding: Dp,
    currentOrderCount: Int,
    currentTotalOrder: Long,
    favoriteItems: List<Product>,
    productsCategory: Map<Category, List<Product>>,
    onSearchClick: () -> Unit,
    onAllCategoryClick: () -> Unit,
    onBucketClick: () -> Unit
) {
    MainBackground {
        val paddings = MaterialTheme.paddings

        Box(
            modifier = modifier
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                contentPadding = PaddingValues(vertical = MaterialTheme.paddings.medium),
            ) {
                item {
                    StoreInfo(
                        mainStoreName = mainStoreName,
                        currentStoreName = currentStoreName
                    )
                }

                contentSpace(paddings.large)

                item {
                    SearchMenu(
                        onClick = onSearchClick
                    )
                }

                contentSpace(paddings.large)

                item {
                    FavoriteMenu(
                        products = favoriteItems
                    )
                }

                contentSpace(paddings.large)

                item {
                    MenuByCategory(
                        productsCategory = productsCategory,
                        onAllCategoryClick = onAllCategoryClick
                    )
                }
            }

            BucketWidget(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = bucketBottomPadding),
                orderCount = currentOrderCount,
                totalOrder = currentTotalOrder,
                onCLick = onBucketClick
            )
        }
    }
}

@Composable
private fun StoreInfo(
    mainStoreName: String,
    currentStoreName: String
) {
    Column(
        modifier = Modifier.padding(horizontal = MaterialTheme.paddings.medium)
    ) {
        Text(
            text = mainStoreName,
            style = MaterialTheme.typography.size24Bold
        )
        Spacer(modifier = Modifier.height(MaterialTheme.paddings.extraSmall))
        val text = buildAnnotatedString {
            append(stringResource(R.string.common_store_title))
            withStyle(
                SpanStyle(
                    fontWeight = FontWeight.Bold
                )
            ) {
                append(" $currentStoreName")
            }
        }
        Text(
            text = text,
            style = MaterialTheme.typography.size16Normal
        )
    }
}

@Composable
private fun SearchMenu(
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = MaterialTheme.paddings.medium)
    ) {
        Text(
            text = stringResource(R.string.search_menu_title),
            style = MaterialTheme.typography.size14SemiBold
        )
        Spacer(modifier = Modifier.height(MaterialTheme.paddings.smallMedium))
        SearchBar(
            value = "",
            placeholder = stringResource(R.string.search_menu_placeholder),
            onValueChange = {},
            onClick = onClick
        )
    }
}

@Composable
private fun FavoriteMenu(
    products: List<Product>
) {
    Column {
        Text(
            modifier = Modifier.padding(horizontal = MaterialTheme.paddings.medium),
            text = stringResource(R.string.favorite_menu_title),
            style = MaterialTheme.typography.size14SemiBold
        )
        Spacer(modifier = Modifier.height(MaterialTheme.paddings.smallMedium))
        LazyRow(
            contentPadding = PaddingValues(horizontal = MaterialTheme.paddings.medium)
        ) {
            items(
                items = products,
                key = { it.id },
                itemContent = { FavoriteMenuItem(product = it) }
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun MenuByCategory(
    productsCategory: Map<Category, List<Product>>,
    onAllCategoryClick: () -> Unit
) {
    val categories = productsCategory.keys.toTypedArray()

    Column {
        val pagerState = rememberPagerState()
        val scope = rememberCoroutineScope()

        LazyRow(
            contentPadding = PaddingValues(horizontal = MaterialTheme.paddings.medium)
        ) {
            item {
                IconButton(
                    modifier = Modifier
                        .padding(end = MaterialTheme.paddings.small)
                        .background(
                            color = MaterialTheme.colors.secondary,
                            shape = MaterialTheme.shapes.round8
                        )
                        .size(30.dp),
                    onClick = onAllCategoryClick,
                    content = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_all_menu),
                                contentDescription = null,
                                tint = MaterialTheme.colors.surface
                            )
                        }
                    }
                )
            }

            itemsIndexed(
                items = categories,
                key = { _, category -> category.id },
            ) { i, category ->
                TabItem(
                    modifier = Modifier.padding(end = MaterialTheme.paddings.small),
                    name = category.name,
                    isSelected = pagerState.currentPage == i,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(i)
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.paddings.smallMedium))

        HorizontalPager(
            count = productsCategory.count(),
            state = pagerState,
            key = { it },
            contentPadding = PaddingValues(start = MaterialTheme.paddings.medium),
        ) { page ->
            val category = categories[page]
            val products = productsCategory[category]

            Column(
                modifier = Modifier.padding(end = MaterialTheme.paddings.medium)
            ) {
                products?.forEach { product ->
                    ProductItem(product = product)
                    Spacer(modifier = Modifier.height(MaterialTheme.paddings.smallMedium))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Suppress(SuppressConstant.SpellCheckingInspection)
@Composable
private fun Preview() {
    SoliteContent {
        MainOrderScreen(
            modifier = it,
            mainStoreName = "Jajanan Sosialita",
            currentStoreName = "Baros",
            bucketBottomPadding = 50.dp,
            currentOrderCount = 5,
            currentTotalOrder = 45000,
            favoriteItems = DummySchema.products,
            productsCategory = DummySchema.categoriesProducts,
            onSearchClick = {},
            onAllCategoryClick = {},
            onBucketClick = {}
        )
    }
}