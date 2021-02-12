package com.sosialite.solite_pos.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.sosialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.sosialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.sosialite.solite_pos.data.source.local.entity.helper.PurchaseProductWithProduct
import com.sosialite.solite_pos.data.source.local.entity.helper.PurchaseWithProduct
import com.sosialite.solite_pos.data.source.local.entity.room.bridge.*
import com.sosialite.solite_pos.data.source.local.entity.room.helper.*
import com.sosialite.solite_pos.data.source.local.entity.room.master.*
import com.sosialite.solite_pos.data.source.local.entity.room.master.VariantOption

@Dao
interface SoliteDao{

//	ORDER

	@Query("SELECT * FROM '${AppDatabase.TBL_ORDER}' WHERE ${Order.STATUS} = :status")
	fun getOrdersByStatus(status: Int): List<OrderWithCustomer>

	@Query("SELECT * FROM '${AppDatabase.TBL_ORDER}' WHERE ${Order.NO} = :orderNo")
	fun getOrdersByNo(orderNo: String): OrderWithCustomer

	@Query("SELECT * FROM ${AppDatabase.TBL_ORDER_DETAIL} WHERE ${Order.NO} = :orderNo")
	fun getDetailOrders(orderNo: String): List<OrderDetail>

	@Query("SELECT * FROM '${AppDatabase.TBL_ORDER}' WHERE ${Order.NO} = :orderNo")
	fun getOrderPayment(orderNo: String): OrderWithPayment?

	@Query("SELECT * FROM ${AppDatabase.TBL_ORDER_DETAIL} WHERE ${OrderDetail.ID} = :idDetail")
	fun getOrderVariants(idDetail: Int): DetailWithVariantOption

	@Transaction
	fun getOrderWithProduct(item: OrderWithCustomer): OrderWithProduct{
		val payment: OrderWithPayment? = getOrderPayment(item.order.orderNo)
		val order = OrderWithProduct(item.order, payment, item.customer)
		val products: ArrayList<ProductOrderDetail> = ArrayList()
		val details = getDetailOrders(item.order.orderNo)
		for (item2 in details){
			val product = getProduct(item2.idProduct)
			val variants = getOrderVariants(item2.id)
			products.add(ProductOrderDetail(product, ArrayList(variants.options), item2.amount))
		}
		order.products = products
		return order
	}

