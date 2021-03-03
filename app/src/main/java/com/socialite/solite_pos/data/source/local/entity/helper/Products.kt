package com.socialite.solite_pos.data.source.local.entity.helper

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.socialite.solite_pos.data.source.local.entity.room.master.Category
import com.socialite.solite_pos.data.source.local.entity.room.master.Product

data class Products(
        var category: Category?,
        var product: Product?,
        var variants: LiveData<List<VariantWithOptions>?>
){
    constructor(): this(null,null, MutableLiveData())
}
