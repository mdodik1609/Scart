package com.mdodik.scart.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = Price.RecurringPrice::class, name = "recurring"),
    JsonSubTypes.Type(value = Price.OneTimePrice::class, name = "oneTime")
)
sealed class Price {
    data class OneTimePrice(
        @JsonProperty("value") val value: Double,
    ) : Price()
    data class RecurringPrice(
        @JsonProperty("recurrences") val recurrences: Int,
        @JsonProperty("value") val value: Double,
    ) : Price()
}