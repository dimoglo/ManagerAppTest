package net.nomia.main.domain.model

import com.auth0.android.jwt.JWT

data class Auth(
    val accessToken: JWT,
    val refreshToken: JWT
)
