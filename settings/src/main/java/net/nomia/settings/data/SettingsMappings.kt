package net.nomia.settings.data

import com.auth0.android.jwt.JWT
import net.nomia.common.data.model.Menu
import net.nomia.common.data.model.Organization
import net.nomia.common.data.model.Store
import net.nomia.common.data.model.Terminal
import net.nomia.settings.data.local.entity.ApplicationTokenData
import net.nomia.settings.data.local.entity.MenuData
import net.nomia.settings.data.local.entity.OrganizationData
import net.nomia.settings.data.local.entity.ServerProviderData
import net.nomia.settings.data.local.entity.TerminalEntity
import net.nomia.settings.domain.model.ApplicationToken
import net.nomia.settings.domain.model.ServerProvider

fun ServerProvider.toEntity() = when (this) {
    is ServerProvider.Prod -> ServerProviderData(type = ServerProviderData.Type.Prod)
    is ServerProvider.Stage -> ServerProviderData(type = ServerProviderData.Type.Stage)
    is ServerProvider.Custom -> ServerProviderData(
        type = ServerProviderData.Type.Custom,
        url = this.url,
        wss = this.wss,
        api = this.api
    )
}

fun ServerProviderData.toDomain() = when (type) {
    ServerProviderData.Type.Prod -> ServerProvider.Prod
    ServerProviderData.Type.Stage -> ServerProvider.Stage
    ServerProviderData.Type.Custom -> ServerProvider.Custom(
        url = url!!,
        wss = wss!!,
        api = api!!
    )
}

fun Menu.toEntity() = MenuData(
    id = id.value,
    name = name,
)

fun MenuData.toDomain() = Menu(
    id = Menu.ID(id),
    name = name,
)

fun Terminal.toEntity() = TerminalEntity(
    id = id.value,
    name = name,
    organization = organization.toEntity(),
    storeId = storeId.value,
    menu = menu?.toEntity(),
    orderSequence = orderSequence,
    deviceType = deviceType,
)

fun TerminalEntity.toDomain() = Terminal(
    id = Terminal.ID(id),
    name = name,
    organization = organization.toDomain(),
    storeId = Store.ID(storeId),
    menu = menu?.toDomain(),
    orderSequence = orderSequence,
    deviceType = deviceType,
)

fun Organization.toEntity() = OrganizationData(
    id = id.value,
    name = name,
    code = code.value,
    currency = currency,
)

fun OrganizationData.toDomain() = Organization(
    id = Organization.ID(id),
    name = name,
    code = Organization.Code(code),
    currency = currency,
)

fun ApplicationTokenData.toDomain() = ApplicationToken(
    accessToken = JWT(accessToken),
    refreshToken = refreshToken?.let(::JWT)
)

fun ApplicationToken.toEntity() = ApplicationTokenData(
    accessToken = accessToken.toString(),
    refreshToken = refreshToken?.toString()
)
