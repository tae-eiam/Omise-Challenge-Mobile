package com.example.omisechallenge.data.model.response

import junit.framework.TestCase.assertEquals
import org.junit.Test

class ProductResponseTest {
    @Test
    fun toProductResult_withValues_returnProductResult() {
        // Given
        val paginationInfoResponse = PaginationInfoResponse(10, 1, 1)
        val productListResponse = listOf(
            ProductResponse(1, "Mock Name", 100, "Mock Image Url")
        )
        val productResultResponse = ProductResultResponse(paginationInfoResponse, productListResponse)

        // When
        val result = productResultResponse.toProductResult()

        // Then
        assertEquals(paginationInfoResponse.totalCount, result.paginationInfo.totalCount)
        assertEquals(paginationInfoResponse.currentPage, result.paginationInfo.currentPage)
        assertEquals(paginationInfoResponse.totalPages, result.paginationInfo.totalPages)

        assertEquals(1, result.productList.size)
        assertEquals(productListResponse[0].id, result.productList[0].id)
        assertEquals(productListResponse[0].name, result.productList[0].name)
        assertEquals(productListResponse[0].price, result.productList[0].price)
        assertEquals(productListResponse[0].imageUrl, result.productList[0].imageUrl)
    }

    @Test
    fun toProductResult_withoutValues_returnProductResult() {
        // Given
        val productResultResponse = ProductResultResponse(null, null)

        // When
        val result = productResultResponse.toProductResult()

        // Then
        assertEquals(0, result.paginationInfo.totalCount)
        assertEquals(0, result.paginationInfo.currentPage)
        assertEquals(0, result.paginationInfo.totalPages)
        assertEquals(0, result.productList.size)
    }

    @Test
    fun toPaginationInfo_withValues_returnPaginationInfo() {
        // Given
        val paginationInfoResponse = PaginationInfoResponse(10, 1, 1)

        // When
        val result = paginationInfoResponse.toPaginationInfo()

        // Then
        assertEquals(paginationInfoResponse.totalCount, result.totalCount)
        assertEquals(paginationInfoResponse.currentPage, result.currentPage)
        assertEquals(paginationInfoResponse.totalPages, result.totalPages)
    }

    @Test
    fun toPaginationInfo_withoutValues_returnPaginationInfo() {
        // Given
        val paginationInfoResponse = PaginationInfoResponse(null, null, null)

        // When
        val result = paginationInfoResponse.toPaginationInfo()

        // Then
        assertEquals(0, result.totalCount)
        assertEquals(0, result.currentPage)
        assertEquals(0, result.totalPages)
    }

    @Test
    fun toProduct_withValues_returnProduct() {
        // Given
        val productResponse = ProductResponse(1, "Mock Name", 100, "Mock Image Url")

        // When
        val result = productResponse.toProduct()

        // Then
        assertEquals(productResponse.id, result.id)
        assertEquals(productResponse.name, result.name)
        assertEquals(productResponse.price, result.price)
        assertEquals(productResponse.imageUrl, result.imageUrl)
    }

    @Test
    fun toProduct_withoutValues_returnProduct() {
        // Given
        val productResponse = ProductResponse(null, null, null, null)

        // When
        val result = productResponse.toProduct()

        // Then
        assertEquals(0, result.id)
        assertEquals("", result.name)
        assertEquals(0, result.price)
        assertEquals("", result.imageUrl)
    }
}