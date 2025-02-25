package com.mdodik.scart.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class ProductStats(
    @JsonProperty("productId") val productId: Int,
    @JsonProperty("action") val action: ItemAction,
    @JsonProperty("startDate") val startDate: LocalDateTime = LocalDateTime.MIN,
    @JsonProperty("endDate") val endDate: LocalDateTime = LocalDateTime.MAX,
)