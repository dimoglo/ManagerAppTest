package net.nomia.pos.core.exception

data class NetworkException(override val cause: Throwable) : NomiaException(cause.message)
