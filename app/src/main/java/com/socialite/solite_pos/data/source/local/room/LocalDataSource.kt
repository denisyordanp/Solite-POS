package com.socialite.solite_pos.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.socialite.solite_pos.data.source.local.entity.helper.*
import com.socialite.solite_pos.data.source.local.entity.room.helper.OrderData

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

	fun getProducts(idCategory: Long): LiveData<List<Products>> {
		val result: MediatorLiveData<List<Products>> = MediatorLiveData()
		result.addSource(soliteDao.getProducts(idCategory)){ products ->
			val dataProducts: ArrayList<Products> = ArrayList()
			for (product in products){
				val category = soliteDao.getCategoryById(product.product!!.category)
				val data = Products()
				data.category = category
				data.product = product.product!!
				data.variants = getProductVariantOptions(product.product!!.id)
				dataProducts.add(data)
			}
			result.value = dataProducts
		}
		return result
	}

	fun getProductVariantOptions(idProduct: Long): LiveData<List<VariantWithOptions>?> {
		val result: MediatorLiveData<List<VariantWithOptions>?> = MediatorLiveData()
		result.addSource(soliteDao.getVariantProducts(idProduct)){ variantProducts ->
			val variants: ArrayList<VariantWithOptions> = ArrayList()
			if (!variantProducts.isNullOrEmpty()){
				var data = VariantWithOptions()
				for ((i, v) in variantProducts.withIndex()){

					when{
						variantProducts.size-1 == i -> {
							if(data.variant != null) {
								if (v.variant == data.variant!!){
									data.options.add(v.option)
									variants.add(data)
								}else{
									variants.add(data)
									data = VariantWithOptions()
									data.variant = v.variant
									data.options.add(v.option)
									variants.add(data)
								}
							}else{
								data.variant = v.variant
								data.options.add(v.option)
								variants.add(data)
							}
						}
						i == 0 -> {
							data.variant = v.variant
							data.options.add(v.option)
						}
						data.variant != null -> {
							if (v.variant == data.variant!!){
								data.options.add(v.option)
							}else{
								variants.add(data)
								data = VariantWithOptions()
								data.variant = v.variant
								data.options.add(v.option)
							}
						}
					}
				}
			}
			result.value = variants
		}
		return result
	}

	fun getPurchaseData(): LiveData<List<PurchaseWithProduct>> {
		val result: MediatorLiveData<List<PurchaseWithProduct>> = MediatorLiveData()
		result.addSource(soliteDao.getPurchases()){
			val list: ArrayList<PurchaseWithProduct> = ArrayList()
			for (purchase in it){
				val purchaseProduct = soliteDao.getPurchasesProduct(purchase.purchaseNo)
				result.addSource(soliteDao.getSupplierById(purchase.idSupplier)) { supplier ->
					val array: ArrayList<PurchaseProductWithProduct> = ArrayList()
					for (products in purchaseProduct){
						val product = soliteDao.getProduct(products.idProduct)
						array.add(PurchaseProductWithProduct(products, product))
					}
					if (list.isNotEmpty()) list.clear()
					list.add(PurchaseWithProduct(purchase, supplier, array))
					result.value = list
				}
			}
		}
		return result
	}

	fun getProductOrder(orderNo: String): LiveData<List<ProductOrderDetail>> {
		val result: MediatorLiveData<List<ProductOrderDetail>> = MediatorLiveData()
		val details = soliteDao.getDetailOrders(orderNo)
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
			result.value = products
		}
		return result
	}
}
