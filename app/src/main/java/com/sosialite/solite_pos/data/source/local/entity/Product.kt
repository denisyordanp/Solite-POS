package com.sosialite.solite_pos.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import java.io.Serializable

@Entity(
	tableName = "Testing",
	primaryKeys = ["code"],
	indices = [Index(value = ["code"])])
data class Product(
	@ColumnInfo(name = "code")
	var code: String,

	@ColumnInfo(name = "name")
	var name: String,

	@ColumnInfo(name = "category")
	var category: String,

	@ColumnInfo(name = "desc")
	var desc: String,

	@ColumnInfo(name = "price")
	var price: Int
): Serializable
