package com.eventful.app.domain.model

/**
 * A generic class that holds a value or an error.
 * Used to represent the state of data operations (loading, success, error).
 */
sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val exception: Throwable) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}
