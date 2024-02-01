package net.nomia.common.data.model

import java.util.UUID

data class LostPasswordResponse(
    val identityType: IdentityType,
    val requestId: RequestId,
) {

    @JvmInline
    value class RequestId(val value: UUID)
}
