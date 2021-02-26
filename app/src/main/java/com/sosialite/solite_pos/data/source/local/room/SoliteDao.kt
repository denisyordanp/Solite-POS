package com.sosialite.solite_pos.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
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
	fun getOrdersByStatus(status: Int, date: String): LiveData<List<OrderWithCustomer>>

	@Query("SELECT * FROM '${Order.DB_NAME}' WHERE ${Order.NO} = :orderNo")
	fun getOrdersByNo(orderNo: String): OrderWithCustomer

	@Query("SELECT * FROM ${OrderDetail.DB_NAME} WHERE ${Order.NO} = :orderNo")
	fun getDetailOrders(orderNo: String): LiveData<List<OrderDetail>>

	@Query("SELECT * FROM '${Order.DB_NAME}' WHERE ${Order.NO} = :orderNo")
	fun getOrderPayment(orderNo: String): LiveData<OrderWithPayment?>

	@Query("SELECT * FROM ${OrderDetail.DB_NAME} WHERE ${OrderDetail.ID} = :idDetail")
	fun getOrderVariants(idDetail: Long): DetailWithVariantOption

	@Query("SELECT * FROM ${OrderProductVariantMix.DB_NAME} WHERE ${OrderProductVariantMix.ID} = :idMix")
	fun getOrderMixVariantsOption(idMix: Long): DetailProductMixWithVariantOption

	@Query("SELECT * FROM ${OrderDetail.DB_NAME} WHERE ${OrderDetail.ID} = :idDetail")
	fun getOrderVariantsMix(idDetail: Long): DetailWithVariantMixOption

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertOrder(order: Order): Long

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertOrders(order: List<Order>)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertDetailOrder(detail: OrderDetail): Long

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertVariantOrder(variants: OrderProductVariant): Long

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertMixVariantOrder(variants: OrderMixProductVariant): Long

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertVariantMixOrder(variants: OrderProductVariantMix): Long

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertPaymentOrders(payment: List<OrderPayment>)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertPaymentOrder(payment: OrderPayment): Long

	@Update
	fun updatePaymentOrder(order: OrderPayment)

	@Transaction
	fun newOrder(order: OrderWithProduct){
		insertOrder(order.order)
		for (item in order.products){
			if (item.product != null){

				val idOrder = insertDetailOrder(OrderDetail(order.order.orderNo, item.product!!.id, item.amount))

				if (item.product!!.isMix){
					for (p in item.mixProducts){
						decreaseProductStock(p.product.id, p.amount)

						val idMix = insertVariantMixOrder(OrderProductVariantMix(idOrder, p.product.id, p.amount))

						for (variant in p.variants){
							insertMixVariantOrder(OrderMixProductVariant(idMix, variant.id))
						}
					}
				}else{
					decreaseProductStock(item.product!!.id, (item.amount * item.product!!.portion))

					for (variant in item.variants){
						insertVariantOrder(OrderProductVariant(idOrder, variant.id))
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
	fun getPurchases(): LiveData<List<Purchase>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertPurchases(data: List<Purchase>)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertPurchase(data: Purchase)

	@Update
	fun updatePurchase(data: Purchase)

	@Query("SELECT * FROM ${PurchaseProduct.DB_NAME} WHERE ${Purchase.NO} = :purchaseNo")
	fun getPurchasesProduct(purchaseNo: String): List<PurchaseProduct>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertPurchaseProducts(data: List<PurchaseProduct>)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertPurchaseProduct(data: PurchaseProduct)

	@Update
	fun updatePurchaseProduct(data: PurchaseProduct)

	@Transaction
	fun newPurchase(data: PurchaseWithProduct){
		insertPurchase(data.purchase)
		insertPurchaseProducts(data.purchaseProduct)
		for (product in data.products){
			if (product.purchaseProduct != null){
				increaseProductStock(product.purchaseProduct!!.idProduct, product.purchaseProduct!!.amount)
			}
		}
	}

//	PRODUCT

	@Query("SELECT * FROM ${Product.DB_NAME} WHERE ${Product.ID} = :idProduct")
	fun getProduct(idProduct: Long): Product

	@Query("SELECT * FROM ${Product.DB_NAME} WHERE ${Category.ID} = :category")
	fun getProductWithCategories(category: Long): LiveData<List<ProductWithCategory>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertProducts(data: List<Product>)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertProduct(data: Product): Long

	@Update
	fun updateProduct(data: Product)

	@Query("UPDATE ${Product.DB_NAME} SET ${Product.STOCK} = ((SELECT ${Product.STOCK} FROM ${Product.DB_NAME} WHERE ${Product.ID} = :idProduct) + :amount) WHERE ${Product.ID} = :idProduct")
	fun increaseProductStock(idProduct: Long, amount: Int)

	@Query("UPDATE ${Product.DB_NAME} SET ${Product.STOCK} = ((SELECT ${Product.STOCK} FROM ${Product.DB_NAME} WHERE ${Product.ID} = :idProduct) - :amount) WHERE ${Product.ID} = :idProduct")
	fun decreaseProductStock(idProduct: Long, amount: Int)

	@Transaction
	fun decreaseAndGetProduct(idProduct: Long, amount: Int): Product {
		decreaseProductStock(idProduct, amount)
		return getProduct(idProduct)
	}

	@Transaction
	fun increaseAndGetProduct(idProduct: Long, amount: Int): Product {
		increaseProductStock(idProduct, amount)
		return getProduct(idProduct)
	}

//	VARIANT PRODUCT

	@Query("SELECT * FROM ${Product.DB_NAME} WHERE ${Category.ID} = :idCategory")
	fun getDataProduct(idCategory: Long): LiveData<List<DataProduct>>

	@Query("SELECT * FROM ${VariantProduct.DB_NAME} WHERE ${Product.ID} = :idProduct AND ${VariantOption.ID} = :idVariantOption")
	fun getVariantProduct(idProduct: Long, idVariantOption: Long): LiveData<List<VariantProduct>>

	@Query("SELECT * FROM ${VariantProduct.DB_NAME} WHERE ${Product.ID} = :idProduct")
	fun getVariantProductById(idProduct: Long): LiveData<VariantProduct?>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertVariantProducts(data: List<VariantProduct>)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertVariantProduct(data: VariantProduct): Long

	@Update
	fun updateVariantProduct(data: VariantProduct)

	@Query("DELETE FROM ${VariantProduct.DB_NAME} WHERE ${VariantOption.ID} = :idVariant AND ${Product.ID} = :idProduct")
	fun removeVariantProduct(idVariant: Long, idProduct: Long)

//	VARIANT MIX

	@Query("SELECT * FROM ${Variant.DB_NAME} WHERE ${Variant.ID} = :idVariant")
	fun getVariantMixProduct(idVariant: Long): VariantWithVariantMix

	@Query("SELECT * FROM ${Variant.DB_NAME} WHERE ${Variant.ID} = :idVariant")
	fun getLiveVariantMixProduct(idVariant: Long): LiveData<VariantWithVariantMix>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertVariantMix(data: VariantMix): Long

	@Update
	fun updateVariantMix(data: VariantMix)

	@Query("DELETE FROM ${VariantMix.DB_NAME} WHERE ${Variant.ID} = :idVariant AND ${Product.ID} = :idProduct")
	fun removeVariantMix(idVariant: Long, idProduct: Long)

//	CATEGORY

	@RawQuery(observedEntities = [Category::class])
	fun getCategories(query: SupportSQLiteQuery): LiveData<List<Category>>

	@Query("SELECT * FROM ${Category.DB_NAME} WHERE ${Category.ID} = :idCategory")
	fun getCategory(idCategory: Int): Category

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertCategories(data: List<Category>)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertCategory(data: Category): Long

	@Update
	fun updateCategory(data: Category)

//	VARIANT

	@Query("SELECT * FROM ${Variant.DB_NAME}")
	fun getVariants(): LiveData<List<Variant>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertVariants(data: List<Variant>)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertVariant(data: Variant): Long

	@Update
	fun updateVariant(data: Variant)

//	VARIANT OPTIONS

	@RawQuery(observedEntities = [VariantOption::class])
	fun getVariantOptions(query: SupportSQLiteQuery): LiveData<List<VariantOption>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertVariantOptions(data: List<VariantOption>)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertVariantOption(data: VariantOption): Long

	@Update
	fun updateVariantOption(data: VariantOption)

//	CUSTOMERS

	@Query("SELECT * FROM ${Customer.DB_NAME}")
	fun getCustomers(): LiveData<List<Customer>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertCustomers(data: List<Customer>)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertCustomer(data: Customer): Long

	@Update
	fun updateCustomer(data: Customer)

	//	SUPPLIER

	@Query("SELECT * FROM ${Supplier.DB_NAME}")
	fun getSuppliers(): LiveData<List<Supplier>>

	@Query("SELECT * FROM ${Supplier.DB_NAME} WHERE ${Supplier.ID} = :idSupplier")
	fun getSupplierById(idSupplier: Long): Supplier

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertSuppliers(data: List<Supplier>)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertSupplier(data: Supplier): Long

	@Update
	fun updateSupplier(data: Supplier)

//	PAYMENTS

	@Query("SELECT * FROM ${Payment.DB_NAME}")
	fun getPayments(): LiveData<List<Payment>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertPayments(data: List<Payment>)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertPayment(data: Payment): Long

	@Update
	fun updatePayment(data: Payment)

//	OUTCOME

	@Query("SELECT * FROM ${Outcome.DB_NAME} WHERE date(${Outcome.DATE}) = date(:date)")
	fun getOutcome(date: String): LiveData<List<Outcome>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertOutcomes(data: List<Outcome>)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertOutcome(data: Outcome): Long

	@Update
	fun updateOutcome(data: Outcome)

	// FILL DATA
	@Transaction
	fun fillData(){
		insertVariant(Variant(1, "Cara Masak", Variant.ONE_OPTION, isMust = true, isMix = false))
		insertVariant(Variant(2, "Mix Variant", Variant.MULTIPLE_OPTION, isMust = true, isMix = true))
		insertVariantOption(VariantOption(1, 1, "Kukus", "Dimsum Kukus", isCount = false, isActive = true, false))
		insertVariantOption(VariantOption(2, 1, "Goreng", "Dimsum Goreng", isCount = false, isActive = true, false))
		insertCategory(Category(1, "Dimsum", "Dimsum Porsian", isStock = true, isActive = true, false))
		insertCategory(Category(2, "Mix Variant", "Dimsum Mix", isStock = false, isActive = true, false))
		insertProduct(Product(1, "Siomay Ayam", 1, "", "Siomay Ayam", 14000, 2250, 4, 10, isMix = false, isActive = true, false))
		insertProduct(Product(2, "Kwotie", 1, "", "Kwotie", 15000, 2500, 4, 10, isMix = false, isActive = true, false))
		insertProduct(Product(3, "Small Mix", 2, "", "Small Mix", 18000, 1, 5, 0, isMix = true, isActive = true, false))
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
