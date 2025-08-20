package com.example.omisechallenge.data.repository

import com.example.omisechallenge.data.ApiResult
import com.example.omisechallenge.data.model.request.OrderRequest
import com.example.omisechallenge.data.model.response.OrderResponse
import com.example.omisechallenge.data.model.response.PaginationInfoResponse
import com.example.omisechallenge.data.model.response.ProductApiResponse
import com.example.omisechallenge.data.model.response.ProductDataResponse
import com.example.omisechallenge.data.model.response.ProductResponse
import com.example.omisechallenge.data.model.response.ProductResultResponse
import com.example.omisechallenge.data.model.response.StoreResponse
import com.example.omisechallenge.data.service.ApiService
import com.example.omisechallenge.domain.model.PaginationInfo
import com.example.omisechallenge.domain.model.Product
import com.example.omisechallenge.domain.model.Store
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class StoreRepositoryImplTest {
    @MockK(relaxed = true)
    lateinit var apiService: ApiService

    private lateinit var repository: StoreRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        repository = StoreRepositoryImpl(apiService)
    }

    @Test
    fun getStoreInfo_loadSuccessful_returnSuccess() {
        runTest {
            // Given
            val storeResponse = StoreResponse(
                name = "Mock Store",
                rating = 4.5,
                openingTime = "15:01:01.772Z",
                closingTime = "19:45:51.365Z"
            )

            val expectedStore = Store(
                name = "Mock Store",
                rating = 4.5,
                openingTime = "15:01:01.772Z",
                closingTime = "19:45:51.365Z"
            )

            val mockResponse = mockk<Response<StoreResponse>>(relaxed = true)

            every { mockResponse.isSuccessful } returns true
            every { mockResponse.body() } returns storeResponse
            coEvery { apiService.getStoreInfo() } returns mockResponse

            // When
            val result = repository.getStoreInfo()

            // Then
            assertTrue(result is ApiResult.Success)
            assertEquals(expectedStore.name, (result as ApiResult.Success).data.name)
            assertEquals(expectedStore.rating, result.data.rating)
            assertEquals(expectedStore.openingTime, result.data.openingTime)
            assertEquals(expectedStore.closingTime, result.data.closingTime)
        }
    }

    @Test
    fun getStoreInfo_loadFailed_withErrorBody_returnError() {
        runTest {
            // Given
            val errorBodyMessage = "Mock Error"
            val errorBody = errorBodyMessage.toResponseBody("application/json".toMediaTypeOrNull())
            val mockResponse = mockk<Response<StoreResponse>>(relaxed = true)

            every { mockResponse.isSuccessful } returns false
            every { mockResponse.code() } returns 500
            every { mockResponse.errorBody() } returns errorBody
            coEvery { apiService.getStoreInfo() } returns mockResponse

            // When
            val result = repository.getStoreInfo()

            // Then
            assertTrue(result is ApiResult.Error)
            assertEquals(500, (result as ApiResult.Error).code)
            assertEquals(errorBodyMessage, result.message)
        }
    }

    @Test
    fun getStoreInfo_loadFailed_withoutErrorBody_returnError() {
        runTest {
            // Given
            val mockResponse = mockk<Response<StoreResponse>>(relaxed = true)

            every { mockResponse.isSuccessful } returns false
            every { mockResponse.code() } returns 500
            every { mockResponse.errorBody() } returns null
            coEvery { apiService.getStoreInfo() } returns mockResponse

            // When
            val result = repository.getStoreInfo()

            // Then
            assertTrue(result is ApiResult.Error)
            assertEquals(500, (result as ApiResult.Error).code)
            assertEquals("Unknown error", result.message)
        }
    }

    @Test
    fun getStoreInfo_throwException_returnError() {
        runTest {
            // Given
            val errorBodyMessage = "Mock Exception"
            coEvery { apiService.getStoreInfo() } throws IOException(errorBodyMessage)

            // When
            val result = repository.getStoreInfo()

            // Then
            assertTrue(result is ApiResult.Error)
            assertEquals(-1, (result as ApiResult.Error).code)
            assertEquals(errorBodyMessage, result.message)
        }
    }

    @Test
    fun getStoreInfo_throwUnknownException_returnError() {
        runTest {
            // Given
            val mockResponse = mockk<Response<StoreResponse>>(relaxed = true)

            every { mockResponse.isSuccessful } returns true
            every { mockResponse.body() } returns null
            coEvery { apiService.getStoreInfo() } returns mockResponse

            // When
            val result = repository.getStoreInfo()

            // Then
            assertTrue(result is ApiResult.Error)
            assertEquals(-1, (result as ApiResult.Error).code)
            assertEquals("Unknown error", result.message)
        }
    }

    @Test
    fun getProducts_loadSuccessful_returnSuccess() {
        runTest {
            // Given
            val mockPaginationInfoResponse = PaginationInfoResponse(10, 1, 1)
            val mockProductListResponse = listOf(
                ProductResponse(1, "Mock Product", 30, "Mock Image Url")
            )
            val mockProductApiResponse = ProductApiResponse(
                data = ProductDataResponse(
                    productResultResponse = ProductResultResponse(mockPaginationInfoResponse, mockProductListResponse)
                )
            )

            val expectedPaginationInfo = PaginationInfo(10, 1, 1)
            val expectedProductList = listOf(
                Product(1, "Mock Product", 30, "Mock Image Url")
            )

            val mockResponse = mockk<Response<ProductApiResponse>>(relaxed = true)

            every { mockResponse.isSuccessful } returns true
            every { mockResponse.body() } returns mockProductApiResponse
            coEvery { apiService.getProducts() } returns mockResponse

            // When
            val result = repository.getProducts()

            // Then
            assertTrue(result is ApiResult.Success)
            assertEquals(expectedPaginationInfo.totalCount, (result as ApiResult.Success).data.paginationInfo.totalCount)
            assertEquals(expectedPaginationInfo.currentPage, result.data.paginationInfo.currentPage)
            assertEquals(expectedPaginationInfo.totalPages, result.data.paginationInfo.totalPages)

            assertEquals(1, result.data.productList.size)
            assertEquals(expectedProductList[0].id, result.data.productList[0].id)
            assertEquals(expectedProductList[0].name, result.data.productList[0].name)
            assertEquals(expectedProductList[0].price, result.data.productList[0].price)
            assertEquals(expectedProductList[0].imageUrl, result.data.productList[0].imageUrl)
        }
    }

    @Test
    fun getProducts_loadFailed_withErrorBody_returnError() {
        runTest {
            // Given
            val errorBodyMessage = "Mock Error"
            val errorBody = errorBodyMessage.toResponseBody("application/json".toMediaTypeOrNull())
            val mockResponse = mockk<Response<ProductApiResponse>>(relaxed = true)

            every { mockResponse.isSuccessful } returns false
            every { mockResponse.code() } returns 500
            every { mockResponse.errorBody() } returns errorBody
            coEvery { apiService.getProducts() } returns mockResponse

            // When
            val result = repository.getProducts()

            // Then
            assertTrue(result is ApiResult.Error)
            assertEquals(500, (result as ApiResult.Error).code)
            assertEquals(errorBodyMessage, result.message)
        }
    }

    @Test
    fun getProducts_loadFailed_withoutErrorBody_returnError() {
        runTest {
            // Given
            val mockResponse = mockk<Response<ProductApiResponse>>(relaxed = true)

            every { mockResponse.isSuccessful } returns false
            every { mockResponse.code() } returns 500
            every { mockResponse.errorBody() } returns null
            coEvery { apiService.getProducts() } returns mockResponse

            // When
            val result = repository.getProducts()

            // Then
            assertTrue(result is ApiResult.Error)
            assertEquals(500, (result as ApiResult.Error).code)
            assertEquals("Unknown error", result.message)
        }
    }

    @Test
    fun getProducts_throwException_returnError() {
        runTest {
            // Given
            val errorBodyMessage = "Mock Exception"
            coEvery { apiService.getProducts() } throws IOException(errorBodyMessage)

            // When
            val result = repository.getProducts()

            // Then
            assertTrue(result is ApiResult.Error)
            assertEquals(-1, (result as ApiResult.Error).code)
            assertEquals(errorBodyMessage, result.message)
        }
    }

    @Test
    fun getProducts_throwUnknownException_returnError() {
        runTest {
            // Given
            val mockResponse = mockk<Response<ProductApiResponse>>(relaxed = true)

            every { mockResponse.isSuccessful } returns true
            every { mockResponse.body() } returns null
            coEvery { apiService.getProducts() } returns mockResponse

            // When
            val result = repository.getProducts()

            // Then
            assertTrue(result is ApiResult.Error)
            assertEquals(-1, (result as ApiResult.Error).code)
            assertEquals("Unknown error", result.message)
        }
    }

    @Test
    fun makeOrder_loadSuccessful_returnSuccess() {
        runTest {
            // Given
            val mockOrderRequest = OrderRequest("Mock Product", 30, "Mock Address")
            val mockResponse = mockk<Response<OrderResponse>>(relaxed = true)

            every { mockResponse.isSuccessful } returns true
            coEvery { apiService.makeOrder(mockOrderRequest) } returns mockResponse

            // When
            val result = repository.makeOrder(mockOrderRequest)

            // Then
            assertTrue(result is ApiResult.Success)
        }
    }

    @Test
    fun makeOrder_loadFailed_withErrorBody_returnError() {
        runTest {
            // Given
            val errorBodyMessage = "Mock Error"
            val errorBody = errorBodyMessage.toResponseBody("application/json".toMediaTypeOrNull())
            val mockOrderRequest = OrderRequest("Mock Product", 30, "Mock Address")
            val mockResponse = mockk<Response<OrderResponse>>(relaxed = true)

            every { mockResponse.isSuccessful } returns false
            every { mockResponse.code() } returns 500
            every { mockResponse.errorBody() } returns errorBody
            coEvery { apiService.makeOrder(mockOrderRequest) } returns mockResponse

            // When
            val result = repository.makeOrder(mockOrderRequest)

            // Then
            assertTrue(result is ApiResult.Error)
            assertEquals(500, (result as ApiResult.Error).code)
            assertEquals(errorBodyMessage, result.message)
        }
    }

    @Test
    fun makeOrder_loadFailed_withoutErrorBody_returnError() {
        runTest {
            // Given
            val mockOrderRequest = OrderRequest("Mock Product", 30, "Mock Address")
            val mockResponse = mockk<Response<OrderResponse>>(relaxed = true)

            every { mockResponse.isSuccessful } returns false
            every { mockResponse.code() } returns 500
            every { mockResponse.errorBody() } returns null
            coEvery { apiService.makeOrder(mockOrderRequest) } returns mockResponse

            // When
            val result = repository.makeOrder(mockOrderRequest)

            // Then
            assertTrue(result is ApiResult.Error)
            assertEquals(500, (result as ApiResult.Error).code)
            assertEquals("Unknown error", result.message)
        }
    }

    @Test
    fun makeOrder_throwException_returnError() {
        runTest {
            // Given
            val mockOrderRequest = OrderRequest("Mock Product", 30, "Mock Address")
            val errorBodyMessage = "Mock Exception"
            coEvery { apiService.makeOrder(mockOrderRequest) } throws IOException(errorBodyMessage)

            // When
            val result = repository.makeOrder(mockOrderRequest)

            // Then
            assertTrue(result is ApiResult.Error)
            assertEquals(-1, (result as ApiResult.Error).code)
            assertEquals(errorBodyMessage, result.message)
        }
    }

    @Test
    fun makeOrder_throwUnknownException_returnError() {
        runTest {
            // Given
            val mockOrderRequest = OrderRequest("Mock Product", 30, "Mock Address")
            coEvery { apiService.makeOrder(mockOrderRequest) } throws IOException()

            // When
            val result = repository.makeOrder(mockOrderRequest)

            // Then
            assertTrue(result is ApiResult.Error)
            assertEquals(-1, (result as ApiResult.Error).code)
            assertEquals("Unknown error", result.message)
        }
    }
}