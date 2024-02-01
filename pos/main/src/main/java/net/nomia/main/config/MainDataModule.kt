package net.nomia.main.config

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.nomia.main.data.EmployeeRepositoryImpl
import net.nomia.main.domain.EmployeeRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class MainDataModule {

    @Singleton
    @Binds
    abstract fun bindEmployeeRepository(
        employeeRepositoryImpl: EmployeeRepositoryImpl
    ): EmployeeRepository
}
