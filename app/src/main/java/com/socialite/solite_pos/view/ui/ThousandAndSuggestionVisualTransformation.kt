package com.socialite.solite_pos.view.ui

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.socialite.core.extensions.thousand

// TODO: Need improvement on when adding suggestion offset
class ThousandAndSuggestionVisualTransformation(
    val isSuggestion: Boolean
) : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {

        val originalText = text.text
        val formattedText = originalText.toLongOrNull()?.thousand() ?: ""
        val commas = formattedText.count { it == '.' || it == ',' }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val increaseOffset = if (isSuggestion) {
                    (originalText.length - offset) + commas
                } else {
                    commas
                }
                return offset + increaseOffset
            }

            override fun transformedToOriginal(offset: Int): Int {
                return offset - commas
            }
        }

        return TransformedText(
            text = AnnotatedString(formattedText),
            offsetMapping = offsetMapping
        )
    }
}
