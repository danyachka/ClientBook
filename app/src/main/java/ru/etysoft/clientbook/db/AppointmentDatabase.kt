package ru.etysoft.clientbook.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.etysoft.clientbook.db.daos.AppointmentDao
import ru.etysoft.clientbook.db.daos.ClientDao
import ru.etysoft.clientbook.db.entities.Client
import ru.etysoft.clientbook.db.entities.appointment.Appointment

@Database(
        version = 1,
        entities = [ Appointment::class, Client::class ]
)
abstract class AppDatabase: RoomDatabase() {

    companion object {
        const val DB_NAME = "app_db"

        var db: AppDatabase? = null
    }

    open fun getDatabase(applicationContext: Context?): AppDatabase {
        if (db == null) {
            val database = Room.databaseBuilder(
                    applicationContext!!,
                    AppDatabase::class.java, DB_NAME
            ).build()

            db = database
        }

        return db as AppDatabase
    }

    abstract fun getAppointmentDao(): AppointmentDao
    abstract fun getClientDao(): ClientDao

}