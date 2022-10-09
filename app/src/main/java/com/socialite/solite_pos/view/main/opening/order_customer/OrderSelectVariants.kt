package com.socialite.solite_pos.view.main.opening.order_customer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.socialite.solite_pos.R
import com.socialite.solite_pos.compose.PrimaryButton
import com.socialite.solite_pos.view.main.opening.ui.theme.SolitePOSTheme

@Composable
fun OrderSelectVariants(
    onBackClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            ProductTitle(onBackClicked = onBackClicked)
        },
        bottomBar = {
            AddToCartBottom()
        },
        backgroundColor = MaterialTheme.colors.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            VariantItem()
            VariantItem()
        }
    }
}

@Composable
private fun ProductTitle(
    onBackClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colors.primary)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .clickable { onBackClicked() }
                .padding(start = 8.dp)
                .padding(8.dp),
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = null,
            tint = Color.Black
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            text = "Dimsum Pedas sangat - Rp. 10.000",
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onPrimary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}

@Composable
private fun VariantItem() {
    Spacer(modifier = Modifier.height(4.dp))
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        VariantTitle()
        VariantOption()
        VariantOption()
        VariantOption()
    }
}

@Composable
private fun VariantTitle() {
    Text(
        text = "Pilihan Level pedas",
        style = MaterialTheme.typography.body1
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
        text = "Pilihan tidak wajib",
        style = MaterialTheme.typography.overline
    )
    Spacer(modifier = Modifier.height(8.dp))
    Spacer(
        modifier = Modifier
            .height(0.5.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colors.onPrimary)
    )
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun VariantOption() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val (name, price, checkBox) = createRefs()
        Text(
            modifier = Modifier
                .constrainAs(name) {
                    linkTo(start = parent.start, end = price.start)
                    linkTo(top = parent.top, bottom = parent.bottom)
                    width = Dimension.fillToConstraints
                }
                .fillMaxWidth(),
            text = "Bubble hitam",
            style = MaterialTheme.typography.body2
        )
        Text(
            modifier = Modifier
                .constrainAs(price) {
                    end.linkTo(checkBox.start)
                    linkTo(top = parent.top, bottom = parent.bottom)
                },
            text = "+Rp. 3000",
            style = MaterialTheme.typography.body2.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Checkbox(
            modifier = Modifier
                .constrainAs(checkBox) {
                    end.linkTo(parent.end)
                    linkTo(top = parent.top, bottom = parent.bottom)
                },
            checked = false,
            onCheckedChange = {}
        )
    }
}

@Composable
private fun AddToCartBottom() {
    ConstraintLayout(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .padding(16.dp)
    ) {
        val (text, qty, addCartBtn) = createRefs()
        Text(
            modifier = Modifier
                .constrainAs(text) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                },
            text = "Banyaknya"
        )
        Row(
            modifier = Modifier
                .constrainAs(qty) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_remove_circle),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "1",
                style = MaterialTheme.typography.body1.copy(
                    fontWeight = FontWeight.Black
                )
            )
            Spacer(modifier = Modifier.width(16.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_add_circle),
                contentDescription = null,
            )
        }
        PrimaryButton(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .constrainAs(addCartBtn) {
                    linkTo(start = parent.start, end = parent.end)
                    linkTo(
                        bottom = parent.bottom,
                        top = qty.bottom,
                        topMargin = 16.dp,
                    )
                    width = Dimension.fillToConstraints
                }
                .fillMaxWidth(),
            buttonText = "Tambahkan - Rp. 30.000",
            onClick = {}
        )
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
private fun Preview() {
    SolitePOSTheme {
        OrderSelectVariants() {

        }
    }
}
