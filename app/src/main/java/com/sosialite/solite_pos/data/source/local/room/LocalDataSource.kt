package com.sosialite.solite_pos.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.sosialite.solite_pos.data.source.local.entity.helper.*
import com.sosialite.solite_pos.data.source.local.entity.room.helper.OrderWithCustomer

class LocalDataSource private constructor(val soliteDao: SoliteDao) {

	companion object {
		private var INSTANCE: LocalDataSource? = null

		fun getInstance(soliteDao: SoliteDao): LocalDataSource {
			if (INSTANCE == null) {
				INSTANCE = LocalDataSource(soliteDao)
			}
			return INSTANCE!!
		}
	}

	fun getPurchaseData(): LiveData<List<PurchaseWithProduct>> {
		val result: MediatorLiveData<List<PurchaseWithProduct>> = MediatorLiveData()
		val purchases = soliteDao.getPurchases()
		result.addSource(purchases){
			val list: ArrayList<PurchaseWithProduct> = ArrayList()
			for (purchase in it){
				val purchaseProduct = soliteDao.getPurchasesProduct(purchase.purchaseNo)
				val supplier = soliteDao.getSupplierById(purchase.idSupplier)
				val array: ArrayList<PurchaseProductWithProduct> = ArrayList()
				for (products in purchaseProduct){
					val product = soliteDao.getProduct(products.idProduct)
					array.add(PurchaseProductWithProduct(products, product))
				}
				list.add(PurchaseWithProduct(purchase, supplier, array))
				result.value = list
			}
		}
		return result
	}

	fun getListOrderDetail(status: Int, date: String): LiveData<List<OrderWithProduct>>{
		val result: MediatorLiveData<List<OrderWithProduct>> = MediatorLiveData()
		result.addSource(soliteDao.getOrdersByStatus(status, date)){ orders ->
			val list: ArrayList<OrderWithProduct> = ArrayList()
			for (item in orders){
				result.addSource(getOrderWithProduct(item)){ product ->
					list.add(product)
					result.value = list
				}
			}
		}
		return result
	}

	fun getOrderDetail(orderNo: String): LiveData<OrderWithProduct>{
		val order = soliteDao.getOrdersByNo(orderNo)
		return getOrderWithProduct(order)
	}

	private fun getOrderWithProduct(item: OrderWithCustomer): LiveData<OrderWithProduct>{
		val result: MediatorLiveData<OrderWithProduct> = MediatorLiveData()
		result.addSource(soliteDao.getOrderPayment(item.order.orderNo)){ payment ->
			val order = OrderWithProduct(item.order, payment, item.customer)
			val details = soliteDao.getDetailOrders(item.order.orderNo)
			result.addSource(details){ listDetail ->
				val products: ArrayList<ProductOrderDetail> = ArrayList()
				for (item2 in listDetail){
					val product = soliteDao.getProduct(item2.idProduct)
					if (product.isMix){
						val mixes = soliteDao.getOrderVariantsMix(item2.id)
						val mixProduct: ArrayList<ProductMixOrderDetail> = ArrayList()
						for (mix in mixes.variantsMix){
							val variants = soliteDao.getOrderMixVariantsOption(mix.id)
							mixProduct.add(ProductMixOrderDetail(
									soliteDao.getProduct(mix.idProduct),
									ArrayList(variants.options),
									mix.amount
							))
						}
						products.add(ProductOrderDetail.createMix(product, mixProduct, item2.amount))
					}else{
						val variants = soliteDao.getOrderVariants(item2.id)
						products.add(ProductOrderDetail.createProduct(product, ArrayList(variants.options), item2.amount))
					}
				}
				order.products = products
				result.value = order
			}
		}
		return result
	}
}
