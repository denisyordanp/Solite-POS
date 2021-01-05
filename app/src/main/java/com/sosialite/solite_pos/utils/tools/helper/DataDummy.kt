package com.sosialite.solite_pos.utils.tools.helper

import com.sosialite.solite_pos.data.source.local.entity.main.Category
import com.sosialite.solite_pos.data.source.local.entity.main.Customer
import com.sosialite.solite_pos.data.source.local.entity.main.Product
import com.sosialite.solite_pos.data.source.local.entity.helper.DetailOrder
import com.sosialite.solite_pos.data.source.local.entity.helper.Order
import com.sosialite.solite_pos.utils.config.MainConfig
import java.util.*
import kotlin.collections.ArrayList

class DataDummy {
	class DataCategory{
		companion object{
			var promo = Category(1, "PROMO")
			var dimsum = Category(2, "Dimsum")
			var mix = Category(3, "Mix Variant")
			var minuman = Category(4, "Minuman")
			var extra = Category(5, "Extra")

			val allCategory: ArrayList<Category>
				get() {
					val array: ArrayList<Category> = ArrayList()
					array.add(dimsum)
					array.add(mix)
					array.add(minuman)
					array.add(extra)
					array.add(promo)
					return array
				}
		}
	}
	class DataProduct{
		companion object{
//			DIMSUM
			var kAyam = Product(1, "Kulit Tahu Ayam", DataCategory.dimsum.id, "Kulit Tahu Ayam", 14000, 10)
			var kUdang = Product(2, "Kulit Tahu Udang",DataCategory.dimsum.id, "Kulit Tahu Udang", 14000, 10)
			var sAyam = Product(3, "Siomay Ayam", DataCategory.dimsum.id, "Siomay Ayam", 14000, 10)
			var sUdang = Product(4, "Siomay Udang",DataCategory.dimsum.id, "Siomay Udang", 14000, 10)
			var sKepiting = Product(5, "Siomay Kepiting",DataCategory.dimsum.id, "Siomay Udang", 14000, 10)
			var sSeafood = Product(6, "Siomay Seafood",DataCategory.dimsum.id, "Siomay Seafood", 14000, 10)
			var ekado = Product(7, "Ekado Goreng",DataCategory.dimsum.id, "Ekado Goreng", 14000, 10)
			var nori = Product(8, "Nori Ayam Udang",DataCategory.dimsum.id, "Nori Ayam Udang", 14000, 10)
			var hakau = Product(9, "Hakau",DataCategory.dimsum.id, "Hakau", 15000, 10)
			var hisit = Product(10, "Hisit Kau",DataCategory.dimsum.id, "Hisit Kau", 15000, 10)
			var lAyam = Product(11, "Lumpia Ayam Goreng",DataCategory.dimsum.id, "Lumpia Ayam Goreng", 14000, 10)
			var lUdang = Product(12, "Lumpia Udang Goreng",DataCategory.dimsum.id, "Lumpia Udang Goreng", 14000, 10)
			var kwotie = Product(13, "Kwotie Goreng",DataCategory.dimsum.id, "Kwotie Goreng", 15000, 10)
			var angsio = Product(14, "Angsio Ceker Ayam",DataCategory.dimsum.id, "Ceker Ayam", 15000, 10)

//			MIX
			var small = Product(15, "Small Mix Variant",DataCategory.mix.id, "Ceker Ayam", 18000, 10)
			var regular = Product(16, "Regular Mix Variant",DataCategory.mix.id, "Ceker Ayam", 35000, 10)
			var big = Product(17, "Big Mix Variant",DataCategory.mix.id, "Ceker Ayam", 53000, 10)
			var party = Product(18, "Party Pack Mix Variant",DataCategory.mix.id, "Ceker Ayam", 105000, 10)

//			EXTRA
			var chili = Product(19, "Extra Chili Oil",DataCategory.extra.id, "Ceker Ayam", 2000, 10)
			var mayo = Product(20, "Extra Mayonnaise",DataCategory.extra.id, "Ceker Ayam", 2000, 10)
			var chilsos = Product(21, "Chili Oil Sosialita",DataCategory.extra.id, "Ceker Ayam", 15000, 10)

//			MINUMAN
			var yLemon = Product(22, "Ice Lemonade Yakult", DataCategory.minuman.id, "Ceker Ayam", 10000, 10)
			var yMelon = Product(23, "Ice Melon Yakult", DataCategory.minuman.id, "Ceker Ayam", 10000, 10)
			var yStrw = Product(24, "Ice Strawberry Yakult", DataCategory.minuman.id, "Ceker Ayam", 10000, 10)
			var yLychee = Product(25, "Ice Lychee Yakult", DataCategory.minuman.id, "Ceker Ayam", 10000, 10)
			var yMarkisa = Product(26, "Ice Markisa Yakult", DataCategory.minuman.id, "Ceker Ayam", 10000, 10)

			var sLemon = Product(27, "Ice Lemonade Squash", DataCategory.minuman.id, "Ceker Ayam", 10000, 10)
			var sMelon = Product(28, "Ice Melon Squash", DataCategory.minuman.id, "Ceker Ayam", 10000, 10)
			var sStrw = Product(29, "Ice Strawberry Squash", DataCategory.minuman.id, "Ceker Ayam", 10000, 10)
			var sLychee = Product(30, "Ice Lychee Squash", DataCategory.minuman.id, "Ceker Ayam", 10000, 10)
			var sMarkisa = Product(31, "Ice Markisa Squash", DataCategory.minuman.id, "Ceker Ayam", 10000, 10)

			var tea = Product(32, "Teh Manis", DataCategory.minuman.id, "Ceker Ayam", 5000, 10)
			var teaM = Product(33, "Teh Tawar", DataCategory.minuman.id, "Ceker Ayam", 4000, 10)
			var jerukNipis = Product(34, "Jeruk Nipis", DataCategory.minuman.id, "Ceker Ayam", 7000, 10)
			var jeruk = Product(35, "Jeruk", DataCategory.minuman.id, "Ceker Ayam", 7000, 10)
			var lemonTea = Product(36, "Lemon Tea", DataCategory.minuman.id, "Ceker Ayam", 8000, 10)
			var minral = Product(37, "Air Mineral", DataCategory.minuman.id, "Ceker Ayam", 4000, 10)

			fun getDetailProduct(category: Category?): ArrayList<DetailOrder>{
				val items: ArrayList<DetailOrder> = ArrayList()
				if (category != null){
					for (prod in allDetailProduct){
						if (prod.product != null){
							if (prod.product!!.category == category.id){
								items.add(prod)
							}
						}
					}
				}
				return items
			}

			private var allDetailProduct: ArrayList<DetailOrder> = ArrayList()
			get() {
				val array: ArrayList<DetailOrder> = ArrayList()
				array.add(DetailOrder(kAyam))
				array.add(DetailOrder(kUdang))
				array.add(DetailOrder(sAyam))
				array.add(DetailOrder(sUdang))
				array.add(DetailOrder(sKepiting))
				array.add(DetailOrder(sSeafood))
				array.add(DetailOrder(ekado))
				array.add(DetailOrder(nori))
				array.add(DetailOrder(hakau))
				array.add(DetailOrder(hisit))
				array.add(DetailOrder(lAyam))
				array.add(DetailOrder(lUdang))
				array.add(DetailOrder(kwotie))
				array.add(DetailOrder(angsio))

				array.add(DetailOrder(small))
				array.add(DetailOrder(regular))
				array.add(DetailOrder(big))
				array.add(DetailOrder(party))

				array.add(DetailOrder(chili))
				array.add(DetailOrder(mayo))
				array.add(DetailOrder(chilsos))

				array.add(DetailOrder(yLemon))
				array.add(DetailOrder(yMelon))
				array.add(DetailOrder(yStrw))
				array.add(DetailOrder(yLychee))
				array.add(DetailOrder(yMarkisa))
				array.add(DetailOrder(sLemon))
				array.add(DetailOrder(sMelon))
				array.add(DetailOrder(sStrw))
				array.add(DetailOrder(sLychee))
				array.add(DetailOrder(sMarkisa))
				array.add(DetailOrder(tea))
				array.add(DetailOrder(teaM))
				array.add(DetailOrder(jerukNipis))
				array.add(DetailOrder(jeruk))
				array.add(DetailOrder(lemonTea))
				array.add(DetailOrder(minral))
				return array
			}
		}
	}

