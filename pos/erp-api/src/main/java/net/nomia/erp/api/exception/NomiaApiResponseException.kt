package net.nomia.erp.api.exception

import net.nomia.pos.core.exception.NomiaException

open class NomiaApiResponseException(message: String?) : NomiaException(message)
