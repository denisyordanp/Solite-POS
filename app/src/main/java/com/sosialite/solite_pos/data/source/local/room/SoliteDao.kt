package com.sosialite.solite_pos.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.sosialite.solite_pos.data.source.local.entity.helper.*
import com.sosialite.solite_pos.data.source.local.entity.room.bridge.*
import com.sosialite.solite_pos.data.source.local.entity.room.helper.*
import com.sosialite.solite_pos.data.source.local.entity.room.master.*
import com.sosialite.solite_pos.data.source.local.entity.room.master.VariantOption

@Dao
interface SoliteDao{

//	ORDER

	@Query("SELECT * FROM '${Order.DB_NAME}' WHERE ${Order.STATUS} = :status AND date(${Order.ORDER_DATE}) = date(:date)")
	fun getOrdersByStatus(status: Int, date: String): List<OrderWithCustomer>

//	@Query("SELECT * FROM '${Order.DB_NAME}' WHERE ${Order.STATUS} = ${Order.DONE} AND date(${Order.ORDER_DATE}) = date(:date)")
//	fun getOrders(date: String): LiveData<List<Order>>

	@Query("SELECT * FROM '${Order.DB_NAME}' WHERE ${Order.NO} = :orderNo")
	fun getOrdersByNo(orderNo: String): OrderWithCustomer

	@Query("SELECT * FROM ${OrderDetail.DB_NAME} WHERE ${Order.NO} = :orderNo")
	fun getDetailOrders(orderNo: String): List<OrderDetail>

	@Query("SELECT * FROM '${Order.DB_NAME}' WHERE ${Order.NO} = :orderNo")
	fun getOrderPayment(orderNo: String): OrderWithPayment?

	@Query("SELECT * FROM ${OrderDetail.DB_NAME} WHERE ${OrderDetail.ID} = :idDetail")
	fun getOrderVariants(idDetail: Int): DetailWithVariantOption

	@Query("SELECT * FROM ${OrderProductVariantMix.DB_NAME} WHERE ${OrderProductVariantMix.ID} = :idMix")
	fun getOrderMixVariantsOption(idMix: Int): DetailProductMixWithVariantOption

	@Query("SELECT * FROM ${OrderDetail.DB_NAME} WHERE ${OrderDetail.ID} = :idDetail")
	fun getOrderVariantsMix(idDetail: Int): DetailWithVariantMixOption

	@Transaction
	fun getOrderWithProduct(item: OrderWithCustomer): OrderWithProduct{
		val payment: OrderWithPayment? = getOrderPayment(item.order.orderNo)
		val order = OrderWithProduct(item.order, payment, item.customer)
		val products: ArrayList<ProductOrderDetail> = ArrayList()
		val details = getDetailOrders(item.order.orderNo)
		for (item2 in details){
			val product = getProduct(item2.idProduct)
			if (product.isMix){
				val mixes = getOrderVariantsMix(item2.id)
				val mixProduct: ArrayList<ProductMixOrderDetail> = ArrayList()
				for (mix in mixes.variantsMix){
					val variants = getOrderMixVariantsOption(mix.id)
					mixProduct.add(ProductMixOrderDetail(
							getProduct(mix.idProduct),
							ArrayList(variants.options),
							mix.amount
					))
				}
				products.add(ProductOrderDetail.createMix(product, mixProduct, item2.amount))
			}else{
				val variants = getOrderVariants(item2.id)
				products.add(ProductOrderDetail.createProduct(product, ArrayList(variants.options), item2.amount))
			}
		}
		order.products = products
		return order
	}

	@Transaction
	fun getListOrderDetail(status: Int, date: String): List<OrderWithProduct>{
		val orders = getOrdersByStatus(status, date)
		val list: ArrayList<OrderWithProduct> = ArrayList()
		for (item in orders){
			list.add(getOrderWithProduct(item))
		}
		return list
	}

	@Transaction
	fun getOrderDetail(orderNo: String): OrderWithProduct{
		val order = getOrdersByNo(orderNo)
		return getOrderWithProduct(order)
	}

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertOrder(order: Order): Long

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertDetailOrders(detail: OrderDetail): Long

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertVariantOrder(variants: OrderProductVariant)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertMixVariantOrder(variants: OrderMixProductVariant)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertVariantMixOrder(variants: OrderProductVariantMix): Long

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertPaymentOrder(payment: OrderPayment)

	@Transaction
	fun insertAndGetPaymentOrder(payment: OrderPayment): OrderWithProduct{
		insertPaymentOrder(payment)
		return getOrderDetail(payment.orderNO)
	}

