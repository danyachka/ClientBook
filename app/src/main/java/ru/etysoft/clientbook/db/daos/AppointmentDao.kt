package ru.etysoft.clientbook.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import ru.etysoft.clientbook.db.entities.AppointmentClient
import ru.etysoft.clientbook.db.entities.appointment.Appointment

@Dao
interface AppointmentDao {

    companion object {
        const val LOADING_COUNT_HALF = 50
    }

    @Insert
    suspend fun insertAll(vararg appointment: Appointment): List<Long>

    @Insert
    suspend fun insert(appointment: Appointment): Long

    @Update
    suspend fun update(appointment: Appointment)

    @Delete
    suspend fun delete(appointment: Appointment)

    @Query("SELECT * FROM appointment WHERE id = :id")
    suspend fun getById(id: Long): Appointment?

    @Query("SELECT * FROM appointment WHERE startTime < :startTime and clientId = :clientId ORDER BY startTime DESC LIMIT $LOADING_COUNT_HALF")
    suspend fun getOlderByClient(startTime: Long, clientId: Long): List<Appointment>

    @Query("SELECT * FROM appointment WHERE startTime > :startTime and clientId = :clientId ORDER BY startTime LIMIT $LOADING_COUNT_HALF")
    suspend fun getNewerClient(startTime: Long, clientId: Long): List<Appointment>

    // AppointmentClient
    @Query("SELECT * FROM appointment ORDER BY ABS(startTime - :startTime) LIMIT 1")
    suspend fun getClosestACByTime(startTime: Long): AppointmentClient?

    @Query("SELECT * FROM appointment WHERE startTime > :startTime AND endTime < :endTime")
    suspend fun getBetween(startTime: Long, endTime: Long): AppointmentClient?

    @Query("SELECT * FROM appointment WHERE clientId = :clientId ORDER BY ABS(startTime - :startTime) LIMIT 1")
    suspend fun getClosestACByTimeAndClient(startTime: Long, clientId: Long): AppointmentClient?

    @Query("SELECT * FROM appointment WHERE id = :id")
    suspend fun getACById(id: Long): AppointmentClient?

    @Transaction
    @Query("SELECT * FROM appointment WHERE clientId = :clientId")
    suspend fun getACAllByClientId(clientId: Long): List<AppointmentClient>

    @Transaction
    @Query("SELECT * FROM appointment WHERE startTime < :startTime ORDER BY startTime DESC LIMIT $LOADING_COUNT_HALF")
    suspend fun getACOlder(startTime: Long): List<AppointmentClient>

    @Transaction
    @Query("SELECT * FROM appointment WHERE startTime > :startTime ORDER BY startTime LIMIT $LOADING_COUNT_HALF")
    suspend fun getACNewer(startTime: Long): List<AppointmentClient>

    @Query("SELECT * FROM appointment WHERE clientId = :clientId")
    suspend fun getAllByClientId(clientId: Long): List<Appointment>
}