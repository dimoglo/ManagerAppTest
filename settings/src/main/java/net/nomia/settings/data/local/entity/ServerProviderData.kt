package net.nomia.settings.data.local.entity

data class ServerProviderData(
    val type: Type,
    val url: String? = null,
    val wss: String? = null,
    val api: String? = null
) {
    enum class Type {
        Prod,
        Stage,
        Custom,
    }
}
