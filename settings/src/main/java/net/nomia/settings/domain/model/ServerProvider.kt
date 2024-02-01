package net.nomia.settings.domain.model

import net.nomia.settings.BuildConfig

sealed class ServerProvider(
    open val url: String,
    open val wss: String,
    open val api: String
) {
    object Prod : ServerProvider(
        url = "https://erp.nomia.app",
        wss = "wss://erp.nomia.app/subscriptions",
        api = "https://erp.nomia.app/graphql"
    )

    object Stage : ServerProvider(
        url = "https://stage.erp.n.nomia.tech",
        wss = "wss://stage.erp.n.nomia.tech/subscriptions",
        api = "https://stage.erp.n.nomia.tech/graphql"
    )

    data class Custom(
        override val url: String = "",
        override val wss: String = "",
        override val api: String = ""
    ) : ServerProvider(url, wss, api)

    companion object{
        var DEFAULT = if (BuildConfig.DEBUG) Stage else Prod
    }
}
