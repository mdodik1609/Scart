package com.mdodik.scart.service

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicInteger
import com.mdodik.scart.repository.CartMongoRepository
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Singleton
class IdGeneratorService(
    private val cartRepo: CartMongoRepository,
) {

    init {
        CoroutineScope(Dispatchers.Default).launch {
            initializeNextId()
        }
    }

    private suspend fun initializeNextId() {
        val initialSize = cartRepo.size()
        nextId.set((initialSize + 1).toInt())
    }

    private val nextId = AtomicInteger(1)
    private val mutex = Mutex()

    suspend fun createNewCartId(): Int = mutex.withLock {
        var newId: Int
        do {
            newId = nextId.getAndIncrement()
        } while (cartRepo.findById(newId) != null)
        return newId
    }

}