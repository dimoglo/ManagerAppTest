package net.nomia.main.data

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest
import net.nomia.common.data.model.Employee
import net.nomia.main.config.MainDatabase
import net.nomia.main.data.local.entity.EmployeeEntity
import net.nomia.main.data.local.entity.EmployeeGroupEntity
import net.nomia.main.domain.EmployeeRepository
import javax.inject.Inject

class EmployeeRepositoryImpl @Inject constructor(
    private val mainDatabase: MainDatabase
) : EmployeeRepository {

    override fun findByPin(pin: Employee.Pin): Flow<Employee?> {
        return mainDatabase.employeeDao().findByPin(pin.value)
            .distinctUntilChanged()
            .mapLatest { it?.toDomain() }
    }

    override suspend fun save(vararg employees: Employee) {
        mainDatabase.withTransaction {
            employees.forEach { employee ->
                mainDatabase.employeeDao().delete(employee.id.value)
                mainDatabase.employeeDao().save(
                    EmployeeEntity(
                        id = employee.id.value,
                        accountId = employee.accountId?.value,
                        firstName = employee.firstName,
                        lastName = employee.lastName,
                        vatin = employee.vatin,
                        pin = employee.pin?.value
                    )
                )

                employee.groups.forEach { employeeGroup ->
                    mainDatabase.employeeDao().save(
                        EmployeeGroupEntity(
                            id = employeeGroup.id.value,
                            employeeId = employee.id.value,
                            storeId = employeeGroup.storeId?.value,
                            roleCode = employeeGroup.roleCode
                        )
                    )
                }
            }
        }
    }

    override suspend fun saveAll(employees: List<Employee>) {
        mainDatabase.withTransaction {
            mainDatabase.employeeDao().deleteAll()
            save(*employees.toTypedArray())
        }
    }

    override suspend fun delete(id: Employee.ID) {
        mainDatabase.employeeDao().delete(id.value)
    }
}