	@Transaction
	fun newOrder(order: OrderWithProduct){
		insertOrder(order.order)
		for (item in order.products){
			if (item.product != null){

				val idOrder = insertDetailOrders(OrderDetail(order.order.orderNo, item.product!!.id, item.amount))

				if (item.product!!.isMix){
					for (p in item.mixProducts){
						decreaseProductStock(p.product.id, p.amount)

						val idMix = insertVariantMixOrder(OrderProductVariantMix(idOrder.toInt(), p.product.id, p.amount)).toInt()

						for (variant in p.variants){
							insertMixVariantOrder(OrderMixProductVariant(idMix, variant.id))
						}
					}
				}else{
					decreaseProductStock(item.product!!.id, (item.amount * item.product!!.portion))

					for (variant in item.variants){
						insertVariantOrder(OrderProductVariant(idOrder.toInt(), variant.id))
					}
				}
			}
		}
	}

	@Update
	fun updateOrder(order: Order)

	@Transaction
	fun cancelOrder(order: OrderWithProduct){
		updateOrder(order.order)
		for (item in order.products){
			if (item.product != null){

				if (item.product!!.isMix){
					for (p in item.mixProducts){
						increaseProductStock(p.product.id, p.amount)
					}
				}else{
					increaseProductStock(item.product!!.id, (item.amount * item.product!!.portion))
				}
			}
		}
	}

//	PURCHASE

	@Query("SELECT * FROM ${Purchase.DB_NAME}")
	fun getPurchases(): List<Purchase>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertPurchase(data: Purchase)

	@Update
	fun updatePurchase(data: Purchase)

	@Query("SELECT * FROM ${PurchaseProduct.DB_NAME} WHERE ${Purchase.NO} = :purchaseNo")
	fun getPurchasesProduct(purchaseNo: String): List<PurchaseProduct>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertPurchaseProduct(data: List<PurchaseProduct>)

	@Update
	fun updatePurchaseProduct(data: PurchaseProduct)

	@Transaction
	fun getPurchaseData(): List<PurchaseWithProduct>{
		val purchases = getPurchases()
		val list: ArrayList<PurchaseWithProduct> = ArrayList()
		for (purchase in purchases){
			val purchaseProduct = getPurchasesProduct(purchase.purchaseNo)
			val supplier = getSupplierById(purchase.idSupplier)
			val array: ArrayList<PurchaseProductWithProduct> = ArrayList()
			for (products in purchaseProduct){
				val product = getProduct(products.idProduct)
				array.add(PurchaseProductWithProduct(products, product))
			}
			list.add(PurchaseWithProduct(purchase, supplier, array))
		}
		return list
	}

	@Transaction
	fun newPurchase(data: PurchaseWithProduct){
		insertPurchase(data.purchase)
		insertPurchaseProduct(data.purchaseProduct)
		for (product in data.products){
			if (product.purchaseProduct != null){
				increaseProductStock(product.purchaseProduct!!.idProduct, product.purchaseProduct!!.amount)
			}
		}
	}

//	PRODUCT

	@Query("SELECT * FROM ${Product.DB_NAME} WHERE ${Product.ID} = :idProduct")
	fun getProduct(idProduct: Int): Product

	@Query("SELECT * FROM ${Product.DB_NAME} WHERE ${Category.ID} = :category")
	fun getProductWithCategories(category: Int): LiveData<List<ProductWithCategory>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertProduct(data: Product): Long

	@Update
	fun updateProduct(data: Product)

	@Query("UPDATE ${Product.DB_NAME} SET ${Product.STOCK} = ((SELECT ${Product.STOCK} FROM ${Product.DB_NAME} WHERE ${Product.ID} = :idProduct) + :amount) WHERE ${Product.ID} = :idProduct")
	fun increaseProductStock(idProduct: Int, amount: Int)

	@Query("UPDATE ${Product.DB_NAME} SET ${Product.STOCK} = ((SELECT ${Product.STOCK} FROM ${Product.DB_NAME} WHERE ${Product.ID} = :idProduct) - :amount) WHERE ${Product.ID} = :idProduct")
	fun decreaseProductStock(idProduct: Int, amount: Int)

//	VARIANT PRODUCT

	@Query("SELECT * FROM ${Product.DB_NAME} WHERE ${Category.ID} = :idCategory")
	fun getDataProduct(idCategory: Int): LiveData<List<DataProduct>>

	@Query("SELECT * FROM ${VariantProduct.DB_NAME} WHERE ${Product.ID} = :idProduct AND ${VariantOption.ID} = :idVariantOption")
	fun getVariantProduct(idProduct: Int, idVariantOption: Int): List<VariantProduct>

	@Query("SELECT * FROM ${VariantProduct.DB_NAME} WHERE ${Product.ID} = :idProduct")
	fun getVariantProductById(idProduct: Int): VariantProduct?

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertVariantProduct(data: VariantProduct)

	@Query("DELETE FROM ${VariantProduct.DB_NAME} WHERE ${VariantOption.ID} = :idVariant AND ${Product.ID} = :idProduct")
	fun removeVariantProduct(idVariant: Int, idProduct: Int)

//	VARIANT MIX

