package com.socialite.solite_pos.view.main.opening

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.socialite.solite_pos.R
import com.socialite.solite_pos.compose.ProductCustomerItem
import com.socialite.solite_pos.data.source.local.entity.room.helper.ProductWithCategory
import com.socialite.solite_pos.view.main.opening.ui.theme.SolitePOSTheme
import com.socialite.solite_pos.view.viewModel.ProductViewModel

class OrderCustomerActivity : AppCompatActivity() {

    private lateinit var viewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ProductViewModel.getMainViewModel(this)
        setContent {
            SolitePOSTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    val state = viewModel.getProducts(1).collectAsState(initial = null)

                    OrderList(products = state.value)
                }
            }
        }
    }
}

@Composable
private fun OrderList(products: List<ProductWithCategory>?) {
    products?.let {
        ConstraintLayout {
            val (content, cart, menu) = createRefs()
            LazyColumn(
                modifier = Modifier
                    .constrainAs(content) {
                        linkTo(start = parent.start, end = parent.end)
                        linkTo(bottom = parent.bottom, top = parent.top, bias = 0f)
                    }
            ) {
                items(it) { product ->
                    ProductCustomerItem(
                        titleText = product.product.name,
                        subTitleText = product.product.desc,
                        priceText = product.product.sellPrice,
                        imageUrl = product.product.image
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
                    .background(
                        color = MaterialTheme.colors.primary,
                        shape = RoundedCornerShape(25.dp)
                    ).padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                val (desc, price) = createRefs()
                Column(
                    modifier = Modifier
                        .constrainAs(desc) {
                            linkTo(top = parent.top, bottom = parent.bottom)
                            linkTo(start = parent.start, end = price.start, endMargin = 16.dp)
                            width = Dimension.fillToConstraints
                        }.fillMaxWidth()
                ) {
                    Text(
                        text = "3 Item",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Chocho banana, kopi sweet, madu TJ, roti baka",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.overline
                    )
                }
                Text(
                    modifier = Modifier
                        .constrainAs(price) {
                                            linkTo(top = parent.top, bottom = parent.bottom)
                            end.linkTo(parent.end)
                        },
                    text = "IDR 35K",
                    style = MaterialTheme.typography.h6
                )
            }
            Button(
                modifier = Modifier
                    .size(50.dp)
                    .constrainAs(menu) {
                        end.linkTo(parent.end, margin = 24.dp)
                        bottom.linkTo(parent.bottom, margin = 24.dp)
                    },
                shape = CircleShape,
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 4.dp
                ),
                onClick = { }
            ) {
                Image(painter = painterResource(id = R.drawable.ic_add), contentDescription = null)
            }
        }
    }
}
