package com.mdodik.scart

import com.mdodik.scart.model.Cart
import com.mdodik.scart.model.Price
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject

@MicronautTest
class CartControllerIntegrationTest {
    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun `getCartForUser returns cart when found`() = runBlocking {
        val request = HttpRequest.GET<Any>("/v1/cart?customerId=123")
        val response = client.toBlocking().exchange(request, Cart::class.java)
        assertEquals(200, response.status.code)
    }

    @Test
    fun `addItemToCart successfully adds an item`() = runBlocking {
        val request = HttpRequest.POST("/v1/cart/1/item/2", Price.OneTimePrice(10.0))
        val response = client.toBlocking().exchange(request, Cart::class.java)
        assertEquals(200, response.status.code)
    }

    @Test
    fun `removeItemFromCart successfully removes an item`() = runBlocking {
        val request = HttpRequest.DELETE<Any>("/v1/cart/1/item/2")
        val response = client.toBlocking().exchange(request, Cart::class.java)
        assertEquals(200, response.status.code)
    }

    @Test
    fun `deleteCart successfully deletes a cart`() = runBlocking {
        val request = HttpRequest.DELETE<Any>("/v1/cart?customerId=123")
        val response = client.toBlocking().exchange(request, Boolean::class.java)
        assertEquals(200, response.status.code)
    }
}