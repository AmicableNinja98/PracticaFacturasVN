package com.example.practicafacturas


import com.example.practicafacturas.home.usecase.HomeScreenViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class HomeScreenViewModelTests {
    private lateinit var viewModel: HomeScreenViewModel
    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeScreenViewModel()
    }

    @Test
    fun `initial useMockData is false`() {
        Assertions.assertEquals(false, viewModel.useMockData)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onSwitchCheckedChange to true updates useMockData and shows mock snackbar`() = runTest {
        viewModel.onSwitchCheckedChange(true)
        testDispatcher.scheduler.advanceUntilIdle()

        Assertions.assertEquals(true, viewModel.useMockData)
        Assertions.assertEquals(
            "Usando datos del Mock",
            viewModel.snackbarHostState.currentSnackbarData?.visuals?.message
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onSwitchCheckedChange to false updates useMockData and shows api snackbar`() = runTest {
        viewModel.onSwitchCheckedChange(false)
        testDispatcher.scheduler.advanceUntilIdle()

        Assertions.assertEquals(false, viewModel.useMockData)
        Assertions.assertEquals(
            "Usando datos de la Api",
            viewModel.snackbarHostState.currentSnackbarData?.visuals?.message
        )
    }

}