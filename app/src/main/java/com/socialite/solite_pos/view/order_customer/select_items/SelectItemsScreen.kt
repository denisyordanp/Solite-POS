package com.socialite.solite_pos.view.order_customer.select_items

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import com.socialite.solite_pos.R
import com.socialite.solite_pos.compose.BasicAlertDialog
import com.socialite.solite_pos.compose.BasicTopBar
import com.socialite.solite_pos.compose.BucketView
import com.socialite.solite_pos.compose.GeneralMenuButtonView
import com.socialite.solite_pos.compose.GeneralMenusView
import com.socialite.solite_pos.compose.SpaceForFloatingButton
import com.socialite.solite_pos.data.source.local.entity.helper.BucketOrder
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.helper.ProductWithCategory
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Category
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Product
import com.socialite.solite_pos.utils.config.DateUtils
import com.socialite.solite_pos.utils.config.toIDR
import com.socialite.solite_pos.view.order_customer.components.ProductCustomerItemView
import com.socialite.solite_pos.view.ui.GeneralMenus
import com.socialite.solite_pos.view.ui.ModalContent
import kotlinx.coroutines.launch

@Composable
@ExperimentalMaterialApi
fun SelectItemsScreen(
    currentViewModel: SelectItemsViewModel = viewModel(
        factory = SelectItemsViewModel.getFactory(
            LocalContext.current
        )
    ),
    bucketOrder: BucketOrder,
    onItemClick: (product: Product, isAdd: Boolean, hasVariant: Boolean) -> Unit,
    onClickOrder: () -> Unit,
    onGeneralMenuClicked: ((menu: GeneralMenus) -> Unit)? = null,
    onBackClicked: (() -> Unit)? = null,
    onRemoveProduct: (detail: ProductOrderDetail) -> Unit
) {
    val scope = rememberCoroutineScope()
    val isEditOrder = onGeneralMenuClicked == null
    var modalContent by remember {
        mutableStateOf(ModalContent.BUCKET_VIEW)
    }
    val modalState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    var alertEditOrder by remember { mutableStateOf(false) }

    ModalBottomSheetLayout(
        sheetState = modalState,
        sheetShape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp
        ),
        sheetContent = {
            when (modalContent) {
                ModalContent.BUCKET_VIEW -> BucketView(
                    bucketOrder = bucketOrder,
                    onClickOrder = {
                        if (isEditOrder) {
                            alertEditOrder = true
                        } else {
                            onClickOrder()
                        }
                    },
                    isEditOrder = isEditOrder,
                    onClearBucket = {
                        scope.launch {
                            modalState.hide()
                        }
                    },
                    onRemoveProduct = onRemoveProduct
                )

                ModalContent.GENERAL_MENUS -> {
                    val badges = currentViewModel.getBadges(DateUtils.currentDate).collectAsState(
                        initial = emptyList()
                    )
                    GeneralMenusView(
                        badges = badges.value,
                        onClicked = {
                            if (it == GeneralMenus.NEW_ORDER) {
                                scope.launch {
                                    modalState.hide()
                                }
                            } else {
                                onGeneralMenuClicked?.invoke(it)
                            }
                        }
                    )
                }

                else -> {
                    // Do nothing
                }
            }
        },
        content = {
            val products = currentViewModel.getAllProducts().collectAsState(initial = emptyMap())
            if (isEditOrder) {
                Scaffold(
                    topBar = {
                        BasicTopBar(
                            titleText = stringResource(R.string.edit_order),
                            onBackClicked = {
                                onBackClicked?.invoke()
                            }
                        )
                    },
                    content = { padding ->
                        ProductOrderList(
                            modifier = Modifier
                                .padding(padding),
                            categoryWithProducts = products.value,
                            bucketOrder = bucketOrder,
                            onBucketClicked = {
                                modalContent = ModalContent.BUCKET_VIEW
                                scope.launch {
                                    modalState.show()
                                }
                            },
                            onItemClick = onItemClick,
                        )
                    }
                )
            } else {
                ProductOrderList(
                    categoryWithProducts = products.value,
                    bucketOrder = bucketOrder,
                    onBucketClicked = {
                        modalContent = ModalContent.BUCKET_VIEW
                        scope.launch {
                            modalState.show()
                        }
                    },
                    onItemClick = onItemClick,
                    onMenusClicked = {
                        modalContent = ModalContent.GENERAL_MENUS
                        scope.launch {
                            modalState.show()
                        }
                    }
                )
            }
        }
    )

    if (alertEditOrder) {
        BasicAlertDialog(
            titleText = stringResource(R.string.edit_order),
            descText = stringResource(R.string.are_you_sure_edit_this_order_products),
            positiveAction = {
                onClickOrder()
                alertEditOrder = false
            },
            positiveText = stringResource(R.string.yes),
            negativeAction = {
                alertEditOrder = false
            },
            negativeText = stringResource(R.string.no)
        )
    }
}

