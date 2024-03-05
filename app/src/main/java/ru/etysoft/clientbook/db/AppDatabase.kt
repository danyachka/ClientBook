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
        entities = [ Appointment::class, Client::class ],
        exportSchema = true
)
abstract class AppDatabase: RoomDatabase() {

    companion object {
        private const val DB_NAME = "app_db"

        private var db: AppDatabase? = null

        public fun getDatabase(applicationContext: Context?): AppDatabase {
            if (db == null) {
                val database = androidx.room.Room.databaseBuilder(
                        applicationContext!!,
                        AppDatabase::class.java, DB_NAME
                ).enableMultiInstanceInvalidation()
                        .build()

                db = database
            }

            return db as AppDatabase
        }
    }

    abstract fun getAppointmentDao(): AppointmentDao
    abstract fun getClientDao(): ClientDao

}