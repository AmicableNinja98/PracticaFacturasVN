package com.example.core

import com.example.core.ui.screens.smartSolar.usecase.SmartSolarScreenState
import com.example.core.ui.screens.smartSolar.usecase.SmartSolarScreenViewModel
import com.example.data_retrofit.repository.SmartSolarLocalService
import com.example.domain.use_details.UseDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class SmartSolarScreenViewModelTest {
    private lateinit var smartSolarLocalService: SmartSolarLocalService
    private lateinit var smartSolarScreenViewModel: SmartSolarScreenViewModel
    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        smartSolarLocalService = mock()
        smartSolarScreenViewModel = SmartSolarScreenViewModel(smartSolarLocalService)
    }

    @Test
    fun `loadMockDetails returns Success() if there is data`() = runTest {
        val useDetails = UseDetails(
            codigo = "ES002100000001994LJ1FA000",
            estado = "No hemos recibido ninguna solicitud de autoconsumo",
            compensacion = "Con excedentes y compensación Individual - Consumo",
            tipo = "Precio PVPC",
            potencia = "5kWp"
        )

        whenever(smartSolarLocalService.getUseDetails()).thenReturn(useDetails)
        smartSolarScreenViewModel.loadMockDetails()

        testDispatcher.scheduler.advanceUntilIdle()

        assert(smartSolarScreenViewModel.state is SmartSolarScreenState.Success)
        assert((smartSolarScreenViewModel.state as SmartSolarScreenState.Success).details == useDetails)
    }

    @Test
    fun `loadMockDetails returns NoData if there is no data`() = runTest {
        whenever(smartSolarLocalService.getUseDetails()).thenReturn(null)
        smartSolarScreenViewModel.loadMockDetails()

        testDispatcher.scheduler.advanceUntilIdle()

        assert(smartSolarScreenViewModel.state is SmartSolarScreenState.NoData)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }
}