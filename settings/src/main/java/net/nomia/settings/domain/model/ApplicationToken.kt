package net.nomia.settings.domain.model

import com.auth0.android.jwt.JWT

data class ApplicationToken(
    val accessToken: JWT,
    val refreshToken: JWT? = null
)
