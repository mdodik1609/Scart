package com.mdodik.scart

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
class StatsControllerIntegrationTest {
    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun `getCartForUser returns stats`() = runBlocking {
        val request = HttpRequest.GET<Any>("/v1/stats?productId=1&action=ADD&startDate=2023-01-01T00:00:00&endDate=2030-01-01T00:00:00")
        val response = client.toBlocking().exchange(request, Long::class.java)
        assertEquals(200, response.status.code)
    }
}