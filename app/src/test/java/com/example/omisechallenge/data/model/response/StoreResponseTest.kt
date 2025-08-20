package com.example.omisechallenge.data.model.response

import junit.framework.TestCase.assertEquals
import org.junit.Test

class StoreResponseTest {
    @Test
    fun toStore_withValues_returnStore() {
        // Given
        val storeResponse = StoreResponse("Mock Name", 4.0, "Mock Opening Time", "Mock Closing Time")

        // When
        val result = storeResponse.toStore()

        // Then
        assertEquals(storeResponse.name, result.name)
        assertEquals(storeResponse.rating, result.rating)
        assertEquals(storeResponse.openingTime, result.openingTime)
        assertEquals(storeResponse.closingTime, result.closingTime)
    }

    @Test
    fun toStore_withoutValues_returnStore() {
        // Given
        val storeResponse = StoreResponse(null, null, null, null)

        // When
        val result = storeResponse.toStore()

        // Then
        assertEquals("", result.name)
        assertEquals(0.0, result.rating)
        assertEquals("", result.openingTime)
        assertEquals("", result.closingTime)

    }
}