package net.nomia.main.data

import net.nomia.common.data.model.Employee
import net.nomia.common.data.model.EmployeeGroup
import net.nomia.common.data.model.Store
import net.nomia.main.data.local.entity.EmployeeFullDBView
import net.nomia.main.data.local.entity.EmployeeGroupEntity

fun EmployeeFullDBView.toDomain() = Employee(
    id = Employee.ID(employee.id),
    accountId = employee.accountId?.let(Employee::AccountId),
    firstName = employee.firstName,
    lastName = employee.lastName,
    vatin = employee.vatin,
    pin = employee.pin?.let(Employee::Pin),
    groups = employeeGroups.map { it.toDomain() }
)


fun EmployeeGroupEntity.toDomain() = EmployeeGroup(
    id = EmployeeGroup.ID(id),
    storeId = storeId?.let(Store::ID),
    roleCode = roleCode
)
