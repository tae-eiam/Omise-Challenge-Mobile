package com.example.omisechallenge.domain.usecase

import com.example.omisechallenge.data.ApiResult
import com.example.omisechallenge.data.model.request.OrderRequest
import com.example.omisechallenge.data.repository.StoreRepository
import com.example.omisechallenge.domain.model.Product
import com.example.omisechallenge.domain.model.Store
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StoreUseCaseImplTest {
    @MockK(relaxed = true)
    lateinit var storeRepository: StoreRepository

    private lateinit var storeUseCase: StoreUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        storeUseCase = StoreUseCaseImpl(storeRepository)
    }

    @Test
    fun getStoreInfo_onSuccess_returnsSuccess() {
        runTest {
            // Given
            val mockStore = Store(
                name = "Mock Store",
                rating = 4.5,
                openingTime = "15:01:01.772Z",
                closingTime = "19:45:51.365Z"
            )
            val expectedResult = ApiResult.Success(mockStore)

            coEvery { storeRepository.getStoreInfo() } returns expectedResult

            // When
            val result = storeUseCase.getStoreInfo()

            // Then
            assertTrue(result is ApiResult.Success)
            assertEquals(mockStore, (result as ApiResult.Success).data)

            coVerify(exactly = 1) {
                storeRepository.getStoreInfo()
            }
        }
    }

    @Test
    fun getStoreInfo_onError_returnsError() {
        runTest {
            // Given
            val errorMessage = "Mock Error"
            val expectedResult = ApiResult.Error(500, errorMessage)

            coEvery { storeRepository.getStoreInfo() } returns expectedResult

            // When
            val result = storeUseCase.getStoreInfo()

            // Then
            assertTrue(result is ApiResult.Error)
            assertEquals(500, (result as ApiResult.Error).code)
            assertEquals(errorMessage, result.message)

            coVerify(exactly = 1) {
                storeRepository.getStoreInfo()
            }
        }
    }

    @Test
    fun getProducts_onSuccess_returnsSuccess() {
        runTest {
            // Given
            val mockProductList = listOf(
                Product(
                    name = "Mock Product",
                    price = 30,
                    imageUrl = "Mock Image Url"
                )
            )
            val expectedResult = ApiResult.Success(mockProductList)

            coEvery { storeRepository.getProducts() } returns expectedResult

            // When
            val result = storeUseCase.getProducts()

            // Then
            assertTrue(result is ApiResult.Success)
            assertEquals(mockProductList, (result as ApiResult.Success).data)

            coVerify(exactly = 1) {
                storeRepository.getProducts()
            }
        }
    }

    @Test
    fun getProducts_onError_returnsError() {
        runTest {
            // Given
            val errorMessage = "Mock Error"
            val expectedResult = ApiResult.Error(500, errorMessage)

            coEvery { storeRepository.getProducts() } returns expectedResult

            // When
            val result = storeUseCase.getProducts()

            // Then
            assertTrue(result is ApiResult.Error)
            assertEquals(500, (result as ApiResult.Error).code)
            assertEquals(errorMessage, result.message)

            coVerify(exactly = 1) {
                storeRepository.getProducts()
            }
        }
    }

    @Test
    fun makeOrder_onSuccess_returnsSuccess() {
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
            val expectedResult = ApiResult.Success(Unit)

            coEvery { storeRepository.makeOrder(orderRequest) } returns expectedResult

            // When
            val result = storeUseCase.makeOrder(orderRequest)

            // Then
            assertTrue(result is ApiResult.Success)

            coVerify(exactly = 1) {
                storeRepository.makeOrder(orderRequest)
            }
        }
    }

    @Test
    fun makeOrder_onError_returnsError() {
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

            val errorMessage = "Mock Error"
            val expectedResult = ApiResult.Error(500, errorMessage)

            coEvery { storeRepository.makeOrder(orderRequest) } returns expectedResult

            // When
            val result = storeUseCase.makeOrder(orderRequest)

            // Then
            assertTrue(result is ApiResult.Error)
            assertEquals(500, (result as ApiResult.Error).code)
            assertEquals(errorMessage, result.message)

            coVerify(exactly = 1) {
                storeRepository.makeOrder(orderRequest)
            }
        }
    }
}