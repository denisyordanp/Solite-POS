package com.socialite.solite_pos.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.socialite.solite_pos.data.source.local.entity.helper.PurchaseProductWithProduct
import com.socialite.solite_pos.data.source.local.entity.room.bridge.*
import com.socialite.solite_pos.data.source.local.entity.room.helper.*
import com.socialite.solite_pos.data.source.local.entity.room.master.*
import com.socialite.solite_pos.data.source.local.entity.room.master.VariantOption

@Dao
interface SoliteDao{

//	ORDER

	@Transaction
	@Query("SELECT * FROM '${Order.DB_NAME}' WHERE ${Order.STATUS} = :status AND date(${Order.ORDER_DATE}) = date(:date)")
	fun getOrdersByStatus(status: Int, date: String): LiveData<List<OrderData>>

	@Transaction
	@Query("SELECT * FROM '${Order.DB_NAME}' WHERE ${Order.NO} = :orderNo")
	fun getOrdersByNo(orderNo: String): LiveData<OrderData>

	@Query("SELECT * FROM ${OrderDetail.DB_NAME} WHERE ${Order.NO} = :orderNo")
	fun getDetailOrdersLiveData(orderNo: String): LiveData<List<OrderDetail>>

	@Transaction
	@Query("SELECT * FROM '${Order.DB_NAME}' WHERE ${Order.NO} = :orderNo")
	fun getOrderPayment(orderNo: String): LiveData<OrderData?>

	@Transaction
	@Query("SELECT * FROM ${OrderDetail.DB_NAME} WHERE ${OrderDetail.ID} = :idDetail")
	fun getOrderVariants(idDetail: Long): DetailWithVariantOption

	@Transaction
	@Query("SELECT * FROM ${OrderProductVariantMix.DB_NAME} WHERE ${OrderProductVariantMix.ID} = :idMix")
	fun getOrderMixVariantsOption(idMix: Long): DetailProductMixWithVariantOption

	@Transaction
	@Query("SELECT * FROM ${OrderDetail.DB_NAME} WHERE ${OrderDetail.ID} = :idDetail")
	fun getOrderVariantsMix(idDetail: Long): DetailWithVariantMixOption

	@Query("SELECT * FROM ${OrderDetail.DB_NAME} WHERE ${Order.NO} = :orderNo AND ${Product.ID} = :productId")
	fun getDetailOrders(orderNo: String, productId: Long): OrderDetail

	@Query("SELECT * FROM ${OrderProductVariantMix.DB_NAME} WHERE ${OrderDetail.ID} = :detailId AND ${Product.ID} = :productId")
	fun getOrderProductVariantMix(detailId: Long, productId: Long): OrderProductVariantMix

	@Query("SELECT * FROM ${OrderMixProductVariant.DB_NAME} WHERE ${OrderProductVariantMix.ID} = :orderVariantId AND ${VariantOption.ID} = :variantOptionId")
	fun getOrderMixProductVariant(orderVariantId: Long, variantOptionId: Long): OrderMixProductVariant

	@Query("SELECT * FROM ${OrderProductVariant.DB_NAME} WHERE ${OrderDetail.ID} = :orderDetailId AND ${VariantOption.ID} = :variantOptionId")
	fun getOrderVariant(orderDetailId: Long, variantOptionId: Long): OrderProductVariant

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun insertOrder(order: Order): Long

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun insertOrders(order: List<Order>)

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun insertDetailOrder(detail: OrderDetail): Long

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun insertDetailOrders(detail: List<OrderDetail>)

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun insertVariantOrder(variants: OrderProductVariant): Long

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun insertVariantOrders(variants: List<OrderProductVariant>)

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun insertMixVariantOrder(variants: OrderMixProductVariant): Long

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun insertMixVariantOrders(variants: List<OrderMixProductVariant>)

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun insertVariantMixOrder(variants: OrderProductVariantMix): Long

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun insertVariantMixOrders(variants: List<OrderProductVariantMix>)

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun insertPaymentOrders(payment: List<OrderPayment>)

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun insertPaymentOrder(payment: OrderPayment): Long

	@Update
	fun updatePaymentOrder(order: OrderPayment)

	@Update
	fun updateOrder(order: Order)

	@Delete
	fun deleteOrderDetail(orderDetail: OrderDetail)

	@Delete
	fun deleteOrderProductVariantMix(variantMix: OrderProductVariantMix)

	@Delete
	fun deleteOrderMixProductVariant(orderMix: OrderMixProductVariant)

	@Delete
	fun deleteOrderVariant(orderVariant: OrderProductVariant)

//	PURCHASE

	@Query("SELECT * FROM ${Purchase.DB_NAME}")
	fun getPurchases(): LiveData<List<PurchaseWithSupplier>>

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun insertPurchases(data: List<Purchase>)

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun insertPurchase(data: Purchase)

	@Update
	fun updatePurchase(data: Purchase)

	@Query("SELECT * FROM ${PurchaseProduct.DB_NAME} WHERE ${Purchase.NO} = :purchaseNo")
	fun getPurchasesProduct(purchaseNo: String): LiveData<List<PurchaseProductWithProduct>>

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun insertPurchaseProducts(data: List<PurchaseProduct>)

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun insertPurchaseProduct(data: PurchaseProduct)

	@Update
	fun updatePurchaseProduct(data: PurchaseProduct)

//	PRODUCT

	@Query("SELECT * FROM ${Product.DB_NAME} WHERE ${Product.ID} = :idProduct")
	fun getProduct(idProduct: Long): Product

