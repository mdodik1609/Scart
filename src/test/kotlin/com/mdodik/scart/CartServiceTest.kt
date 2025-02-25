package com.mdodik.scart

import com.mdodik.scart.model.Cart
import com.mdodik.scart.repository.CartMongoRepository
import com.mdodik.scart.service.CartService
import com.mongodb.client.result.DeleteResult
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertThrows

class CartServiceTest {
    private lateinit var cartRepo: CartMongoRepository
    private lateinit var cartService: CartService

    @BeforeEach
    fun setup() {
        cartService = CartService(cartRepo)
    }

    @Test
    fun `getCartByCustomerId returns cart when found`() = runBlocking {
        val cart = Cart(id = 1, customerId = 123, items = mutableListOf())
        coEvery { cartRepo.findByCustomerId(123) } returns cart

        val result = cartService.getCartByCustomerId(123)
        assertEquals(cart, result)
    }

    @Test
    fun `getCartByCustomerId throws exception when not found`() = runBlocking {
        coEvery { cartRepo.findByCustomerId(123) } returns null

        val exception = assertThrows<NoSuchElementException> { runBlocking { cartService.getCartByCustomerId(123) } }
        assertEquals("Cart for customerId [123] not found.", exception.message)
    }

    @Test
    fun `deleteCart removes cart when found`() = runBlocking {
        val cart = Cart(id = 1, customerId = 123, items = mutableListOf())
        coEvery { cartRepo.findByCustomerId(123) } returns cart
        coEvery { cartRepo.delete(cart) } returns DeleteResult.acknowledged(1)

        val result = cartService.deleteCart(123)
        assertTrue(result)
    }

    @Test
    fun `deleteCart throws exception when cart not found`() = runBlocking {
        coEvery { cartRepo.findByCustomerId(123) } returns null

        assertThrows<NoSuchElementException> { runBlocking { cartService.deleteCart(123) } }
    }
}