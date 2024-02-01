package net.nomia.main.domain.model

import kotlinx.coroutines.flow.StateFlow
import net.nomia.common.data.model.Employee

sealed interface Principal {
    val employee: StateFlow<Employee?>

    class ExternalUser(val auth: StateFlow<Auth>, override val employee: StateFlow<Employee?>) :
        Principal

    class InternalUser(override val employee: StateFlow<Employee?>) : Principal
}
