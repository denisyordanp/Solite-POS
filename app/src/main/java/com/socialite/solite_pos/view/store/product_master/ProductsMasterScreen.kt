package com.socialite.solite_pos.view.store.product_master

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.socialite.solite_pos.R
import com.socialite.solite_pos.compose.BasicAddButton
import com.socialite.solite_pos.compose.SpaceForFloatingButton
import com.socialite.solite_pos.data.source.local.entity.helper.ProductVariantCount
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Category
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Product
import com.socialite.solite_pos.utils.config.thousand
import kotlinx.coroutines.launch

@Composable
@ExperimentalPagerApi
fun ProductsMasterScreen(
    currentViewModel: ProductMasterViewModel = hiltViewModel(),
    onBackClicked: () -> Unit,
    onItemClicked: (Product) -> Unit,
    onVariantClicked: (Product) -> Unit,
    onAddProductClicked: () -> Unit
) {
    val pagerState = rememberPagerState()
    val products = currentViewModel.getProductsWithCategory().collectAsState(initial = emptyList())
    val categories = products.value.map {
        it.first
    }

    Box {
        var shouldShowButton by remember { mutableStateOf(true) }

        Scaffold(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxSize(),
            topBar = {
                ProductHeader(
                    pagerState = pagerState,
                    categories = categories,
                    onBackClicked = onBackClicked
                )
            },
            content = { padding ->
                HorizontalPager(
                    modifier = Modifier
                        .padding(padding),
                    count = products.value.size,
                    state = pagerState
                ) { page ->
                    ProductItems(
                        products = products.value[page].second,
                        onItemClicked = onItemClicked,
                        onVariantClicked = onVariantClicked,
                        onScrollInProgress = {
                            shouldShowButton = !it
                        },
                        onUpdateProduct = {
                            currentViewModel.updateProduct(it)
                        }
                    )
                }
            }
        )

        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            visible = shouldShowButton,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            BasicAddButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd),
                onAddClicked = onAddProductClicked
            )
        }
    }
}

@Composable
@ExperimentalPagerApi
private fun ProductHeader(
    pagerState: PagerState,
    categories: List<Category>,
    onBackClicked: () -> Unit
) {
    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        elevation = 8.dp,
        color = MaterialTheme.colors.primary
    ) {
        Row(
            modifier = Modifier
                .height(50.dp)
        ) {
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .clickable { onBackClicked() }
                    .padding(start = 8.dp)
                    .padding(8.dp),
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = null,
                tint = MaterialTheme.colors.onSurface
            )
            Spacer(modifier = Modifier.width(8.dp))
            if (categories.isNotEmpty()) {
                ScrollableTabRow(
                    selectedTabIndex = pagerState.currentPage,
                    indicator = {
                        TabRowDefaults.Indicator(
                            Modifier.pagerTabIndicatorOffset(pagerState, it)
                        )
                    },
                    edgePadding = 16.dp
                ) {
                    categories.forEachIndexed { i, category ->
                        Tab(
                            selected = pagerState.currentPage == i,
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(i)
                                }
                            }
                        ) {

                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                            ) {
                                Text(
                                    modifier = Modifier,
                                    text = category.name,
                                    style = MaterialTheme.typography.body2
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductItems(
    products: List<ProductVariantCount>,
    onItemClicked: (Product) -> Unit,
    onVariantClicked: (Product) -> Unit,
    onScrollInProgress: (Boolean) -> Unit,
    onUpdateProduct: (product: Product) -> Unit
) {
    val listState = rememberLazyListState()
    onScrollInProgress(listState.isScrollInProgress)

    LazyColumn(state = listState) {
        items(products) {
            ProductItem(
                viewData = it,
                onItemClicked = onItemClicked,
                onVariantClicked = {
                    onVariantClicked(it.product)
                },
                onUpdateProduct = onUpdateProduct
            )
        }

        item { SpaceForFloatingButton() }
    }
}

@Composable
private fun ProductItem(
    viewData: ProductVariantCount,
    onItemClicked: (Product) -> Unit,
    onVariantClicked: () -> Unit,
    onUpdateProduct: (product: Product) -> Unit
) {
    val product = viewData.product
    ConstraintLayout(
        modifier = Modifier
            .padding(bottom = 4.dp)
            .clickable { onItemClicked(product) }
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.surface)
            .padding(16.dp)
    ) {

        val (content, switch, variant) = createRefs()

        Column(
            modifier = Modifier
                .constrainAs(content) {
                    linkTo(top = parent.top, bottom = parent.bottom)
                    linkTo(start = parent.start, end = variant.start, endMargin = 8.dp)
                    width = Dimension.fillToConstraints
                }
                .fillMaxWidth()
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.body1.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = product.desc,
                style = MaterialTheme.typography.body2
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Rp. ${product.price.thousand()}",
                style = MaterialTheme.typography.body2.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Switch(
            modifier = Modifier
                .constrainAs(switch) {
                    linkTo(top = parent.top, bottom = variant.top)
                    end.linkTo(parent.end)
                },
            checked = product.isActive,
            onCheckedChange = {
                onUpdateProduct(
                    product.copy(
                        isActive = it
                    )
                )
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colors.primary,
                uncheckedThumbColor = MaterialTheme.colors.primaryVariant
            )
        )

        Row(
            modifier = Modifier
                .clickable {
                    onVariantClicked()
                }
                .constrainAs(variant) {
                    linkTo(top = switch.bottom, bottom = parent.bottom)
                    end.linkTo(parent.end)
                },
        ) {
            val text = if (viewData.variantCount == 0) {
                stringResource(id = R.string.select_variant)
            } else {
                "${viewData.variantCount} " + stringResource(id = R.string.variant)
            }

            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically),
                text = text,
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
}
