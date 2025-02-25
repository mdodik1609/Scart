package com.mdodik.scart.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class Item (
    @JsonProperty("productId") val productId: Int,
    @JsonProperty("action") val action: ItemAction,
    @JsonProperty("price") val price: Price,
    @JsonProperty("createdAt") val createdAt: LocalDateTime,
)

enum class ItemAction {
    ADD, MODIFY, DELETE
}