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

    companion object {
        const val LOADING_COUNT = 20
    }

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

    // ClientListActivity
    // By phone
    @Query("SELECT * FROM client WHERE phoneNumber LIKE '%' || :phonePart || '%' ORDER BY id DESC LIMIT $LOADING_COUNT")
    fun getLatestByPhone(phonePart: String): List<Client>

    @Query("SELECT * FROM client WHERE phoneNumber LIKE '%' || :phonePart || '%' AND id < :id ORDER BY id DESC LIMIT $LOADING_COUNT")
    fun getNextByPhone(phonePart: String, id: Long): List<Client>

    @Query("SELECT * FROM client WHERE phoneNumber LIKE '%' || :phonePart || '%' AND id > :id ORDER BY id LIMIT $LOADING_COUNT")
    fun getPreviousByPhone(phonePart: String, id: Long): List<Client>

    // By name
    @Query("SELECT * FROM client WHERE name LIKE '%'||:name||'%'  ORDER BY id DESC LIMIT $LOADING_COUNT")
    fun getLatestByName(name: String): List<Client>

    @Query("SELECT * FROM client WHERE name LIKE '%'||:name||'%' AND id < :id ORDER BY id DESC LIMIT $LOADING_COUNT")
    fun getNextByName(name: String, id: Long): List<Client>

    @Query("SELECT * FROM client WHERE name LIKE '%'||:name||'%' AND id > :id ORDER BY id LIMIT $LOADING_COUNT")
    fun getPreviousByName(name: String, id: Long): List<Client>
}