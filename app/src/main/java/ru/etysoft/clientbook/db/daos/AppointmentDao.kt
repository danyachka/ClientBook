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
    fun insertAll(vararg appointment: Appointment)

    @Update
    fun update(appointment: Appointment)

    @Delete
    fun delete(appointment: Appointment)

    @Query("SELECT * FROM appointment WHERE id = :id")
    fun getById(id: Long): Appointment

    @Query("SELECT * FROM appointment WHERE id < :id and clientId = :clientId ORDER BY id DESC LIMIT $LOADING_COUNT_HALF")
    fun getOlderByClient(id: Long, clientId: Long): List<Appointment>

    @Query("SELECT * FROM appointment WHERE id > :id and clientId = :clientId ORDER BY id LIMIT $LOADING_COUNT_HALF")
    fun getNewerClient(id: Long, clientId: Long): List<Appointment>

    // AppointmentClient
    @Query("SELECT * FROM appointment ORDER BY ABS(startTime - :time) LIMIT 1")
    fun getClosestACByTime(time: Long): AppointmentClient

    @Query("SELECT * FROM appointment WHERE clientId = :clientId ORDER BY ABS(startTime - :time) LIMIT 1")
    fun getClosestACByTimeAndClient(time: Long, clientId: Long): AppointmentClient

    @Query("SELECT * FROM appointment WHERE id = :id")
    fun getACById(id: Long): AppointmentClient

    @Transaction
    @Query("SELECT * FROM appointment WHERE clientId = :clientId")
    fun getACAllByClientId(clientId: Long): List<AppointmentClient>

    @Transaction
    @Query("SELECT * FROM appointment WHERE id < :id ORDER BY id DESC LIMIT $LOADING_COUNT_HALF")
    fun getACOlder(id: Long): List<AppointmentClient>

    @Transaction
    @Query("SELECT * FROM appointment WHERE id > :id ORDER BY id LIMIT $LOADING_COUNT_HALF")
    fun getACNewer(id: Long): List<AppointmentClient>

    @Query("SELECT * FROM appointment WHERE clientId = :clientId")
    fun getAllByClientId(clientId: Long): List<Appointment>
}