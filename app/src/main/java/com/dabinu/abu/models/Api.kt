package com.dabinu.abu.models



enum class State {
    LOADING, SUCCESS, ERROR
}


class Resource<T>(val state: State = State.LOADING, val data: T?) {
    companion object {
        fun<T> success(data: T): Resource<T> {
            return Resource(State.SUCCESS, data)
        }

        fun<T> error(e: T? = null): Resource<T> {
            return Resource(State.ERROR, e)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(State.LOADING, data)
        }
    }
}

