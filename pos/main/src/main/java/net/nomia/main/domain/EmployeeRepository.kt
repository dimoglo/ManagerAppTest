package net.nomia.main.domain

import kotlinx.coroutines.flow.Flow
import net.nomia.common.data.model.Employee

interface EmployeeRepository {
    fun findByPin(pin: Employee.Pin): Flow<Employee?>

    suspend fun save(vararg employees: Employee)
    suspend fun saveAll(employees: List<Employee>)

    suspend fun delete(id: Employee.ID)
}
