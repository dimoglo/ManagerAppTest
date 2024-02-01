@file:Suppress("TooGenericExceptionThrown")

package net.nomia.main.domain

import com.auth0.android.jwt.JWT
import net.nomia.common.data.model.Employee
import net.nomia.common.data.model.EmployeeGroup
import net.nomia.common.data.model.LostPasswordResponse
import net.nomia.common.data.model.Menu
import net.nomia.common.data.model.Organization
import net.nomia.common.data.model.RoleCode
import net.nomia.common.data.model.SignInResponse
import net.nomia.common.data.model.Store
import net.nomia.common.data.model.Terminal
import net.nomia.erp.mutation.LostPasswordMutation
import net.nomia.erp.mutation.SignInPosMutation
import net.nomia.erp.schema.fragment.AuthFragment
import net.nomia.erp.schema.fragment.EmployeeFragment
import net.nomia.erp.schema.fragment.MainStoreFragment
import net.nomia.erp.schema.fragment.MenuFragment
import net.nomia.erp.schema.fragment.OrganizationFragment
import net.nomia.erp.schema.fragment.TerminalFragment
import net.nomia.erp.schema.type.IdentityType
import net.nomia.main.domain.model.Auth
import net.nomia.main.domain.model.TerminalInput
import java.util.Currency
import net.nomia.erp.schema.type.RoleCode as ApiRoleCode

fun SignInPosMutation.SignInV2.toDomain() = SignInResponse(
    requestId = SignInResponse.RequestId(requestId),
    hasPassword = hasPassword,
    authType = authType.toDomain()
)

fun LostPasswordMutation.LostPassword.toDomain() = LostPasswordResponse(
    requestId = LostPasswordResponse.RequestId(requestId),
    identityType = identityType.toDomain(),
)

fun IdentityType.toDomain() = when (this) {
    IdentityType.EMAIL -> net.nomia.common.data.model.IdentityType.Email
    IdentityType.PHONE -> net.nomia.common.data.model.IdentityType.Phone
    else -> throw RuntimeException("Unknown identity type = ${this.name}")
}

fun AuthFragment.toDomain() = Auth(
    accessToken = JWT(accessToken),
    refreshToken = JWT(refreshToken)
)

fun OrganizationFragment.toDomain() = Organization(
    id = Organization.ID(id),
    name = name,
    code = Organization.Code(shortOrgId!!),
    currency = Currency.getInstance(settings!!.currencyUnit),
)

fun MainStoreFragment.toDomain() = Store(
    id = Store.ID(id),
    name = name,
    address = actualAddress,
    tablesFeatureEnabled = tablesFeatureEnabled
)

fun MenuFragment.toDomain() = Menu(
    id = Menu.ID(id),
    name = name,
)

fun TerminalFragment.toDomain(deviceType: Terminal.DeviceType) = Terminal(
    id = Terminal.ID(id),
    name = name,
    menu = menu?.fragments?.menuFragment?.toDomain(),
    storeId = Store.ID(store!!.fragments.mainStoreFragment.id),
    orderSequence = orderSequence ?: 0,
    organization = store.organization.fragments.organizationFragment.toDomain(),
    deviceType = deviceType,
)

fun TerminalInput.toDomain(deviceType: Terminal.DeviceType) = Terminal(
    id = id!!,
    name = name,
    organization = organization,
    storeId = store.id,
    menu = menu,
    orderSequence = orderSequence,
    deviceType = deviceType,
)

fun EmployeeFragment.toDomain() = Employee(
    id = Employee.ID(id),
    accountId = accountId?.let(Employee::AccountId),
    firstName = firstName,
    lastName = lastName,
    vatin = vatin,
    pin = pin?.let(Employee::Pin),
    groups = userGroups.map {
        EmployeeGroup(
            id = EmployeeGroup.ID(it.id),
            storeId = it.storeId?.let(Store::ID),
            roleCode = when (it.code) {
                ApiRoleCode.ADMIN -> RoleCode.Admin
                ApiRoleCode.MANAGER -> RoleCode.Manager
                ApiRoleCode.CASHIER -> RoleCode.Cashier
                ApiRoleCode.UNKNOWN__ -> throw RuntimeException("Unsupported type ${it.code}")
            }
        )
    }
)
