package net.nomia.common.data.model

import java.util.UUID

data class SignInResponse(
    val requestId: RequestId,
    val hasPassword: Boolean,
    val authType: IdentityType
) {
    @JvmInline
    value class RequestId(val value: UUID)
}
