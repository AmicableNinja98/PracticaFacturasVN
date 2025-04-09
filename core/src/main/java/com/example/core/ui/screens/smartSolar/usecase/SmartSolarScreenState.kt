package com.example.core.ui.screens.smartSolar.usecase

import com.example.domain.use_details.UseDetails

sealed class SmartSolarScreenState {
    data object Loading : SmartSolarScreenState()
    data class Success(val details: UseDetails) : SmartSolarScreenState()
    data object NoData : SmartSolarScreenState()
}