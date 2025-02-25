package com.mdodik.scart.repository

import com.mdodik.scart.model.Cart
import com.mdodik.scart.model.Item
import com.mdodik.scart.model.ProductStats
import com.mongodb.client.model.Filters
import jakarta.inject.Singleton
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.div
import org.litote.kmongo.eq
import org.litote.kmongo.gte
import org.litote.kmongo.lte

@Singleton
class CartMongoRepository(
    private val cartCollection: CoroutineCollection<Cart>,
) {
    suspend fun save(cart: Cart) = cartCollection.save(cart)

    suspend fun update(cart: Cart) = cartCollection.updateOneById(cart.id, cart)

    suspend fun findById(cartId: Int) = cartCollection.findOne(Cart::id eq cartId)

    suspend fun findByCustomerId(customerId: Int) = cartCollection.findOne(Cart::customerId eq customerId)

    suspend fun delete(cart: Cart) = cartCollection.deleteOne(Cart::id eq cart.id)

    suspend fun countByProductIdAndActionAndTimestampBetween(productStats: ProductStats): Long {
        val filter = Filters.and(
            Cart::items / Item::productId eq productStats.productId,
            Cart::items / Item::action eq productStats.action,
            Cart::items / Item::createdAt gte productStats.startDate,
            Cart::items / Item::createdAt lte productStats.endDate,
        )
        return cartCollection.countDocuments(filter)
    }

    suspend fun size() = cartCollection.countDocuments()

}