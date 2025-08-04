package com.tumble.kronoxtoapp.domain.models.presentation

import com.tumble.kronoxtoapp.domain.models.network.NetworkResponse
import java.util.Date

data class ResourceSelectionModel (
    val resource: NetworkResponse.KronoxResourceElement,
    val date: Date
)