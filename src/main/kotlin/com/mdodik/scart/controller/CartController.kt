package com.mdodik.scart.controller

import com.mdodik.scart.util.HasLog
import com.mdodik.scart.model.Cart
import com.mdodik.scart.model.ItemAction
import com.mdodik.scart.model.Price
import com.mdodik.scart.service.CartService
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.http.server.cors.CrossOrigin
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule

@Controller("/v1/cart", produces = ["application/json"], consumes = ["application/json"])
@Secured(SecurityRule.IS_ANONYMOUS)
class CartController(
    private val cartService: CartService
): HasLog() {

    @CrossOrigin(allowedOriginsRegex = ".*")
    @Get
//    @RolesAllowed(UserRole.ADMIN)
    suspend fun getCartForUser(
        @QueryValue customerId: Int,
    ): MutableHttpResponse<*> {
        log.info("[GET /$customerId] find cart for user")
        return try {
            val cart = cartService.getCartByCustomerId(customerId)
            HttpResponse.ok(
                cart
            )
        } catch (e: IllegalArgumentException) {
            HttpResponse.badRequest(
                e.message
            )
        }
    }

    @CrossOrigin(allowedOriginsRegex = ".*")
    @Post("/{cartId}/item/{itemId}")
    suspend fun addItemToCart(
        @PathVariable cartId: Int,
        @PathVariable itemId: Int,
        @QueryValue action: ItemAction?,
        @Body price: Price?,
        request: HttpRequest<Cart>,
    ): MutableHttpResponse<*> {
        log.info("[${request.path}] adding item $itemId to cart $cartId")
        return try {
            val cart =
                cartService.addItemToCart(
                    cartId = cartId,
                    productId = itemId,
                    action = action,
                    price = price
                )
            HttpResponse.ok(
                cart
            )
        } catch (e: IllegalArgumentException) {
            HttpResponse.badRequest(
                e.message
            )
        }
    }

    @CrossOrigin(allowedOriginsRegex = ".*")
    @Delete("/{cartCustomerId}/item/{productId}")
    suspend fun removeItemFromCart(
        @PathVariable cartCustomerId: Int,
        @PathVariable productId: Int,
        request: HttpRequest<Cart>,
    ): MutableHttpResponse<*> {
        log.info("[${request.path}] remove item $productId from cart $cartCustomerId")
        return try {
            val cart = cartService.removeItemFromCart(cartCustomerId, productId)
            HttpResponse.ok(
                cart
            )
        } catch (e: IllegalArgumentException) {
            HttpResponse.badRequest(
                e.message
            )
        }
    }
    @CrossOrigin(allowedOriginsRegex = ".*")
    @Delete
    suspend fun deleteCart(
        @QueryValue customerId: Int,
        request: HttpRequest<Cart>,
    ): MutableHttpResponse<*> {
        log.info("[${request.path}] delete cart $customerId")
        return try {
            val cart = cartService.deleteCart(customerId)
            HttpResponse.ok(
                cart
            )
        } catch (e: IllegalArgumentException) {
            HttpResponse.badRequest(
                e.message
            )
        }
    }

}