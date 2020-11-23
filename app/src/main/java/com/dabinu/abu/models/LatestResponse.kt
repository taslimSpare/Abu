package com.dabinu.abu.models

data class LatestResponse(
    val success: Boolean = true,
    val timestamp: Long = 0,
    val base: String = "USD",
    val date: String = "",
    val rates: Map<String, Double> = mapOf()
)