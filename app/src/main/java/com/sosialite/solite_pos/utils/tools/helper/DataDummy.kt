//package com.sosialite.solite_pos.utils.tools.helper
//
//import com.sosialite.solite_pos.data.source.local.entity.room.master.Category
//import com.sosialite.solite_pos.data.source.local.entity.room.master.Product
//import kotlin.collections.ArrayList
//
//class DataDummy {
//	class DataCategory{
//		companion object{
//			var promo = Category(1, "PROMO", "desc", false)
//			var dimsum = Category(2, "Dimsum", "desc", false)
//			var mix = Category(3, "Mix Variant", "desc", false)
//			var minuman = Category(4, "Minuman", "desc", false)
//			var extra = Category(5, "Extra", "desc", false)
//
//			val allCategory: ArrayList<Category>
//				get() {
//					val array: ArrayList<Category> = ArrayList()
//					array.add(dimsum)
//					array.add(mix)
//					array.add(minuman)
//					array.add(extra)
//					array.add(promo)
//					return array
//				}
//		}
//	}
//	class DataProduct{
//		companion object{
////			DIMSUM
//			var kAyam = Product(1, "Kulit Tahu Ayam", DataCategory.dimsum.id, "Kulit Tahu Ayam", 14000, 10, true)
//			var kUdang = Product(2, "Kulit Tahu Udang",DataCategory.dimsum.id, "Kulit Tahu Udang", 14000, 10, true)
//			var sAyam = Product(3, "Siomay Ayam", DataCategory.dimsum.id, "Siomay Ayam", 14000, 10, true)
//			var sUdang = Product(4, "Siomay Udang",DataCategory.dimsum.id, "Siomay Udang", 14000, 10, true)
//			var sKepiting = Product(5, "Siomay Kepiting",DataCategory.dimsum.id, "Siomay Udang", 14000, 10, true)
//			var sSeafood = Product(6, "Siomay Seafood",DataCategory.dimsum.id, "Siomay Seafood", 14000, 10, true)
//			var ekado = Product(7, "Ekado Goreng",DataCategory.dimsum.id, "Ekado Goreng", 14000, 10, true)
//			var nori = Product(8, "Nori Ayam Udang",DataCategory.dimsum.id, "Nori Ayam Udang", 14000, 10, true)
//			var hakau = Product(9, "Hakau",DataCategory.dimsum.id, "Hakau", 15000, 10, true)
//			var hisit = Product(10, "Hisit Kau",DataCategory.dimsum.id, "Hisit Kau", 15000, 10, true)
//			var lAyam = Product(11, "Lumpia Ayam Goreng",DataCategory.dimsum.id, "Lumpia Ayam Goreng", 14000, 10, true)
//			var lUdang = Product(12, "Lumpia Udang Goreng",DataCategory.dimsum.id, "Lumpia Udang Goreng", 14000, 10, true)
//			var kwotie = Product(13, "Kwotie Goreng",DataCategory.dimsum.id, "Kwotie Goreng", 15000, 10, true)
//			var angsio = Product(14, "Angsio Ceker Ayam",DataCategory.dimsum.id, "Ceker Ayam", 15000, 10, true)
//
////			MIX
//			var small = Product(15, "Small Mix Variant",DataCategory.mix.id, "Ceker Ayam", 18000, 10, true)
//			var regular = Product(16, "Regular Mix Variant",DataCategory.mix.id, "Ceker Ayam", 35000, 10, true)
//			var big = Product(17, "Big Mix Variant",DataCategory.mix.id, "Ceker Ayam", 53000, 10, true)
//			var party = Product(18, "Party Pack Mix Variant",DataCategory.mix.id, "Ceker Ayam", 105000, 10, true)
//
////			EXTRA
//			var chili = Product(19, "Extra Chili Oil",DataCategory.extra.id, "Ceker Ayam", 2000, 10, true)
//			var mayo = Product(20, "Extra Mayonnaise",DataCategory.extra.id, "Ceker Ayam", 2000, 10, true)
//			var chilsos = Product(21, "Chili Oil Sosialita",DataCategory.extra.id, "Ceker Ayam", 15000, 10, true)
//
////			MINUMAN
//			var yLemon = Product(22, "Ice Lemonade Yakult", DataCategory.minuman.id, "Ceker Ayam", 10000, 10, true)
//			var yMelon = Product(23, "Ice Melon Yakult", DataCategory.minuman.id, "Ceker Ayam", 10000, 10, true)
//			var yStrw = Product(24, "Ice Strawberry Yakult", DataCategory.minuman.id, "Ceker Ayam", 10000, 10, true)
//			var yLychee = Product(25, "Ice Lychee Yakult", DataCategory.minuman.id, "Ceker Ayam", 10000, 10, true)
//			var yMarkisa = Product(26, "Ice Markisa Yakult", DataCategory.minuman.id, "Ceker Ayam", 10000, 10, true)
//
//			var sLemon = Product(27, "Ice Lemonade Squash", DataCategory.minuman.id, "Ceker Ayam", 10000, 10, true)
//			var sMelon = Product(28, "Ice Melon Squash", DataCategory.minuman.id, "Ceker Ayam", 10000, 10, true)
//			var sStrw = Product(29, "Ice Strawberry Squash", DataCategory.minuman.id, "Ceker Ayam", 10000, 10, true)
//			var sLychee = Product(30, "Ice Lychee Squash", DataCategory.minuman.id, "Ceker Ayam", 10000, 10, true)
//			var sMarkisa = Product(31, "Ice Markisa Squash", DataCategory.minuman.id, "Ceker Ayam", 10000, 10, true)
//
//			var tea = Product(32, "Teh Manis", DataCategory.minuman.id, "Ceker Ayam", 5000, 10, true)
//			var teaM = Product(33, "Teh Tawar", DataCategory.minuman.id, "Ceker Ayam", 4000, 10, true)
//			var jerukNipis = Product(34, "Jeruk Nipis", DataCategory.minuman.id, "Ceker Ayam", 7000, 10, true)
//			var jeruk = Product(35, "Jeruk", DataCategory.minuman.id, "Ceker Ayam", 7000, 10, true)
//			var lemonTea = Product(36, "Lemon Tea", DataCategory.minuman.id, "Ceker Ayam", 8000, 10, true)
//			var minral = Product(37, "Air Mineral", DataCategory.minuman.id, "Ceker Ayam", 4000, 10, true)
//
//			fun getDetailProduct(category: Category?): ArrayList<DetailOrderEx>{
//				val items: ArrayList<DetailOrderEx> = ArrayList()
//				if (category != null){
//					for (prod in allDetailProductEx){
//						if (prod.product != null){
//							if (prod.product!!.category == category.id){
//								items.add(prod)
//							}
//						}
//					}
//				}
//				return items
//			}
//
//			private var allDetailProductEx: ArrayList<DetailOrderEx> = ArrayList()
//			get() {
//				val array: ArrayList<DetailOrderEx> = ArrayList()
//				array.add(DetailOrderEx(kAyam))
//				array.add(DetailOrderEx(kUdang))
//				array.add(DetailOrderEx(sAyam))
//				array.add(DetailOrderEx(sUdang))
//				array.add(DetailOrderEx(sKepiting))
//				array.add(DetailOrderEx(sSeafood))
//				array.add(DetailOrderEx(ekado))
//				array.add(DetailOrderEx(nori))
//				array.add(DetailOrderEx(hakau))
//				array.add(DetailOrderEx(hisit))
//				array.add(DetailOrderEx(lAyam))
//				array.add(DetailOrderEx(lUdang))
//				array.add(DetailOrderEx(kwotie))
//				array.add(DetailOrderEx(angsio))
//
//				array.add(DetailOrderEx(small))
//				array.add(DetailOrderEx(regular))
//				array.add(DetailOrderEx(big))
//				array.add(DetailOrderEx(party))
//
//				array.add(DetailOrderEx(chili))
//				array.add(DetailOrderEx(mayo))
//				array.add(DetailOrderEx(chilsos))
//
//				array.add(DetailOrderEx(yLemon))
//				array.add(DetailOrderEx(yMelon))
//				array.add(DetailOrderEx(yStrw))
//				array.add(DetailOrderEx(yLychee))
//				array.add(DetailOrderEx(yMarkisa))
//				array.add(DetailOrderEx(sLemon))
//				array.add(DetailOrderEx(sMelon))
//				array.add(DetailOrderEx(sStrw))
//				array.add(DetailOrderEx(sLychee))
//				array.add(DetailOrderEx(sMarkisa))
//				array.add(DetailOrderEx(tea))
//				array.add(DetailOrderEx(teaM))
//				array.add(DetailOrderEx(jerukNipis))
//				array.add(DetailOrderEx(jeruk))
//				array.add(DetailOrderEx(lemonTea))
//				array.add(DetailOrderEx(minral))
//				return array
//			}
//		}
//	}
//
//	class DataOrder{
//		companion object{
//
//			var dummyProduct: ArrayList<DetailOrderEx> = ArrayList()
//			get() {
//				val array: ArrayList<DetailOrderEx> = ArrayList()
//				array.add(DetailOrderEx(DataProduct.kAyam, 1))
//				array.add(DetailOrderEx(DataProduct.sUdang, 2))
//				array.add(DetailOrderEx(DataProduct.hisit, 1))
//				array.add(DetailOrderEx(DataProduct.jerukNipis, 2))
//				return array
//			}
//
////			fun getDone(): ArrayList<Order>{
////				val items: ArrayList<Order> = ArrayList()
////				items.add(Order(Customer(23, "Denis"), 6545646, dummyProduct, MainConfig.currentTime, Calendar.getInstance(), 75000, Order.DONE))
////				items.add(Order(Customer(23, "Denis"), 6545646, dummyProduct, MainConfig.currentTime, Calendar.getInstance(), 80000, Order.DONE))
////				items.add(Order(Customer(23, "Denis"), 6545646, dummyProduct, MainConfig.currentTime, Calendar.getInstance(), 100000, Order.DONE))
////				return items
////			}
////
////			fun getPay(): ArrayList<Order>{
////				val items: ArrayList<Order> = ArrayList()
////				items.add(Order(Customer(22, "Evaviliya"), 54165, dummyProduct, MainConfig.currentTime, Calendar.getInstance(), 0, Order.NEED_PAY))
////				items.add(Order(Customer(22, "Evaviliya"), 54165, dummyProduct, MainConfig.currentTime, Calendar.getInstance(), 0, Order.NEED_PAY))
////				items.add(Order(Customer(22, "Evaviliya"), 54165, dummyProduct, MainConfig.currentTime, Calendar.getInstance(), 0, Order.NEED_PAY))
////				items.add(Order(Customer(22, "Evaviliya"), 54165, dummyProduct, MainConfig.currentTime, Calendar.getInstance(), 0, Order.NEED_PAY))
////				return items
////			}
////
////			fun getProcess(): ArrayList<Order>{
////				val items: ArrayList<Order> = ArrayList()
////				items.add(Order(Customer(21, "Linda"), 123, dummyProduct, MainConfig.currentTime, Calendar.getInstance(), 0, Order.ON_PROCESS))
////				items.add(Order(Customer(21, "Linda"), 124, dummyProduct, MainConfig.currentTime, Calendar.getInstance(), 0, Order.ON_PROCESS))
////				items.add(Order(Customer(21, "Linda"), 125, dummyProduct, MainConfig.currentTime, null, 0, Order.ON_PROCESS))
////				items.add(Order(Customer(21, "Linda"), 126, dummyProduct, MainConfig.currentTime, null, 0, Order.ON_PROCESS))
////				items.add(Order(Customer(21, "Linda"), 127, dummyProduct, MainConfig.currentTime, null, 0, Order.ON_PROCESS))
////				items.add(Order(Customer(21, "Linda"), 128, dummyProduct, MainConfig.currentTime, null, 0, Order.ON_PROCESS))
////				return items
////			}
//		}
//	}
//}