	@Query("SELECT * FROM ${Variant.DB_NAME} WHERE ${Variant.ID} = :idVariant")
	fun getVariantMixProduct(idVariant: Int): VariantWithVariantMix

	@Query("SELECT * FROM ${Variant.DB_NAME} WHERE ${Variant.ID} = :idVariant")
	fun getLiveVariantMixProduct(idVariant: Int): LiveData<VariantWithVariantMix>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertVariantMix(data: VariantMix)

	@Query("DELETE FROM ${VariantMix.DB_NAME} WHERE ${Variant.ID} = :idVariant AND ${Product.ID} = :idProduct")
	fun removeVariantMix(idVariant: Int, idProduct: Int)

//	CATEGORY

	@RawQuery(observedEntities = [Category::class])
	fun getCategories(query: SupportSQLiteQuery): LiveData<List<Category>>

	@Query("SELECT * FROM ${Category.DB_NAME} WHERE ${Category.ID} = :idCategory")
	fun getCategory(idCategory: Int): Category

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertCategory(data: Category): Long

	@Update
	fun updateCategory(data: Category)

//	VARIANT

	@Query("SELECT * FROM ${Variant.DB_NAME}")
	fun getVariants(): LiveData<List<Variant>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertVariant(data: Variant)

	@Update
	fun updateVariant(data: Variant)

//	VARIANT OPTIONS

	@RawQuery(observedEntities = [VariantOption::class])
	fun getVariantOptions(query: SupportSQLiteQuery): LiveData<List<VariantOption>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertVariantOption(data: VariantOption)

	@Update
	fun updateVariantOption(data: VariantOption)

//	CUSTOMERS

	@Query("SELECT * FROM ${Customer.DB_NAME}")
	fun getCustomers(): LiveData<List<Customer>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertCustomer(data: Customer): Long

	@Update
	fun updateCustomer(data: Customer)

	//	SUPPLIER

	@Query("SELECT * FROM ${Supplier.DB_NAME}")
	fun getSuppliers(): LiveData<List<Supplier>>

	@Query("SELECT * FROM ${Supplier.DB_NAME} WHERE ${Supplier.ID} = :idSupplier")
	fun getSupplierById(idSupplier: Int): Supplier

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertSupplier(data: Supplier): Long

	@Update
	fun updateSupplier(data: Supplier)

//	PAYMENTS

	@Query("SELECT * FROM ${Payment.DB_NAME}")
	fun getPayments(): LiveData<List<Payment>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertPayment(data: Payment)

	@Update
	fun updatePayment(data: Payment)

//	OUTCOME

	@Query("SELECT * FROM ${Outcome.DB_NAME} WHERE date(${Outcome.DATE}) = date(:date)")
	fun getOutcome(date: String): LiveData<List<Outcome>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertOutcome(data: Outcome)

	@Update
	fun updateOutcome(data: Outcome)


	// FILL DATA
	@Transaction
	fun fillData(){
		insertVariant(Variant(1, "Cara Masak", Variant.ONE_OPTION, isMust = true, isMix = false))
		insertVariant(Variant(2, "Mix Variant", Variant.MULTIPLE_OPTION, isMust = true, isMix = true))
		insertVariantOption(VariantOption(1, 1, "Kukus", "Dimsum Kukus", 0, isCount = false, isActive = true, false))
		insertVariantOption(VariantOption(2, 1, "Goreng", "Dimsum Goreng", 0, isCount = false, isActive = true, false))
		insertCategory(Category(1, "Dimsum", "Dimsum Porsian", isStock = true, isActive = true, false))
		insertCategory(Category(2, "Mix Variant", "Dimsum Mix", isStock = false, isActive = true, false))
		insertProduct(Product(1, "Siomay Ayam", 1, "Siomay Ayam", 14000, 2250, 4, 10, isMix = false, isActive = true, false))
		insertProduct(Product(2, "Kwotie", 1, "Kwotie", 15000, 2500, 4, 10, isMix = false, isActive = true, false))
		insertProduct(Product(3, "Small Mix", 2, "Small Mix", 18000, 1, 5, 0, isMix = true, isActive = true, false))
		insertVariantMix(VariantMix(1, 2, 1))
		insertVariantMix(VariantMix(2, 2, 2))
		insertVariantProduct(VariantProduct(1, 1, 1, 1))
		insertVariantProduct(VariantProduct(2, 1, 2, 2))
		insertVariantProduct(VariantProduct(3, 2, 1, 3))
		insertPayment(Payment(1, "Cash", "Pembayaran Tunai", 0f, isCash = true, isActive = true, false))
		insertPayment(Payment(2, "Debit", "Pembayaran Debit", 0.1f, isCash = false, isActive = true, false))
		insertSupplier(Supplier(1, "Dimsum Almaris", "1654156123", "Setrasari", "Lumanyun", false))
	}
}
