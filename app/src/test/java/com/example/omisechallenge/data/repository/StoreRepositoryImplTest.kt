package com.example.omisechallenge.data.repository

import com.example.omisechallenge.data.ApiResult
import com.example.omisechallenge.data.model.request.OrderRequest
import com.example.omisechallenge.data.model.response.ProductResponse
import com.example.omisechallenge.data.model.response.StoreResponse
import com.example.omisechallenge.data.service.ApiService
import com.example.omisechallenge.domain.model.Product
import com.example.omisechallenge.domain.model.Store
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
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

            coEvery { apiService.getStoreInfo() } returns Response.success(storeResponse)

            // When
            val result = repository.getStoreInfo()

            // Then
            assertTrue(result is ApiResult.Success)
            assertEquals(expectedStore.name, (result as ApiResult.Success).data.name)
            assertEquals(expectedStore.rating, result.data.rating)
            assertEquals(expectedStore.openingTime, result.data.openingTime)
            assertEquals(expectedStore.closingTime, result.data.closingTime
            )
        }
    }

    @Test
    fun getStoreInfo_loadFailed_withErrorBody_returnError() {
        runTest {
            // Given
            val errorBodyMessage = "Mock Error"
            val errorBody = errorBodyMessage.toResponseBody("application/json".toMediaTypeOrNull())

            coEvery { apiService.getStoreInfo() } returns Response.error(500, errorBody)

            // When
            val result = repository.getStoreInfo()

            // Then
            assertTrue(result is ApiResult.Error)
            assertEquals(500, (result as ApiResult.Error).code)
            assertEquals(errorBodyMessage, result.message)
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
    fun getProducts_loadSuccessful_haveProducts_returnSuccess() {
        runTest {
            // Given
            val productListResponse = listOf(ProductResponse(
                name = "Mock Product",
                price = 30,
                imageUrl = "Mock Image Url"
            ))

            val expectedProductList = listOf(Product(
                name = "Mock Product",
                price = 30,
                imageUrl = "Mock Image Url"
            ))

            coEvery { apiService.getProducts() } returns Response.success(productListResponse)

            // When
            val result = repository.getProducts()

            // Then
            assertTrue(result is ApiResult.Success)
            assertEquals(1, (result as ApiResult.Success).data.size)
            assertEquals(expectedProductList[0].name, result.data[0].name)
            assertEquals(expectedProductList[0].price, result.data[0].price)
            assertEquals(expectedProductList[0].imageUrl, result.data[0].imageUrl)
        }
    }

    @Test
    fun getProducts_loadSuccessful_haveNoProduct_returnSuccess() {
        runTest {
            // Given
            coEvery { apiService.getProducts() } returns Response.success(null)

            // When
            val result = repository.getProducts()

            // Then
            assertTrue(result is ApiResult.Success)
            assertEquals(0, (result as ApiResult.Success).data.size)
        }
    }

    @Test
    fun getProducts_loadFailed_withErrorBody_returnError() {
        runTest {
            // Given
            val errorBodyMessage = "Mock Error"
            val errorBody = errorBodyMessage.toResponseBody("application/json".toMediaTypeOrNull())

            coEvery { apiService.getProducts() } returns Response.error(500, errorBody)

            // When
            val result = repository.getProducts()

            // Then
            assertTrue(result is ApiResult.Error)
            assertEquals(500, (result as ApiResult.Error).code)
            assertEquals(errorBodyMessage, result.message)
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
    fun makeOrder_loadSuccessful_returnSuccess() {
        runTest {
            // Given
            val orderRequest = OrderRequest(
                products = listOf(Product(
                    name = "Mock Product",
                    price = 30,
                    imageUrl = "Mock Image Url"
                )),
                delivery_address = "Mock Address"
            )

            val responseBody = "".toResponseBody("application/json".toMediaTypeOrNull())
            coEvery { apiService.makeOrder(orderRequest) } returns Response.success(responseBody)

            // When
            val result = repository.makeOrder(orderRequest)

            // Then
            assertTrue(result is ApiResult.Success)
        }
    }

    @Test
    fun makeOrder_loadFailed_withErrorBody_returnError() {
        runTest {
            // Given
            val orderRequest = OrderRequest(
                products = listOf(Product(
                    name = "Mock Product",
                    price = 30,
                    imageUrl = "Mock Image Url"
                )),
                delivery_address = "Mock Address"
            )

            val errorBodyMessage = "Mock Error"
            val errorBody = errorBodyMessage.toResponseBody("application/json".toMediaTypeOrNull())

            coEvery { apiService.makeOrder(orderRequest) } returns Response.error(500, errorBody)

            // When
            val result = repository.makeOrder(orderRequest)

            // Then
            assertTrue(result is ApiResult.Error)
            assertEquals(500, (result as ApiResult.Error).code)
            assertEquals(errorBodyMessage, result.message)
        }
    }

    @Test
    fun makeOrder_throwException_returnError() {
        runTest {
            // Given
            val orderRequest = OrderRequest(
                products = listOf(Product(
                    name = "Mock Product",
                    price = 30,
                    imageUrl = "Mock Image Url"
                )),
                delivery_address = "Mock Address"
            )

            val errorBodyMessage = "Mock Exception"
            coEvery { apiService.makeOrder(orderRequest) } throws IOException(errorBodyMessage)

            // When
            val result = repository.makeOrder(orderRequest)

            // Then
            assertTrue(result is ApiResult.Error)
            assertEquals(-1, (result as ApiResult.Error).code)
            assertEquals(errorBodyMessage, result.message)
        }
    }

}