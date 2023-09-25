package com.socialite.solite_pos.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.socialite.domain.menu.UserAuthority
import com.socialite.solite_pos.R
import com.socialite.solite_pos.schema.User
import com.socialite.solite_pos.view.ui.theme.SolitePOSTheme

@Composable
fun TopBarUserDetail(
    modifier: Modifier = Modifier,
    user: User?,
    storeName: String?
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colors.primary)
    ) {
        Surface(
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(
                topEnd = 16.dp,
                topStart = 16.dp
            ),
            color = MaterialTheme.colors.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = user?.name.orEmpty(),
                            style = MaterialTheme.typography.h5
                        )
                        Text(
                            text = user?.email.orEmpty(),
                            style = MaterialTheme.typography.body1
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = user?.authority?.name.orEmpty(),
                        style = MaterialTheme.typography.body1,
                        fontWeight = FontWeight.Black
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(16.dp))

                if (storeName != null) {
                    val text = stringResource(R.string.currently_you_are_at, storeName)
                    val start = text.indexOf(storeName)
                    val spanStyles = listOf(
                        AnnotatedString.Range(
                            SpanStyle(fontWeight = FontWeight.Black),
                            start = start,
                            end = text.length
                        )
                    )
                    Text(
                        text = AnnotatedString(text, spanStyles),
                        style = MaterialTheme.typography.body1,
                    )
                } else {
                    Text(
                        text = stringResource(R.string.you_are_not_pick_a_store_yet),
                        style = MaterialTheme.typography.body1,
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
private fun Preview() {
    SolitePOSTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            TopBarUserDetail(
                modifier = Modifier.align(Alignment.TopCenter),
                user = User(
                    name = "Denis Yordan",
                    email = "denis.yordan.p@gmail.com",
                    authority = UserAuthority.OWNER
                ),
                storeName = "Jajanan Sosialita"
            )
        }
    }
}