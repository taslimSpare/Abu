package com.dabinu.abu.models

data class ConvertResponse(
    val success: Boolean = true,
    val query: ConvertQuery = ConvertQuery(),
    val info: Info = Info(),
    val historical: String = "",
    val date: String = "",
    val result: Double = 0.0
)


data class ConvertQuery(
    val from: String = "",
    val to: String = "",
    val amount: Double = 0.0
)


data class Info(
    val timestamp: Long = 0,
    val rate: Double = 0.0
)

