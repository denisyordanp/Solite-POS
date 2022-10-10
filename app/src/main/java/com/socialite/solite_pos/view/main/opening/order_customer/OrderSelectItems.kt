package com.socialite.solite_pos.view.main.opening.order_customer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.socialite.solite_pos.compose.BucketView
import com.socialite.solite_pos.compose.GeneralMenuButtonView
import com.socialite.solite_pos.compose.GeneralMenusView
import com.socialite.solite_pos.compose.ProductCustomerItemView
import com.socialite.solite_pos.data.source.local.entity.room.helper.ProductWithCategory
import com.socialite.solite_pos.view.main.opening.ui.GeneralMenus
import com.socialite.solite_pos.view.main.opening.ui.ModalContent
import com.socialite.solite_pos.view.viewModel.ProductViewModel
import kotlinx.coroutines.launch

@Composable
@ExperimentalMaterialApi
fun OrderSelectItems(
    viewModel: ProductViewModel,
    onItemClicked: () -> Unit,
    onClickOrder: () -> Unit,
    onGeneralMenuClicked: (menu: GeneralMenus) -> Unit
) {
    val state = viewModel.getProducts(1).collectAsState(initial = null)

    var modalContent by remember {
        mutableStateOf(ModalContent.BUCKET_VIEW)
    }
    val modalState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = modalState,
        sheetShape = RoundedCornerShape(
            topStart = 8.dp,
            topEnd = 8.dp
        ),
        sheetContent = {
            when (modalContent) {
                ModalContent.BUCKET_VIEW -> BucketView(
                    onClickOrder = onClickOrder
                )

                ModalContent.GENERAL_MENUS -> GeneralMenusView(
                    onClicked = {
                        if (it == GeneralMenus.NEW_ORDER) {
                            scope.launch {
                                modalState.hide()
                            }
                        } else {
                            onGeneralMenuClicked(it)
                        }
                    }
                )

                else -> {
                    // Do nothing
                }
            }
        },
        content = {
            OrderList(
                products = state.value,
                onBucketClicked = {
                    modalContent = ModalContent.BUCKET_VIEW
                    scope.launch {
                        modalState.show()
                    }
                },
                onItemClicked = onItemClicked,
                onMenusClicked = {
                    modalContent = ModalContent.GENERAL_MENUS
                    scope.launch {
                        modalState.show()
                    }
                }
            )
        }
    )
}

@Composable
private fun OrderList(
    products: List<ProductWithCategory>?,
    onBucketClicked: () -> Unit,
    onItemClicked: () -> Unit,
    onMenusClicked: () -> Unit,
) {
    products?.let {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.background)
        ) {
            val (content, cart, menu) = createRefs()
            LazyColumn(
                modifier = Modifier
                    .constrainAs(content) {
                        linkTo(start = parent.start, end = parent.end)
                        linkTo(bottom = parent.bottom, top = parent.top, bias = 0f)
                    }
            ) {
                items(it) { product ->
                    ProductCustomerItemView(
                        titleText = product.product.name,
                        subTitleText = product.product.desc,
                        priceText = product.product.sellPrice,
                        imageUrl = product.product.image,
                        onItemClicked = onItemClicked
                    )
                }
            }
            ConstraintLayout(
                modifier = Modifier
                    .constrainAs(cart) {
                        linkTo(
                            start = parent.start,
                            end = menu.start,
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
                        text = "3 Item",
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onPrimary
                    )
                    Text(
                        text = "Chocho banana, kopi sweet, madu TJ, roti baka",
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
                    text = "IDR 35K",
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onPrimary
                )
            }
            GeneralMenuButtonView(
                modifier = Modifier
                    .constrainAs(menu) {
                        end.linkTo(parent.end, margin = 24.dp)
                        bottom.linkTo(parent.bottom, margin = 24.dp)
                    },
                onMenuClicked = onMenusClicked
            )
        }
    }
}
