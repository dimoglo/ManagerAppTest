package net.nomia.settings.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import net.nomia.common.data.model.Terminal
import net.nomia.settings.data.local.entity.TerminalEntity
import java.util.UUID

@Dao
interface TerminalDao {

    @Transaction
    @Query("SELECT * FROM terminal order by name desc limit 1")
    fun findFirst(): Flow<TerminalEntity?>

    @Transaction
    @Query("SELECT * FROM terminal where id = :id ")
    fun findById(id: UUID): Flow<TerminalEntity?>

    @Transaction
    @Query(""" 
        select deviceType
        from terminal
        order by name desc limit 1
    """)
    fun getDefaultDeviceType(): Flow<Terminal.DeviceType>

    @Transaction
    @Query(
        """
        select orderSequence
        from terminal
        where id = :terminalId
    """
    )
    fun getOrderSequence(terminalId: UUID): Flow<Long>

    @Transaction
    @Query(
        """
        update terminal
            set orderSequence = :orderSequence
        where id = :terminalId
    """
    )
    suspend fun saveOrderSequence(terminalId: UUID, orderSequence: Long)


    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(vararg entities: TerminalEntity)

    @Transaction
    @Delete
    suspend fun delete(vararg entities: TerminalEntity)

    @Transaction
    @Query("delete from terminal")
    suspend fun deleteAll()
}
