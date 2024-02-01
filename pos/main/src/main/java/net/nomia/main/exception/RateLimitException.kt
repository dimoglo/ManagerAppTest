package net.nomia.main.exception

import net.nomia.pos.core.exception.NomiaException
import java.math.BigDecimal

data class RateLimitException(
    override val cause: Throwable,
    val timeLeft: BigDecimal?,
) : NomiaException(cause.message)
