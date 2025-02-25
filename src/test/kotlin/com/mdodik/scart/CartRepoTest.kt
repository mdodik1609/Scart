package com.mdodik.scart

import com.mdodik.scart.model.Cart
import com.mdodik.scart.model.ItemAction
import com.mdodik.scart.model.ProductStats
import com.mdodik.scart.repository.CartMongoRepository
import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq
import java.time.LocalDateTime

class CartRepoTest {
    private lateinit var cartCollection: CoroutineCollection<Cart>
    private lateinit var cartRepo: CartMongoRepository

    @BeforeEach
    fun setup() {
        cartCollection = mockk()
        cartRepo = CartMongoRepository(cartCollection)
    }

    @Test
    fun `update modifies a cart`() = runBlocking {
        val cart = Cart(id = 1, customerId = 123, items = mutableListOf())
        coEvery { cartCollection.updateOneById(cart.id, cart) } returns UpdateResult.acknowledged(1, 1, null)

        val result = cartRepo.update(cart)
        assertNotNull(result)
    }

    @Test
    fun `findById retrieves a cart when exists`() = runBlocking {
        val cart = Cart(id = 1, customerId = 123, items = mutableListOf())
        coEvery { cartCollection.findOne(Cart::id eq 1) } returns cart

        val result = cartRepo.findById(1)
        assertEquals(cart, result)
    }

    @Test
    fun `findByCustomerId retrieves a cart when exists`() = runBlocking {
        val cart = Cart(id = 1, customerId = 123, items = mutableListOf())
        coEvery { cartCollection.findOne(Cart::customerId eq 123) } returns cart

        val result = cartRepo.findByCustomerId(123)
        assertEquals(cart, result)
    }

    @Test
    fun `delete removes a cart`() = runBlocking {
        val cart = Cart(id = 1, customerId = 123, items = mutableListOf())
        coEvery { cartCollection.deleteOne(Cart::id eq cart.id) } returns DeleteResult.acknowledged(1)

        val result = cartRepo.delete(cart)
        assertNotNull(result)
    }

    @Test
    fun `countByProductIdAndActionAndTimestampBetween returns correct count`() = runBlocking {
        val productStats = ProductStats(1, ItemAction.ADD, LocalDateTime.now().minusDays(7), LocalDateTime.now())
        coEvery { cartCollection.countDocuments() } returns 5L

        val result = cartRepo.countByProductIdAndActionAndTimestampBetween(productStats)
        assertEquals(5L, result)
    }

    @Test
    fun `size returns correct document count`() = runBlocking {
        coEvery { cartCollection.countDocuments() } returns 100L

        val result = cartRepo.size()
        assertEquals(100L, result)
    }
}

