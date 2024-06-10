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
    fun getById(id: String): Appointment

    @Query("SELECT * FROM appointment WHERE id = :id")
    fun getACById(id: String): AppointmentClient

    @Transaction
    @Query("SELECT * FROM appointment WHERE clientId = :clientId")
    fun getACAllByClientId(clientId: String): List<AppointmentClient>

    @Transaction
    @Query("SELECT * FROM appointment WHERE startTime < :beforeTime ORDER BY startTime DESC LIMIT $LOADING_COUNT_HALF")
    fun getACOlder(beforeTime: Long): List<AppointmentClient>

    @Transaction
    @Query("SELECT * FROM appointment WHERE startTime > :time ORDER BY startTime LIMIT $LOADING_COUNT_HALF")
    fun getACNewer(time: Long): List<AppointmentClient>

    @Query("SELECT * FROM appointment WHERE clientId = :clientId")
    fun getAllByClientId(clientId: String): List<Appointment>

    @Query("SELECT * FROM appointment WHERE startTime < :beforeTime ORDER BY startTime DESC LIMIT $LOADING_COUNT_HALF")
    fun getOlder(beforeTime: Long): List<Appointment>

    @Query("SELECT * FROM appointment WHERE startTime > :time ORDER BY startTime LIMIT $LOADING_COUNT_HALF")
    fun getNewer(time: Long): List<Appointment>


}