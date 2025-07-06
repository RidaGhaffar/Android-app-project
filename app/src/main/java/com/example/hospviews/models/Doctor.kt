package com.example.hospviews.models


data class Doctor(
    val id: Int,
    var name: String,
    var specialization: String,
    var isAvailable: Boolean = true
)