	class DataOrder{
		companion object{

			var dummyProduct: ArrayList<DetailOrder> = ArrayList()
			get() {
				val array: ArrayList<DetailOrder> = ArrayList()
				array.add(DetailOrder(DataProduct.kAyam, 1))
				array.add(DetailOrder(DataProduct.sUdang, 2))
				array.add(DetailOrder(DataProduct.hisit, 1))
				array.add(DetailOrder(DataProduct.jerukNipis, 2))
				return array
			}

			fun getDone(): ArrayList<Order>{
				val items: ArrayList<Order> = ArrayList()
				items.add(Order(Customer(23, "Denis"), 6545646, dummyProduct, MainConfig.currentTime, Calendar.getInstance(), 75000, Order.DONE))
				items.add(Order(Customer(23, "Denis"), 6545646, dummyProduct, MainConfig.currentTime, Calendar.getInstance(), 80000, Order.DONE))
				items.add(Order(Customer(23, "Denis"), 6545646, dummyProduct, MainConfig.currentTime, Calendar.getInstance(), 100000, Order.DONE))
				return items
			}

			fun getPay(): ArrayList<Order>{
				val items: ArrayList<Order> = ArrayList()
				items.add(Order(Customer(22, "Evaviliya"), 54165, dummyProduct, MainConfig.currentTime, Calendar.getInstance(), 0, Order.NEED_PAY))
				items.add(Order(Customer(22, "Evaviliya"), 54165, dummyProduct, MainConfig.currentTime, Calendar.getInstance(), 0, Order.NEED_PAY))
				items.add(Order(Customer(22, "Evaviliya"), 54165, dummyProduct, MainConfig.currentTime, Calendar.getInstance(), 0, Order.NEED_PAY))
				items.add(Order(Customer(22, "Evaviliya"), 54165, dummyProduct, MainConfig.currentTime, Calendar.getInstance(), 0, Order.NEED_PAY))
				return items
			}

			fun getProcess(): ArrayList<Order>{
				val items: ArrayList<Order> = ArrayList()
				items.add(Order(Customer(21, "Linda"), 123, dummyProduct, MainConfig.currentTime, Calendar.getInstance(), 0, Order.ON_PROCESS))
				items.add(Order(Customer(21, "Linda"), 124, dummyProduct, MainConfig.currentTime, Calendar.getInstance(), 0, Order.ON_PROCESS))
				items.add(Order(Customer(21, "Linda"), 125, dummyProduct, MainConfig.currentTime, null, 0, Order.ON_PROCESS))
				items.add(Order(Customer(21, "Linda"), 126, dummyProduct, MainConfig.currentTime, null, 0, Order.ON_PROCESS))
				items.add(Order(Customer(21, "Linda"), 127, dummyProduct, MainConfig.currentTime, null, 0, Order.ON_PROCESS))
				items.add(Order(Customer(21, "Linda"), 128, dummyProduct, MainConfig.currentTime, null, 0, Order.ON_PROCESS))
				return items
			}
		}
	}
}
