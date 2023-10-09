package com.socialite.solite_pos.feature.customerorder.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.socialite.common.ui.component.MainBackground
import com.socialite.common.ui.component.SoliteContent
import com.socialite.common.ui.extension.contentSpace
import com.socialite.common.utility.constant.SuppressConstant
import com.socialite.core.ui.extension.body1Normal
import com.socialite.core.ui.extension.defaultH5
import com.socialite.core.ui.extension.mainMenu
import com.socialite.core.ui.extension.paddings
import com.socialite.feature.customerorder.R
import com.socialite.schema.ui.main.Category
import com.socialite.schema.ui.main.Product
import com.socialite.solite_pos.feature.customerorder.component.FavoriteMenuItem
import com.socialite.solite_pos.feature.customerorder.component.ProductItem
import com.socialite.solite_pos.feature.customerorder.component.SearchBar
import com.socialite.solite_pos.feature.customerorder.component.TabItem
import kotlinx.coroutines.launch

@Composable
fun MainOrderScreen(
    mainStoreName: String,
    currentStoreName: String,
    favoriteItems: List<Product>,
    productsCategory: Map<Category, List<Product>>
) {
    MainBackground {
        val paddings = MaterialTheme.paddings

        var searchText by remember {
            mutableStateOf("")
        }

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
                    value = searchText,
                    onValueChange = { text ->
                        searchText = text
                    }
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
                MenuByCategory(productsCategory = productsCategory)
            }
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
            style = MaterialTheme.typography.defaultH5
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
            style = MaterialTheme.typography.body1Normal
        )
    }
}

