package ru.etysoft.clientbook.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ru.etysoft.clientbook.db.entities.Client
import ru.etysoft.clientbook.db.entities.appointment.Appointment

@Dao
interface ClientDao {

    @Insert
    fun insertAll(vararg client: Client)

    @Update
    fun update(client: Client)

    @Delete
    fun delete(client: Client)

    @Query("SELECT * FROM client WHERE id = :id")
    fun getById(id: String): Client?

    @Query("SELECT * FROM client WHERE name = :name")
    fun getByName(name: String): Client?

    @Query("SELECT * FROM client WHERE phoneNumber = :phone")
    fun getByPhone(phone: String): Client?
}