package com.dabinu.abu.models

data class ResponseBody<T>(
    val state: String = "",
    val message: String = "",
    val data: T? = null
) {
    fun loading(): ResponseBody<T> {
        return ResponseBody(STATE_LOADING, "", null)
    }
}

const val STATE_LOADING = "LOADING"
const val STATE_FAILED = "FAILED"
const val STATE_SUCCESSFUL = "SUCCESSFUL"