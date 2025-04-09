package com.example.data_retrofit.services

import com.example.domain.use_details.UseDetails

interface SmartSolarService {
    suspend fun getUseDetails(): UseDetails?
}