@Composable
private fun ProductOrderList(
    modifier: Modifier = Modifier,
    bucketOrder: BucketOrder,
    categoryWithProducts: Map<Category, List<ProductWithCategory>>,
    onBucketClicked: () -> Unit,
    onItemClick: (product: Product, isAdd: Boolean, hasVariant: Boolean) -> Unit,
    onMenusClicked: (() -> Unit)? = null,
) {
    val isEditOrder = onMenusClicked == null
    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
    ) {
        val (content, cart, menu) = createRefs()
        val listState = rememberLazyListState()

        LazyColumn(
            modifier = Modifier
                .constrainAs(content) {
                    linkTo(start = parent.start, end = parent.end)
                    linkTo(bottom = parent.bottom, top = parent.top, bias = 0f)
                },
            state = listState
        ) {

            categoryWithProducts.forEach { categoryWithProduct ->

                item {
                    CategoryWithProducts(
                        categoryWithProducts = categoryWithProduct,
                        bucketOrder = bucketOrder,
                        onItemClick = onItemClick
                    )
                }
            }

            item { SpaceForFloatingButton() }
        }

        if (bucketOrder != BucketOrder.idle()) {
            ConstraintLayout(
                modifier = Modifier
                    .constrainAs(cart) {
                        linkTo(
                            start = parent.start,
                            end = if (isEditOrder) parent.end else menu.start,
                            endMargin = 16.dp,
                            startMargin = 24.dp
                        )
                        bottom.linkTo(parent.bottom, margin = 24.dp)
                        width = Dimension.fillToConstraints
                    }
                    .fillMaxWidth()
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(25.dp)
                    )
                    .clickable { onBucketClicked() }
                    .background(
                        color = MaterialTheme.colors.primary,
                        shape = RoundedCornerShape(25.dp)
                    )
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                val (desc, price) = createRefs()
                Column(
                    modifier = Modifier
                        .constrainAs(desc) {
                            linkTo(top = parent.top, bottom = parent.bottom)
                            linkTo(start = parent.start, end = price.start, endMargin = 16.dp)
                            width = Dimension.fillToConstraints
                        }
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "${bucketOrder.totalItems()} item",
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onPrimary
                    )
                    Text(
                        text = bucketOrder.productAsString(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.overline,
                        color = MaterialTheme.colors.onPrimary
                    )
                }
                Text(
                    modifier = Modifier
                        .constrainAs(price) {
                            linkTo(top = parent.top, bottom = parent.bottom)
                            end.linkTo(parent.end)
                        },
                    text = bucketOrder.getTotal().toIDR(),
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onPrimary
                )
            }
        }

        if (!isEditOrder) {
            AnimatedVisibility(
                modifier = Modifier
                    .constrainAs(menu) {
                        end.linkTo(parent.end, margin = 24.dp)
                        bottom.linkTo(parent.bottom, margin = 24.dp)
                    },
                visible = !listState.isScrollInProgress,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                GeneralMenuButtonView(
                    onMenuClicked = onMenusClicked!!
                )
            }
        }
    }
}

@Composable
private fun CategoryWithProducts(
    categoryWithProducts: Map.Entry<Category, List<ProductWithCategory>>,
    bucketOrder: BucketOrder,
    onItemClick: (product: Product, isAdd: Boolean, hasVariant: Boolean) -> Unit
) {

    var isExpand by rememberSaveable {
        mutableStateOf(false)
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp)
                .clickable {
                    isExpand = !isExpand
                }
                .background(color = MaterialTheme.colors.surface)
                .padding(16.dp)
        ) {
            Text(
                modifier = Modifier
                    .weight(1f),
                text = categoryWithProducts.key.name,
                style = MaterialTheme.typography.body1.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Icon(
                painter = painterResource(
                    id = if (isExpand) R.drawable.ic_expand_less_24 else R.drawable.ic_expand_more_24
                ), contentDescription = null
            )
        }
        if (isExpand) {
            categoryWithProducts.value.forEach { productWithCategory ->
                ProductCustomerItemView(
                    productWithCategory = productWithCategory,
                    currentAmount = bucketOrder.getProductAmount(productWithCategory.product.id),
                    onItemClick = { isAdd, hasVariant ->
                        onItemClick(productWithCategory.product, isAdd, hasVariant)
                    }
                )
            }
        }
    }
}