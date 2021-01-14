package com.sosialite.solite_pos.data.source.local.entity.helper

import android.widget.RadioButton
import com.sosialite.solite_pos.data.source.local.entity.room.master.VariantOption

data class OptionWithRadioButton(
	var variantOption: VariantOption,
	var radioButton: RadioButton
)
