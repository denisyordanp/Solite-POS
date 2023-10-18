package com.socialite.solite_pos.feature.customerorder.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.socialite.common.ui.component.PrimaryButton
import com.socialite.common.ui.component.SoliteContent
import com.socialite.core.ui.extension.paddings
import com.socialite.core.ui.extension.round12
import com.socialite.core.ui.extension.round8
import com.socialite.core.ui.extension.size10Normal
import com.socialite.core.ui.extension.size12Normal
import com.socialite.core.ui.extension.size14Bold
import com.socialite.core.ui.extension.size14SemiBold
import com.socialite.core.ui.extension.size20Bold
import com.socialite.feature.customerorder.R
import com.socialite.schema.ui.dummy.DummySchema
import com.socialite.schema.ui.helper.ProductWithCategory
import com.socialite.schema.ui.helper.VariantWithOptions
import com.socialite.solite_pos.feature.customerorder.component.CheckedIcon
import com.socialite.solite_pos.feature.customerorder.component.CircleButtonIcon
import com.socialite.solite_pos.feature.customerorder.component.ProductDetail

@Composable
fun SelectProductVariantBottomSheet(
    modifier: Modifier = Modifier,
    productWithCategory: ProductWithCategory,
    variants: List<VariantWithOptions>,
    currentAmount: Int
) {
    Column {
        Column(
            modifier = modifier
                .padding(horizontal = MaterialTheme.paddings.medium)
        ) {
            ProductDetail(productWithCategory = productWithCategory)
            Spacer(modifier = Modifier.height(MaterialTheme.paddings.medium))
            LazyColumn {
                items(variants) { variant ->
                    VariantOptionsItem(variantWithOptions = variant)
                }
            }
            Spacer(modifier = Modifier.height(MaterialTheme.paddings.smallMedium))
            TotalText(
                modifier = Modifier
                    .align(Alignment.End)
            )
            Spacer(modifier = Modifier.height(MaterialTheme.paddings.small))
        }
        BottomButtons(
            currentAmount = currentAmount,
            onAddCLick = { /*TODO*/ },
            onReduceCLick = { /*TODO*/ }
        )
    }
}

@Composable
private fun VariantOptionsItem(
    variantWithOptions: VariantWithOptions
) {
    Column {
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = variantWithOptions.variant.name,
                style = MaterialTheme.typography.size14SemiBold
            )
            Spacer(modifier = Modifier.width(MaterialTheme.paddings.extraSmall))

            val optionTypeText = if (variantWithOptions.variant.isMust == true)
                stringResource(R.string.mandatory_option)
            else
                stringResource(R.string.optional_option)
            Text(
                text = optionTypeText,
                style = MaterialTheme.typography.size10Normal
            )
        }
        Spacer(modifier = Modifier.height(MaterialTheme.paddings.small))
        Column(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colors.surface,
                    shape = MaterialTheme.shapes.round12
                )
                .padding(MaterialTheme.paddings.medium),
        ) {
            variantWithOptions.options.forEach { option ->
                Row(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.round8)
                        .clickable { }
                        .padding(MaterialTheme.paddings.extraMediumSmall),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .weight(1f),
                        text = option.name,
                        style = MaterialTheme.typography.size14SemiBold,
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.width(MaterialTheme.paddings.extraSmall))
                    CheckedIcon(isChecked = false)
                }
            }
        }
        Spacer(modifier = Modifier.height(MaterialTheme.paddings.extraSmall))
        Text(
            text = stringResource(R.string.this_variant_must_be_selected),
            style = MaterialTheme.typography.size10Normal,
            color = MaterialTheme.colors.error
        )
        Spacer(modifier = Modifier.height(MaterialTheme.paddings.smallMedium))
    }
}

@Composable
private fun TotalText(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Total",
            style = MaterialTheme.typography.size12Normal
        )
        Spacer(modifier = Modifier.width(MaterialTheme.paddings.extraSmall))
        Text(
            text = "IDR 30K", // TODO: replace with product price times amount
            style = MaterialTheme.typography.size14Bold
        )
    }
}

@Composable
private fun BottomButtons(
    currentAmount: Int,
    onAddCLick: () -> Unit,
    onReduceCLick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .padding(MaterialTheme.paddings.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircleButtonIcon(
            iconResource = R.drawable.ic_minus,
            backgroundColor = MaterialTheme.colors.secondary,
            iconColor = MaterialTheme.colors.background,
            onCLick = onReduceCLick
        )
        Spacer(modifier = Modifier.width(MaterialTheme.paddings.medium))
        Text(
            text = currentAmount.toString(),
            style = MaterialTheme.typography.size20Bold
        )
        Spacer(modifier = Modifier.width(MaterialTheme.paddings.medium))
        CircleButtonIcon(
            iconResource = R.drawable.ic_plus,
            backgroundColor = MaterialTheme.colors.primary,
            iconColor = MaterialTheme.colors.onPrimary,
            onCLick = onAddCLick
        )
        Spacer(modifier = Modifier.width(MaterialTheme.paddings.large))
        PrimaryButton(
            modifier = Modifier
                .weight(1f),
            text = stringResource(R.string.adding_button_title),
            onClick = {

            }
        )
    }
}

@Preview
@Composable
private fun Preview() {
    SoliteContent {
        SelectProductVariantBottomSheet(
            modifier = it,
            productWithCategory = DummySchema.productWithCategories.first(),
            variants = DummySchema.variantWithOptions.take(2),
            currentAmount = 10
        )
    }
}