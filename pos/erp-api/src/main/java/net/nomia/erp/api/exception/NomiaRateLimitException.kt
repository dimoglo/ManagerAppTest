package net.nomia.erp.api.exception

import java.math.BigDecimal

class NomiaRateLimitException(
    message: String?,
    val timeLeft: BigDecimal?
) : NomiaApiResponseException(message)
