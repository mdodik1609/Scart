package com.mdodik.scart.service

import com.mdodik.scart.model.Cart
import com.mdodik.scart.model.Item
import com.mdodik.scart.model.ItemAction
import com.mdodik.scart.model.Price
import com.mdodik.scart.repository.CartMongoRepository
import com.mongodb.MongoException
import jakarta.inject.Singleton
import java.time.LocalDateTime
import java.util.Random

@Singleton
class CartService(
    private val cartRepo: CartMongoRepository,
    //private val idGeneratorService: IdGeneratorService,
) {

    private suspend fun getCartById(cartId: Int): Cart {
        return cartRepo.findById(cartId)
            ?: throw NoSuchElementException("Cart for id [$cartId] not found.")
    }

    suspend fun getCartByCustomerId(customerId: Int): Cart {
        return cartRepo.findByCustomerId(customerId)
            ?: throw NoSuchElementException("Cart for customerId [$customerId] not found.")
    }

    suspend fun deleteCart(customerId: Int): Boolean {
        val cart = getCartByCustomerId(customerId)
        return cartRepo.delete(cart).wasAcknowledged()
    }

    suspend fun addItemToCart(cartId: Int, productId: Int, action: ItemAction?, price: Price?):Cart {
        var cart = getCartById(cartId)
        val itemPrice = price ?:
            when (Random().nextInt() % 2 == 0){
                true -> Price.OneTimePrice(
                    value = Random().nextDouble()
                )
                else -> Price.RecurringPrice(
                    recurrences = 2,
                    value = Random().nextDouble()
                )
            }
        val itemAction = action ?:
            when (Random().nextInt() % 3) {
                0 -> ItemAction.ADD
                1 -> ItemAction.MODIFY
                else -> ItemAction.DELETE
            }
        val item = Item(
                productId = productId,
                action = itemAction,
                price =  itemPrice,
                createdAt = LocalDateTime.now(),
        )
        cart.items += item
        return cartRepo.update(cart)
            .takeIf { it.wasAcknowledged() }
            ?.let { cart }
            ?: throw MongoException("Cart not updated.")
    }

    suspend fun removeItemFromCart(customerId: Int, productId: Int):Cart {
        val cart = getCartByCustomerId(customerId)
        val item: List<Item> = cart.items.filter { it.productId == productId }
        cart.items.removeAll(item)
        return cartRepo.update(cart)
            .takeIf { it.wasAcknowledged() }
            ?.let { cart }
            ?: throw MongoException("Cart not updated.")
    }
}