	@Transaction
	@Query("SELECT * FROM ${Product.DB_NAME} WHERE ${Category.ID} = :category")
	fun getProductWithCategories(category: Long): LiveData<List<ProductWithCategory>>

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun insertProducts(data: List<Product>)

	@Insert(onConflict = OnConflictStrategy.IGNORE)
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

	@Transaction
	@Query("SELECT * FROM ${Product.DB_NAME} WHERE ${Category.ID} = :idCategory")
	fun getDataProduct(idCategory: Long): LiveData<List<DataProduct>>

	@Query("SELECT * FROM ${Product.DB_NAME} WHERE ${Category.ID} = :idCategory")
	fun getProducts(idCategory: Long): LiveData<List<ProductWithCategory>>

	@Query("SELECT * FROM ${VariantProduct.DB_NAME} WHERE ${Product.ID} = :idProduct AND ${VariantOption.ID} = :idVariantOption")
	fun getVariantProduct(idProduct: Long, idVariantOption: Long): LiveData<VariantProduct?>

	@Transaction
	@Query("SELECT * FROM ${VariantProduct.DB_NAME} WHERE ${Product.ID} = :idProduct ORDER BY ${Variant.ID}")
	fun getVariantProducts(idProduct: Long): LiveData<List<VariantProductWithOption>?>

	@Query("SELECT * FROM ${VariantProduct.DB_NAME} WHERE ${Product.ID} = :idProduct")
	fun getVariantProductById(idProduct: Long): LiveData<VariantProduct?>

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun insertVariantProducts(data: List<VariantProduct>)

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun insertVariantProduct(data: VariantProduct): Long

	@Update
	fun updateVariantProduct(data: VariantProduct)

	@Query("DELETE FROM ${VariantProduct.DB_NAME} WHERE ${VariantOption.ID} = :idVariant AND ${Product.ID} = :idProduct")
	fun removeVariantProduct(idVariant: Long, idProduct: Long)

//	VARIANT MIX

	@Transaction
	@Query("SELECT * FROM ${Variant.DB_NAME} WHERE ${Variant.ID} = :idVariant")
	fun getVariantMixProduct(idVariant: Long): LiveData<VariantWithVariantMix>

	@Transaction
	@Query("SELECT * FROM ${VariantMix.DB_NAME} WHERE ${Variant.ID} = :idVariant AND ${Product.ID} = :idProduct")
	fun getVariantMixProductById(idVariant: Long, idProduct: Long): LiveData<VariantMix?>

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun insertVariantMixes(data: List<VariantMix>)

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun insertVariantMix(data: VariantMix): Long

	@Update
	fun updateVariantMix(data: VariantMix)

	@Query("DELETE FROM ${VariantMix.DB_NAME} WHERE ${VariantMix.ID} = :idVariantMix")
	fun removeVariantMix(idVariantMix: Long)

//	CATEGORY

	@RawQuery(observedEntities = [Category::class])
	fun getCategories(query: SupportSQLiteQuery): LiveData<List<Category>>

	@Query("SELECT * FROM ${Category.DB_NAME} WHERE ${Category.ID} = :idCategory")
	fun getCategoryById(idCategory: Long): Category

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun insertCategories(data: List<Category>)

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun insertCategory(data: Category): Long

	@Update
	fun updateCategory(data: Category)

//	VARIANT

	@Query("SELECT * FROM ${Variant.DB_NAME}")
	fun getVariants(): LiveData<List<Variant>>

	@Query("SELECT * FROM ${Variant.DB_NAME} WHERE ${Variant.ID} = :idVariant")
	fun getVariantById(idVariant: Long): Variant

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun insertVariants(data: List<Variant>)

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun insertVariant(data: Variant): Long

	@Update
	fun updateVariant(data: Variant)

//	VARIANT OPTIONS

	@RawQuery(observedEntities = [VariantOption::class])
	fun getVariantOptions(query: SupportSQLiteQuery): LiveData<List<VariantOption>>

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun insertVariantOptions(data: List<VariantOption>)

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun insertVariantOption(data: VariantOption): Long

	@Update
	fun updateVariantOption(data: VariantOption)

//	CUSTOMERS

	@Query("SELECT * FROM ${Customer.DB_NAME}")
	fun getCustomers(): LiveData<List<Customer>>

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun insertCustomers(data: List<Customer>)

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun insertCustomer(data: Customer): Long

	@Update
	fun updateCustomer(data: Customer)

	//	SUPPLIER

	@Query("SELECT * FROM ${Supplier.DB_NAME}")
	fun getSuppliers(): LiveData<List<Supplier>>

	@Query("SELECT * FROM ${Supplier.DB_NAME} WHERE ${Supplier.ID} = :idSupplier")
	fun getSupplierById(idSupplier: Long): LiveData<Supplier>

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun insertSuppliers(data: List<Supplier>)

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun insertSupplier(data: Supplier): Long

	@Update
	fun updateSupplier(data: Supplier)

//	PAYMENTS

	@Query("SELECT * FROM ${Payment.DB_NAME}")
	fun getPayments(): LiveData<List<Payment>>

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun insertPayments(data: List<Payment>)

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun insertPayment(data: Payment): Long

	@Update
	fun updatePayment(data: Payment)

//	OUTCOME

	@Query("SELECT * FROM ${Outcome.DB_NAME} WHERE date(${Outcome.DATE}) = date(:date)")
    fun getOutcome(date: String): LiveData<List<Outcome>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOutcomes(data: List<Outcome>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOutcome(data: Outcome): Long

    @Update
    fun updateOutcome(data: Outcome)

    @Query("SELECT * FROM ${User.DB_NAME} WHERE ${User.ID} = :userId")
    fun getUser(userId: String): LiveData<User?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUsers(data: List<User>)
}
