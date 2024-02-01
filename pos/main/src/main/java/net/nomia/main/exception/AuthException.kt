package net.nomia.main.exception

import net.nomia.pos.core.exception.NomiaException

data class AuthException(override val cause: Throwable) : NomiaException(cause.message)
