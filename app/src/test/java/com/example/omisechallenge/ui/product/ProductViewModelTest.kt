package com.example.omisechallenge.ui.product

import com.example.omisechallenge.data.ApiResult
import com.example.omisechallenge.domain.model.Product
import com.example.omisechallenge.domain.model.Store
import com.example.omisechallenge.domain.usecase.StoreUseCase
import com.example.omisechallenge.ui.UiState
import com.example.omisechallenge.ui.model.Order
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductViewModelTest {
    @MockK(relaxed = true)
    lateinit var storeUseCase: StoreUseCase

    private lateinit var productViewModel: ProductViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        Dispatchers.setMain(StandardTestDispatcher())
        productViewModel = ProductViewModel(storeUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadStoreInfo_onSuccess_setSuccessState() {
        runTest {
            // Given
            val mockStore = Store(
                name = "Mock Store",
                rating = 4.5,
                openingTime = "15:01:01.772Z",
                closingTime = "19:45:51.365Z"
            )

            coEvery { storeUseCase.getStoreInfo() } coAnswers {
                delay(500)
                ApiResult.Success(mockStore)
            }

            val states = mutableListOf<UiState<Any>>()
            val job = launch {
                productViewModel.storeState.toList(states)
            }

            // When
            productViewModel.loadStoreInfo()
            advanceUntilIdle()

            // Then
            assertEquals(3, states.size)
            assertEquals(UiState.Idle, states[0])
            assertEquals(UiState.Loading, states[1])
            assertEquals(UiState.Success(mockStore), states[2])

            job.cancel()
        }
    }

    @Test
    fun loadStoreInfo_onError_setErrorState() {
        runTest {
            // Given
            coEvery { storeUseCase.getStoreInfo() } coAnswers {
                delay(500)
                ApiResult.Error(500, "Mock Error")
            }

            val states = mutableListOf<UiState<Any>>()
            val job = launch {
                productViewModel.storeState.toList(states)
            }

            // When
            productViewModel.loadStoreInfo()
            advanceUntilIdle()

            // Then
            assertEquals(3, states.size)
            assertEquals(UiState.Idle, states[0])
            assertEquals(UiState.Loading, states[1])
            assertEquals(UiState.Error("Mock Error"), states[2])

            job.cancel()
        }
    }

    @Test
    fun loadProducts_onSuccess_setSuccessState() {
        runTest {
            // Given
            val mockProductList = listOf(
                Product(
                    name = "Mock Product",
                    price = 30,
                    imageUrl = "Mock Image Url"
                )
            )

            coEvery { storeUseCase.getProducts() } coAnswers {
                delay(500)
                ApiResult.Success(mockProductList)
            }

            val states = mutableListOf<UiState<Any>>()
            val job = launch {
                productViewModel.productState.toList(states)
            }

            // When
            productViewModel.loadProducts()
            advanceUntilIdle()

            // Then
            assertEquals(3, states.size)
            assertEquals(UiState.Idle, states[0])
            assertEquals(UiState.Loading, states[1])
            assertEquals(UiState.Success(mockProductList), states[2])

            job.cancel()
        }
    }

    @Test
    fun loadProducts_onError_setErrorState() {
        runTest {
            // Given
            coEvery { storeUseCase.getProducts() } coAnswers {
                delay(500)
                ApiResult.Error(500, "Mock Error")
            }

            val states = mutableListOf<UiState<Any>>()
            val job = launch {
                productViewModel.productState.toList(states)
            }

            // When
            productViewModel.loadProducts()
            advanceUntilIdle()

            // Then
            assertEquals(3, states.size)
            assertEquals(UiState.Idle, states[0])
            assertEquals(UiState.Loading, states[1])
            assertEquals(UiState.Error("Mock Error"), states[2])

            job.cancel()
        }
    }

    @Test
    fun convertProductListToOrderList_emptyProductList_emptySelectedOrder_returnEmptyOrderList() {
        // Given
        val productList = listOf<Product>()

        // When
        val result = productViewModel.convertProductListToOrderList(productList)

        // Then
        assertEquals(0, result.size)
    }

    @Test
    fun convertProductListToOrderList_emptyProductList_haveSelectedOrder_returnEmptyOrderList() {
        // Given
        val productList = listOf<Product>()
        val selectedOrder = Order(
            name = "Mock Order 1",
            price = 30,
            imageUrl = "Mock Image Url",
            amount = 3
        )

        productViewModel.updateOrder(selectedOrder)

        // When
        val result = productViewModel.convertProductListToOrderList(productList)

        // Then
        assertEquals(0, result.size)
    }

    @Test
    fun convertProductListToOrderList_haveProductList_emptySelectedOrder_returnUnmodifiedOrderList() {
        // Given
        val productList = listOf(
            Product(
                name = "Mock Product 1",
                price = 30,
                imageUrl = "Mock Image Url 1"
            ),
            Product(
                name = "Mock Product 2",
                price = 40,
                imageUrl = "Mock Image Url 2"
            ),
        )

        // When
        val result = productViewModel.convertProductListToOrderList(productList)

        // Then
        assertEquals(2, result.size)

        assertEquals(productList[0].name, result[0].name)
        assertEquals(productList[0].price, result[0].price)
        assertEquals(productList[0].imageUrl, result[0].imageUrl)

        assertEquals(productList[1].name, result[1].name)
        assertEquals(productList[1].price, result[1].price)
        assertEquals(productList[1].imageUrl, result[1].imageUrl)
    }

    @Test
    fun convertProductListToOrderList_haveProductList_haveSelectedOrder_returnModifiedOrderList() {
        // Given
        val productList = listOf(
            Product(
                name = "Mock Product 1",
                price = 30,
                imageUrl = "Mock Image Url 1"
            ),
            Product(
                name = "Mock Product 2",
                price = 40,
                imageUrl = "Mock Image Url 2"
            ),
        )

        val matchOrder = Order(
            name = "Mock Product 1",
            price = 30,
            imageUrl = "Mock Image Url 1",
            amount = 3
        )
        val unmatchOrder = Order(
            name = "Mock Product 3",
            price = 50,
            imageUrl = "Mock Image Url 3",
            amount = 1
        )

        productViewModel.updateOrder(matchOrder)
        productViewModel.updateOrder(unmatchOrder)

        // When
        val result = productViewModel.convertProductListToOrderList(productList)

        // Then
        assertEquals(2, result.size)

        assertEquals(productList[0].name, result[0].name)
        assertEquals(productList[0].price, result[0].price)
        assertEquals(productList[0].imageUrl, result[0].imageUrl)
        assertEquals(matchOrder.amount, result[0].amount)

        assertEquals(productList[1].name, result[1].name)
        assertEquals(productList[1].price, result[1].price)
        assertEquals(productList[1].imageUrl, result[1].imageUrl)
        assertEquals(0, result[1].amount)
    }

    @Test
    fun updateOrder_newOrder_notAddNewOrder() {
        // Given
        val mockOrder = Order(
            name = "Mock Product 1",
            price = 30,
            imageUrl = "Mock Image Url 1",
            amount = 0
        )

        // When
        assertEquals(0, productViewModel.selectedOrderList.size)
        productViewModel.updateOrder(mockOrder)

        // Then
        assertEquals(0, productViewModel.selectedOrderList.size)
    }

    @Test
    fun updateOrder_newOrder_addNewOrder() {
        // Given
        val mockOrder = Order(
            name = "Mock Product 1",
            price = 30,
            imageUrl = "Mock Image Url 1",
            amount = 3
        )

        // When
        assertEquals(0, productViewModel.selectedOrderList.size)
        productViewModel.updateOrder(mockOrder)

        // Then
        assertEquals(1, productViewModel.selectedOrderList.size)
        assertEquals(mockOrder.name, productViewModel.selectedOrderList[0].name)
        assertEquals(mockOrder.price, productViewModel.selectedOrderList[0].price)
        assertEquals(mockOrder.imageUrl, productViewModel.selectedOrderList[0].imageUrl)
        assertEquals(mockOrder.amount, productViewModel.selectedOrderList[0].amount)
    }

    @Test
    fun updateOrder_existingOrder_removeOrder() {
        // Given
        val mockExistingOrder = Order(
            name = "Mock Product 1",
            price = 30,
            imageUrl = "Mock Image Url 1",
            amount = 1
        )

        val mockRemoveOrder = Order(
            name = "Mock Product 1",
            price = 30,
            imageUrl = "Mock Image Url 1",
            amount = 0
        )

        // When
        assertEquals(0, productViewModel.selectedOrderList.size)
        productViewModel.updateOrder(mockExistingOrder)
        assertEquals(1, productViewModel.selectedOrderList.size)

        productViewModel.updateOrder(mockRemoveOrder)

        // Then
        assertEquals(0, productViewModel.selectedOrderList.size)
    }

    @Test
    fun updateOrder_existingOrder_editOrder() {
        // Given
        val mockExistingOrder = Order(
            name = "Mock Product 1",
            price = 30,
            imageUrl = "Mock Image Url 1",
            amount = 1
        )

        val mockEditOrder = Order(
            name = "Mock Product 1",
            price = 30,
            imageUrl = "Mock Image Url 1",
            amount = 2
        )

        // When
        assertEquals(0, productViewModel.selectedOrderList.size)
        productViewModel.updateOrder(mockExistingOrder)
        assertEquals(1, productViewModel.selectedOrderList.size)
        assertEquals(mockExistingOrder.name, productViewModel.selectedOrderList[0].name)
        assertEquals(mockExistingOrder.price, productViewModel.selectedOrderList[0].price)
        assertEquals(mockExistingOrder.imageUrl, productViewModel.selectedOrderList[0].imageUrl)
        assertEquals(mockExistingOrder.amount, productViewModel.selectedOrderList[0].amount)

        productViewModel.updateOrder(mockEditOrder)

        // Then
        assertEquals(1, productViewModel.selectedOrderList.size)
        assertEquals(mockEditOrder.name, productViewModel.selectedOrderList[0].name)
        assertEquals(mockEditOrder.price, productViewModel.selectedOrderList[0].price)
        assertEquals(mockEditOrder.imageUrl, productViewModel.selectedOrderList[0].imageUrl)
        assertEquals(mockEditOrder.amount, productViewModel.selectedOrderList[0].amount)
    }
}