	@Transaction
	fun getListOrderDetail(status: Int): List<OrderWithProduct>{
		val orders = getOrdersByStatus(status)
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
	fun insertPaymentOrder(payment: OrderPayment)

	@Transaction
	fun insertAndGetPaymentOrder(payment: OrderPayment): OrderWithProduct{
		insertPaymentOrder(payment)
		return getOrderDetail(payment.orderNO)
	}

	@Transaction
	fun reduceProductStock(idProduct: Int, isMix: Boolean){
		val product = getProduct(idProduct)
		if (isMix){
			product.stock = product.stock-1
		}else{
			product.stock = product.stock-product.portion
		}
		updateProduct(product)
	}

	@Transaction
	fun newOrder(order: OrderWithProduct){
		insertOrder(order.order)
		for (item in order.products){
			if (item.product != null){
				val category = getCategory(item.product!!.category)
				val amount = if (category.isStock){
					item.amount * item.product!!.portion
				}else{
					item.amount
				}
				decreaseProductStock(item.product!!.id, amount)
				val idOrder = insertDetailOrders(OrderDetail(order.order.orderNo, item.product!!.id, item.amount))
				for (variant in item.variants){
					insertVariantOrder(OrderProductVariant(idOrder.toInt(), variant.id, 0))
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
				val category = getCategory(item.product!!.category)
				val amount = if (category.isStock){
					item.amount * item.product!!.portion
				}else{
					item.amount
				}
				increaseProductStock(item.product!!.id, amount)
			}
		}
	}

//	PURCHASE

	@Query("SELECT * FROM ${AppDatabase.TBL_PURCHASE}")
	fun getPurchases(): List<Purchase>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertPurchase(data: Purchase)

	@Update
	fun updatePurchase(data: Purchase)

	@Query("SELECT * FROM ${AppDatabase.TBL_PURCHASE_PRODUCT} WHERE ${Purchase.NO} = :purchaseNo")
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

	@Query("SELECT * FROM ${AppDatabase.TBL_PRODUCT} WHERE ${Product.ID} = :idProduct")
	fun getProduct(idProduct: Int): Product

	@Query("SELECT * FROM ${AppDatabase.TBL_PRODUCT} WHERE ${Category.ID} = :category")
	fun getProductWithCategories(category: Int): LiveData<List<ProductWithCategory>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertProduct(data: Product): Long

	@Update
	fun updateProduct(data: Product)

	@Query("UPDATE ${AppDatabase.TBL_PRODUCT} SET ${Product.STOCK} = ((SELECT ${Product.STOCK} FROM ${AppDatabase.TBL_PRODUCT} WHERE ${Product.ID} = :idProduct) + :amount) WHERE ${Product.ID} = :idProduct")
	fun increaseProductStock(idProduct: Int, amount: Int)

	@Query("UPDATE ${AppDatabase.TBL_PRODUCT} SET ${Product.STOCK} = ((SELECT ${Product.STOCK} FROM ${AppDatabase.TBL_PRODUCT} WHERE ${Product.ID} = :idProduct) - :amount) WHERE ${Product.ID} = :idProduct")
	fun decreaseProductStock(idProduct: Int, amount: Int)

//	VARIANT PRODUCT

	@Query("SELECT * FROM ${AppDatabase.TBL_PRODUCT} WHERE ${Category.ID} = :idCategory")
	fun getDataProduct(idCategory: Int): LiveData<List<DataProduct>>

	@Query("SELECT * FROM ${AppDatabase.TBL_VARIANT_PRODUCT} WHERE ${Product.ID} = :idProduct AND ${VariantOption.ID} = :idVariantOption")
	fun getVariantProduct(idProduct: Int, idVariantOption: Int): List<VariantProduct>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertVariantProduct(data: VariantProduct)

	@Query("DELETE FROM ${AppDatabase.TBL_VARIANT_PRODUCT} WHERE ${VariantOption.ID} = :idVariant AND ${Product.ID} = :idProduct")
	fun removeVariantProduct(idVariant: Int, idProduct: Int)

//	VARIANT MIX

	@Query("SELECT * FROM ${AppDatabase.TBL_VARIANT} WHERE ${Variant.ID} = :idVariant")
	fun getVariantMixProduct(idVariant: Int): VariantWithVariantMix

	@Query("SELECT * FROM ${AppDatabase.TBL_VARIANT} WHERE ${Variant.ID} = :idVariant")
	fun getLiveVariantMixProduct(idVariant: Int): LiveData<VariantWithVariantMix>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertVariantMix(data: VariantMix)

	@Query("DELETE FROM ${AppDatabase.TBL_VARIANT_MIX} WHERE ${Variant.ID} = :idVariant AND ${Product.ID} = :idProduct")
	fun removeVariantMix(idVariant: Int, idProduct: Int)

//	CATEGORY

	@RawQuery(observedEntities = [Category::class])
	fun getCategories(query: SupportSQLiteQuery): LiveData<List<Category>>

	@Query("SELECT * FROM ${AppDatabase.TBL_CATEGORY} WHERE ${Category.ID} = :idCategory")
	fun getCategory(idCategory: Int): Category

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertCategory(data: Category): Long

	@Update
	fun updateCategory(data: Category)

//	VARIANT

	@Query("SELECT * FROM ${AppDatabase.TBL_VARIANT}")
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

	@Query("SELECT * FROM ${AppDatabase.TBL_CUSTOMER}")
	fun getCustomers(): LiveData<List<Customer>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertCustomer(data: Customer): Long

	@Update
	fun updateCustomer(data: Customer)

	//	SUPPLIER

	@Query("SELECT * FROM ${AppDatabase.TBL_SUPPLIER}")
	fun getSuppliers(): LiveData<List<Supplier>>

	@Query("SELECT * FROM ${AppDatabase.TBL_SUPPLIER} WHERE ${Supplier.ID} = :idSupplier")
	fun getSupplierById(idSupplier: Int): Supplier

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertSupplier(data: Supplier): Long

	@Update
	fun updateSupplier(data: Supplier)

//	PAYMENTS

	@Query("SELECT * FROM ${AppDatabase.TBL_PAYMENT}")
	fun getPayments(): LiveData<List<Payment>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertPayment(data: Payment)

	@Update
	fun updatePayment(data: Payment)

//	OUTCOME

	@Query("SELECT * FROM ${AppDatabase.TBL_OUTCOME} WHERE date(${Outcome.DATE}) = date(:date)")
	fun getOutcome(date: String): LiveData<List<Outcome>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertOutcome(data: Outcome)

	@Update
	fun updateOutcome(data: Outcome)


	// FILL DATA
	@Transaction
	fun fillData(){
		insertVariant(Variant(1, "Cara Masak", Variant.ONE_OPTION, isMust = true, isMix = false))
		insertVariantOption(VariantOption(1, 1, "Kukus", "Dimsum Kukus", 0, isCount = false, isActive = true))
		insertVariantOption(VariantOption(2, 1, "Goreng", "Dimsum Goreng", 0, isCount = false, isActive = true))
		insertCategory(Category(1, "Dimsum", "Dimsum Porsian", isStock = true, isActive = true))
		insertProduct(Product(1, "Siomay Ayam", 1, "Siomay Ayam", 14000, 2250, 4, 1, isActive = true))
		insertProduct(Product(2, "Kwotie", 1, "Kwotie", 15000, 2500, 4, 1, isActive = true))
		insertVariantProduct(VariantProduct(1, 1, 1, 1))
		insertVariantProduct(VariantProduct(1, 1, 2, 2))
		insertPayment(Payment(1, "Cash", "Pembayaran Tunai", 0f, isCash = true, isActive = true))
		insertPayment(Payment(2, "Debit", "Pembayaran Debit", 0.1f, isCash = false, isActive = true))
		insertSupplier(Supplier(1, "Dimsum Almaris", "1654156123", "Setrasari", "Lumanyun"))
	}
}
