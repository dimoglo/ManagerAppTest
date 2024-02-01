package net.nomia.pos.core.data

import net.nomia.pos.core.text.Content

sealed class Response<T> {
    object Loading : Response<Nothing>()
    data class Success<T>(val result: T) : Response<T>()
    data class Error(val message: Content, val throwable: Throwable? = null) : Response<Nothing>()
    object Timeout : Response<Nothing>()
}

fun <T, D> Response<T>.map(successBlock: Response.Success<T>.() -> D): Response<out D> =
    when (this) {
        is Response.Error -> Response.Error(message, throwable)
        Response.Loading -> Response.Loading
        is Response.Success -> Response.Success(successBlock())
        Response.Timeout -> Response.Timeout
    }
