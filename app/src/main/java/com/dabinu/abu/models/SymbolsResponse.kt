package com.dabinu.abu.models

data class SymbolsResponse(
    var success: Boolean = true,
    var symbols: Map<String, String> = mapOf()
)