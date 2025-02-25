package com.mdodik.scart.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Cart (
    @io.micronaut.data.annotation.Id @JsonProperty("_id") var id: Int,
    @JsonProperty("customerId") var customerId: Int,
    @JsonProperty("items") var items: MutableList<Item> = mutableListOf(),
)