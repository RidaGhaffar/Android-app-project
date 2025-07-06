package com.example.hospviews.models



data class Patient(
    val id: Int,
    var name: String,
    var age: Int,
    var status: String,
    var doctor: String,
    val checkInTime: String,
    val room: String,
    var phoneNumber: String = "000-000-0000" // Default phone number
)