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
    suspend fun insertAll(vararg client: Client)

    @Update
    suspend fun update(client: Client)

    @Delete
    suspend fun delete(client: Client)

    @Query("SELECT * FROM client WHERE id = :id")
    suspend fun getById(id: String): Client?

    @Query("SELECT * FROM client WHERE name = :name")
    suspend fun getByName(name: String): Client?

    @Query("SELECT * FROM client WHERE phoneNumber = :phone")
    suspend fun getByPhone(phone: String): Client?

    // ClientListActivity
    // By phone
    @Query("SELECT * FROM client WHERE phoneNumber LIKE '%' || :phonePart || '%' ORDER BY id DESC LIMIT $LOADING_COUNT")
    suspend fun getLatestByPhone(phonePart: String): List<Client>

    @Query("SELECT * FROM client WHERE phoneNumber LIKE '%' || :phonePart || '%' AND id < :id ORDER BY id DESC LIMIT $LOADING_COUNT")
    suspend fun getNextByPhone(phonePart: String, id: Long): List<Client>

    @Query("SELECT * FROM client WHERE phoneNumber LIKE '%' || :phonePart || '%' AND id > :id ORDER BY id LIMIT $LOADING_COUNT")
    suspend fun getPreviousByPhone(phonePart: String, id: Long): List<Client>

    // By name
    @Query("SELECT * FROM client WHERE name LIKE '%'||:name||'%'  ORDER BY id DESC LIMIT $LOADING_COUNT")
    suspend fun getLatestByName(name: String): List<Client>

    @Query("SELECT * FROM client WHERE name LIKE '%'||:name||'%' AND id < :id ORDER BY id DESC LIMIT $LOADING_COUNT")
    suspend fun getNextByName(name: String, id: Long): List<Client>

    @Query("SELECT * FROM client WHERE name LIKE '%'||:name||'%' AND id > :id ORDER BY id LIMIT $LOADING_COUNT")
    suspend fun getPreviousByName(name: String, id: Long): List<Client>
}