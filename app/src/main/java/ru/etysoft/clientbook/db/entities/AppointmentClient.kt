package ru.etysoft.clientbook.db.entities

import androidx.room.Embedded
import androidx.room.Relation
import ru.etysoft.clientbook.db.entities.appointment.Appointment

data class AppointmentClient (

    @Embedded
    val appointment: Appointment,

    @Relation(
            parentColumn = "clientId",
            entityColumn = "id"
    )
    val client: Client

)