@Composable
private fun SearchMenu(
    value: String,
    onValueChange: (value: String) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = MaterialTheme.paddings.medium)
    ) {
        Text(
            text = stringResource(R.string.search_menu_title),
            style = MaterialTheme.typography.mainMenu
        )
        Spacer(modifier = Modifier.height(MaterialTheme.paddings.smallMedium))
        SearchBar(
            value = value,
            placeholder = stringResource(R.string.search_menu_placeholder),
            onValueChange = onValueChange
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
            style = MaterialTheme.typography.mainMenu
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
    productsCategory: Map<Category, List<Product>>
) {
    val categories = productsCategory.keys.toTypedArray()

    Column {
        val pagerState = rememberPagerState()
        val scope = rememberCoroutineScope()

        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = {},
            edgePadding = MaterialTheme.paddings.medium,
            backgroundColor = Color.Transparent
        ) {
            // TODO: Add all categories button
            categories.forEachIndexed { i, category ->
                Tab(
                    modifier = Modifier.padding(end = MaterialTheme.paddings.small),
                    selected = false,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(i)
                        }
                    }
                ) {
                    TabItem(name = category.name, isSelected = pagerState.currentPage == i)
                }
            }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.paddings.smallMedium))

        HorizontalPager(
            modifier = Modifier
                .padding(horizontal = MaterialTheme.paddings.medium),
            count = productsCategory.count(),
            state = pagerState,
            key = { it }
        ) { page ->
            val category = categories[page]
            val products = productsCategory[category]

            Column {
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
            "Jajanan Sosialita",
            "Baros",
            listOf(
                Product(
                    name = "Siomay",
                    desc = "Siomay ayam",
                    price = 15000,
                    category = "",
                    image = "https://denisyordanp.com/public_assets/images/solite_pos_logo.png",
                    id = "1",
                    isActive = true,
                    isUploaded = false
                ),
                Product(
                    name = "Lorem ipsum dolor sit amet",
                    desc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
                    price = 15000,
                    category = "",
                    image = "",
                    id = "2",
                    isActive = true,
                    isUploaded = false
                ),
                Product(
                    name = "Siomay",
                    desc = "Siomay ayam",
                    price = 15000,
                    category = "",
                    image = "https://denisyordanp.com/public_assets/images/solite_pos_logo.png",
                    id = "3",
                    isActive = true,
                    isUploaded = false
                ),
                Product(
                    name = "Lorem ipsum dolor sit amet",
                    desc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
                    price = 15000,
                    category = "",
                    image = "",
                    id = "4",
                    isActive = true,
                    isUploaded = false
                ),
            ),
            mapOf(
                Category(
                    id = "1",
                    name = "Makanan",
                    desc = "",
                    isActive = true,
                    isUploaded = true
                ) to listOf(
                    Product(
                        name = "Siomay",
                        desc = "Siomay ayam",
                        price = 15000,
                        category = "",
                        image = "https://denisyordanp.com/public_assets/images/solite_pos_logo.png",
                        id = "1",
                        isActive = true,
                        isUploaded = false
                    ),
                    Product(
                        name = "Lorem ipsum dolor sit amet",
                        desc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
                        price = 15000,
                        category = "",
                        image = "",
                        id = "2",
                        isActive = true,
                        isUploaded = false
                    ),
                    Product(
                        name = "Siomay",
                        desc = "Siomay ayam",
                        price = 15000,
                        category = "",
                        image = "https://denisyordanp.com/public_assets/images/solite_pos_logo.png",
                        id = "3",
                        isActive = true,
                        isUploaded = false
                    ),
                    Product(
                        name = "Lorem ipsum dolor sit amet",
                        desc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
                        price = 15000,
                        category = "",
                        image = "",
                        id = "4",
                        isActive = true,
                        isUploaded = false
                    ),
                ),
                Category(
                    id = "2",
                    name = "Minuman",
                    desc = "",
                    isActive = true,
                    isUploaded = true
                ) to listOf(
                    Product(
                        name = "Siomay",
                        desc = "Siomay ayam",
                        price = 15000,
                        category = "",
                        image = "https://denisyordanp.com/public_assets/images/solite_pos_logo.png",
                        id = "1",
                        isActive = true,
                        isUploaded = false
                    ),
                    Product(
                        name = "Lorem ipsum dolor sit amet",
                        desc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
                        price = 15000,
                        category = "",
                        image = "",
                        id = "2",
                        isActive = true,
                        isUploaded = false
                    ),
                    Product(
                        name = "Siomay",
                        desc = "Siomay ayam",
                        price = 15000,
                        category = "",
                        image = "https://denisyordanp.com/public_assets/images/solite_pos_logo.png",
                        id = "3",
                        isActive = true,
                        isUploaded = false
                    ),
                    Product(
                        name = "Lorem ipsum dolor sit amet",
                        desc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
                        price = 15000,
                        category = "",
                        image = "",
                        id = "4",
                        isActive = true,
                        isUploaded = false
                    ),
                ),
                Category(
                    id = "3",
                    name = "Coffee",
                    desc = "",
                    isActive = true,
                    isUploaded = true
                ) to listOf(
                    Product(
                        name = "Siomay",
                        desc = "Siomay ayam",
                        price = 15000,
                        category = "",
                        image = "https://denisyordanp.com/public_assets/images/solite_pos_logo.png",
                        id = "1",
                        isActive = true,
                        isUploaded = false
                    ),
                    Product(
                        name = "Lorem ipsum dolor sit amet",
                        desc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
                        price = 15000,
                        category = "",
                        image = "",
                        id = "2",
                        isActive = true,
                        isUploaded = false
                    ),
                    Product(
                        name = "Siomay",
                        desc = "Siomay ayam",
                        price = 15000,
                        category = "",
                        image = "https://denisyordanp.com/public_assets/images/solite_pos_logo.png",
                        id = "3",
                        isActive = true,
                        isUploaded = false
                    ),
                    Product(
                        name = "Lorem ipsum dolor sit amet",
                        desc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
                        price = 15000,
                        category = "",
                        image = "",
                        id = "4",
                        isActive = true,
                        isUploaded = false
                    ),
                ),
                Category(
                    id = "4",
                    name = "Non-Coffee",
                    desc = "",
                    isActive = true,
                    isUploaded = true
                ) to listOf(
                    Product(
                        name = "Siomay",
                        desc = "Siomay ayam",
                        price = 15000,
                        category = "",
                        image = "https://denisyordanp.com/public_assets/images/solite_pos_logo.png",
                        id = "1",
                        isActive = true,
                        isUploaded = false
                    ),
                    Product(
                        name = "Lorem ipsum dolor sit amet",
                        desc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
                        price = 15000,
                        category = "",
                        image = "",
                        id = "2",
                        isActive = true,
                        isUploaded = false
                    ),
                    Product(
                        name = "Siomay",
                        desc = "Siomay ayam",
                        price = 15000,
                        category = "",
                        image = "https://denisyordanp.com/public_assets/images/solite_pos_logo.png",
                        id = "3",
                        isActive = true,
                        isUploaded = false
                    ),
                    Product(
                        name = "Lorem ipsum dolor sit amet",
                        desc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
                        price = 15000,
                        category = "",
                        image = "",
                        id = "4",
                        isActive = true,
                        isUploaded = false
                    ),
                ),
            )
        )
    }
}