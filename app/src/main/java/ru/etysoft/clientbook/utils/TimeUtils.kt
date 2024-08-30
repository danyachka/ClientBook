package ru.etysoft.clientbook.utils

import ru.etysoft.clientbook.db.entities.AppointmentClient
import ru.etysoft.clientbook.db.entities.appointment.Appointment
import java.time.ZoneId

class TimeUtils {

    fun getFirstPositionOfDay(list: ArrayList<AppointmentClient>, time: Long): Int {
        var closest = list[0].appointment
        for (i: Int in 0..<list.size) {
            val appointment = list[i].appointment

            if (kotlin.math.abs(closest.startTime - time) > kotlin.math.abs(appointment.startTime - time)) {
                closest = appointment
            }
        }

        val zoneId = ZoneId.systemDefault()
        val date = closest.getLocalDate(zoneId)
        val dateStartTime: Long = date.atStartOfDay().atZone(zoneId).toInstant().toEpochMilli()

        var firstPositionOfDay = 0
        for (i: Int in 0..<list.size) {
            val currentStart = list[i].appointment.startTime
            if (dateStartTime > currentStart) continue

            firstPositionOfDay = i
            break
        }

        return  firstPositionOfDay
    }
}