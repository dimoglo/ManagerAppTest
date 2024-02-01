package net.nomia.settings.data.local.entity

data class ApplicationTokenData(
    val accessToken: String,
    val refreshToken: String? = null
)
