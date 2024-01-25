package ru.etysoft.clientbook.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ru.etysoft.clientbook.db.entities.appointment.Appointment

@Dao
interface ClientDao {

    @Insert
    fun insertAll(vararg clientDao: ClientDao)

    @Update
    fun update(clientDao: ClientDao)

    @Delete
    fun delete(clientDao: ClientDao)

    @Query("SELECT * FROM client WHERE id = :id")
    fun getById(id: String): ClientDao
}