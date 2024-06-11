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
    suspend fun insertAll(vararg appointment: Appointment)

    @Update
    suspend fun update(appointment: Appointment)

    @Delete
    suspend fun delete(appointment: Appointment)

    @Query("SELECT * FROM appointment WHERE id = :id")
    suspend fun getById(id: Long): Appointment?

    @Query("SELECT * FROM appointment WHERE id < :id and clientId = :clientId ORDER BY id DESC LIMIT $LOADING_COUNT_HALF")
    suspend fun getOlderByClient(id: Long, clientId: Long): List<Appointment>

    @Query("SELECT * FROM appointment WHERE id > :id and clientId = :clientId ORDER BY id LIMIT $LOADING_COUNT_HALF")
    suspend fun getNewerClient(id: Long, clientId: Long): List<Appointment>

    // AppointmentClient
    @Query("SELECT * FROM appointment ORDER BY ABS(startTime - :time) LIMIT 1")
    suspend fun getClosestACByTime(time: Long): AppointmentClient?

    @Query("SELECT * FROM appointment WHERE clientId = :clientId ORDER BY ABS(startTime - :time) LIMIT 1")
    suspend fun getClosestACByTimeAndClient(time: Long, clientId: Long): AppointmentClient?

    @Query("SELECT * FROM appointment WHERE id = :id")
    suspend fun getACById(id: Long): AppointmentClient?

    @Transaction
    @Query("SELECT * FROM appointment WHERE clientId = :clientId")
    suspend fun getACAllByClientId(clientId: Long): List<AppointmentClient>

    @Transaction
    @Query("SELECT * FROM appointment WHERE id < :id ORDER BY id DESC LIMIT $LOADING_COUNT_HALF")
    suspend fun getACOlder(id: Long): List<AppointmentClient>

    @Transaction
    @Query("SELECT * FROM appointment WHERE id > :id ORDER BY id LIMIT $LOADING_COUNT_HALF")
    suspend fun getACNewer(id: Long): List<AppointmentClient>

    @Query("SELECT * FROM appointment WHERE clientId = :clientId")
    suspend fun getAllByClientId(clientId: Long): List<Appointment>
}