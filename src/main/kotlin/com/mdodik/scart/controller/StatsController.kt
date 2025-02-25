package com.mdodik.scart.controller

import com.mdodik.scart.model.ItemAction
import com.mdodik.scart.util.HasLog
import com.mdodik.scart.model.ProductStats
import com.mdodik.scart.service.StatsService
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.server.cors.CrossOrigin
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import java.time.LocalDateTime

@Controller("/v1/stats", produces = ["application/json"], consumes = ["application/json"])
//@Secured(SecurityRule.IS_AUTHENTICATED)
@Secured(SecurityRule.IS_ANONYMOUS)
class StatsController(
    private val statsService: StatsService,
): HasLog() {

    @CrossOrigin(allowedOriginsRegex = ".*")
    @Get
//    @RolesAllowed(UserRole.ADMIN)
    suspend fun getCartForUser(
        httpRequest: HttpRequest<Any>,
        @QueryValue productId: Int,
        @QueryValue action: ItemAction,
        @QueryValue(defaultValue = "0001-01-01T00:00:00") startDate: LocalDateTime,
        @QueryValue(defaultValue = "2030-01-01T00:00:00") endDate: LocalDateTime,
    ): MutableHttpResponse<*> {
        log.info("[GET ${httpRequest.path}] get stats endpoint") //if it was secured, log which user called this
        val productStats = ProductStats(
            productId = productId,
            action = action,
            startDate = startDate,
            endDate = endDate,
        )
        return try {
            val stats = statsService.getStats(productStats)
            HttpResponse.ok(
                stats
            )
        } catch (e: IllegalArgumentException) {
            HttpResponse.badRequest(
                e.message
            )
        }
    }

}