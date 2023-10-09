package com.socialite.solite_pos.feature.customerorder.screen

import androidx.compose.foundation.Image
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import com.socialite.common.ui.component.MainBackground
import com.socialite.common.ui.component.SoliteContent
import com.socialite.common.ui.extension.contentSpace
import com.socialite.common.utility.constant.SuppressConstant
import com.socialite.core.ui.extension.body1Normal
import com.socialite.core.ui.extension.body2Bold
import com.socialite.core.ui.extension.defaultH5
import com.socialite.core.ui.extension.image
import com.socialite.core.ui.extension.item
import com.socialite.core.ui.extension.mainMenu
import com.socialite.core.ui.extension.overLineNormal
import com.socialite.core.ui.extension.paddings
import com.socialite.feature.customerorder.R
import com.socialite.schema.ui.main.Product
import com.socialite.solite_pos.feature.customerorder.component.SearchBar

@Composable
fun MainOrderScreen(
    mainStoreName: String,
    currentStoreName: String,
    products: List<Product>
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
                    products = products
                )
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

@Composable
private fun FavoriteMenuItem(product: Product) {
    val paddings = MaterialTheme.paddings

    ConstraintLayout(
        modifier = Modifier
            .size(
                width = 150.dp,
                height = 170.dp
            )
            .padding(end = paddings.smallMedium)
    ) {
        val (image, card, name, desc) = createRefs()

        Surface(
            modifier = Modifier
                .constrainAs(card) {
                    linkTo(start = parent.start, end = parent.end)
                    linkTo(top = parent.top, bottom = parent.bottom, topMargin = paddings.medium)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                },
            color = MaterialTheme.colors.surface,
            shape = MaterialTheme.shapes.item,
            content = {}
        )

        Surface(
            modifier = Modifier
                .height(80.dp)
                .constrainAs(image) {
                    linkTo(
                        start = parent.start,
                        end = parent.end,
                        startMargin = paddings.smallMedium,
                        endMargin = paddings.smallMedium
                    )
                    top.linkTo(parent.top)
                    width = Dimension.fillToConstraints
                },
            color = MaterialTheme.colors.onSurface,
            shape = MaterialTheme.shapes.image
        ) {
            Box {
                Image(
                    modifier = Modifier.align(Alignment.Center),
                    painter = painterResource(id = com.socialite.common.ui.R.drawable.ic_image_placeholder),
                    contentDescription = null
                )
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize(),
                    model = product.image,
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                )
            }
        }

        Text(
            modifier = Modifier
                .height(35.dp)
                .constrainAs(name) {
                    linkTo(
                        start = parent.start,
                        end = parent.end,
                        startMargin = paddings.smallMedium,
                        endMargin = paddings.smallMedium
                    )
                    width = Dimension.fillToConstraints
                    top.linkTo(image.bottom, margin = paddings.smallMedium)
                },
            text = product.name,
            style = MaterialTheme.typography.body2Bold,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier
                .constrainAs(desc) {
                    linkTo(
                        start = parent.start,
                        end = parent.end,
                        startMargin = paddings.smallMedium,
                        endMargin = paddings.smallMedium
                    )
                    width = Dimension.fillToConstraints
                    top.linkTo(name.bottom, margin = paddings.extraSmall)
                },
            text = product.desc,
            style = MaterialTheme.typography.overLineNormal,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
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
            )
        )
    }
}