package com.mdodik.scart

import com.mdodik.scart.model.ItemAction
import com.mdodik.scart.model.ProductStats
import com.mdodik.scart.repository.CartMongoRepository
import com.mdodik.scart.service.StatsService
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import io.mockk.*

class StatsServiceTest {
    private lateinit var cartRepo: CartMongoRepository
    private lateinit var statsService: StatsService

    @BeforeEach
    fun setup() {
        cartRepo = mockk()
        statsService = StatsService(cartRepo)
    }

    @Test
    suspend fun `getStats returns count`() = runBlocking {
        val productStats = ProductStats(1, ItemAction.ADD, LocalDateTime.now().minusDays(7), LocalDateTime.now())
        coEvery { cartRepo.countByProductIdAndActionAndTimestampBetween(productStats) } returns 10L

        val result = statsService.getStats(productStats)
        assertEquals(10L, result)
    }
}