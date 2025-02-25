package com.mdodik.scart.service

import com.mdodik.scart.model.ProductStats
import com.mdodik.scart.repository.CartMongoRepository
import jakarta.inject.Singleton

@Singleton
class StatsService(
    private val cartRepo: CartMongoRepository,
) {
    suspend fun getStats(productService: ProductStats) =
        cartRepo.countByProductIdAndActionAndTimestampBetween(productService)

}