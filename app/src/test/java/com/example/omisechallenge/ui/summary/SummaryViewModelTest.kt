package com.example.omisechallenge.ui.summary

import com.example.omisechallenge.data.ApiResult
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
class SummaryViewModelTest {
    @MockK(relaxed = true)
    lateinit var storeUseCase: StoreUseCase

    private lateinit var summaryViewModel: SummaryViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        Dispatchers.setMain(StandardTestDispatcher())
        summaryViewModel = SummaryViewModel()
        summaryViewModel.storeUseCase = storeUseCase
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getTotalPayment_emptyOrder_returnZero() {
        // Given
        val orderList = listOf<Order>()
        summaryViewModel.orderList = orderList

        // When
        val result = summaryViewModel.getTotalPayment()

        // Then
        assertEquals(0, result)
    }

    @Test
    fun getTotalPayment_singleOrder() {
        // Given
        val orderList = listOf(
            Order(
                name = "Mock Order 1",
                price = 30,
                imageUrl = "Mock Image Url",
                amount = 1
            )
        )
        summaryViewModel.orderList = orderList

        // When
        val result = summaryViewModel.getTotalPayment()

        // Then
        assertEquals(30, result)
    }

    @Test
    fun getTotalPayment_multipleOrders() {
        // Given
        val orderList = listOf(
            Order(
                name = "Mock Order 1",
                price = 30,
                imageUrl = "Mock Image Url",
                amount = 3
            ),
            Order(
                name = "Mock Order 2",
                price = 40,
                imageUrl = "Mock Image Url",
                amount = 2
            ),
        )
        summaryViewModel.orderList = orderList

        // When
        val result = summaryViewModel.getTotalPayment()

        // Then
        assertEquals(170, result)
    }

    @Test
    fun validatePayment_onSuccess_setSuccessState() {
        runTest {
            // Given
            val orderList = listOf(
                Order(
                    name = "Mock Order 1",
                    price = 30,
                    imageUrl = "Mock Image Url",
                    amount = 3
                )
            )
            val mockAddress = "Mock Addresss"

            summaryViewModel.orderList = orderList
            summaryViewModel.address = mockAddress

            coEvery { storeUseCase.makeOrder(any()) } coAnswers {
                delay(500)
                ApiResult.Success(Unit)
            }

            val states = mutableListOf<UiState<Any>>()
            val job = launch {
                summaryViewModel.paymentState.toList(states)
            }

            // When
            summaryViewModel.validatePayment()
            advanceUntilIdle()

            // Then
            assertEquals(3, states.size)
            assertEquals(UiState.Idle, states[0])
            assertEquals(UiState.Loading, states[1])
            assertEquals(UiState.Success(Unit), states[2])

            job.cancel()
        }
    }

    @Test
    fun validatePayment_onError_setErrorState() {
        runTest {
            // Given
            val orderList = listOf(
                Order(
                    name = "Mock Order 1",
                    price = 30,
                    imageUrl = "Mock Image Url",
                    amount = 3
                )
            )
            val mockAddress = "Mock Addresss"

            summaryViewModel.orderList = orderList
            summaryViewModel.address = mockAddress

            coEvery { storeUseCase.makeOrder(any()) } coAnswers {
                delay(500)
                ApiResult.Error(500, "Mock Error")
            }

            val states = mutableListOf<UiState<Any>>()
            val job = launch {
                summaryViewModel.paymentState.toList(states)
            }

            // When
            summaryViewModel.validatePayment()
            advanceUntilIdle()

            // Then
            assertEquals(3, states.size)
            assertEquals(UiState.Idle, states[0])
            assertEquals(UiState.Loading, states[1])
            assertEquals(UiState.Error("Mock Error"), states[2])

            job.cancel()
        }
    }
}