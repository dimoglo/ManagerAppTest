package net.nomia.main.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import net.nomia.main.data.local.entity.EmployeeEntity
import net.nomia.main.data.local.entity.EmployeeFullDBView
import net.nomia.main.data.local.entity.EmployeeGroupEntity
import java.util.UUID

@Dao
interface EmployeeDao {

    @Transaction
    @Query("select * from employee where pin = :pin")
    fun findByPin(pin: String): Flow<EmployeeFullDBView?>

    @Query("delete from employee")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(vararg entities: EmployeeEntity)

    @Transaction
    @Query("delete from employee where id = :id")
    suspend fun delete(id: UUID)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(vararg entities: EmployeeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(vararg entities: EmployeeGroupEntity)
}
