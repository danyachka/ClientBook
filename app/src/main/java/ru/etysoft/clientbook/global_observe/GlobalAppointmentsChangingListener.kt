package ru.etysoft.clientbook.global_observe

import ru.etysoft.clientbook.db.daos.AppointmentDao
import ru.etysoft.clientbook.db.entities.AppointmentClient
import ru.etysoft.clientbook.db.entities.appointment.Appointment
import ru.etysoft.clientbook.ui.adapters.appointment.AppointmentAdapter
import ru.etysoft.clientbook.ui.adapters.appointment.AppointmentLoader

interface GlobalAppointmentsChangingListener {

    fun onAppointmentRemoved(appointment: Appointment)

    fun onAppointmentAdded(appointmentClient: AppointmentClient)

    fun onAppointmentChanged(appointmentClient: AppointmentClient)
}

fun removeFromList(appointment: Appointment,
                   list: ArrayList<AppointmentClient>,
                   adapter: AppointmentAdapter) {
    for (i: Int in 0..<list.size) {
        val ac = list[i]
        if (ac.appointment.id != appointment.id) continue

        list.removeAt(i)
        adapter.notifyItemRemoved(i)
        break
    }
}

fun processListAddition(appointmentClient: AppointmentClient,
                        loader: AppointmentLoader,
                        list: ArrayList<AppointmentClient>,
                        adapter: AppointmentAdapter) {
    val appointment = appointmentClient.appointment
    if (list.size > AppointmentDao.LOADING_COUNT_HALF) {
        if (appointment.startTime < list.first().appointment.startTime) {
            loader.isOldestLoaded = false
            return
        } else if (appointment.startTime > list.last().appointment.startTime) {
            loader.isNewestLoaded = false
            return
        }
    }

    var isAdded = false
    for (i: Int in 0..<list.size) {
        val thisAppointment = list[i].appointment

        if (thisAppointment.startTime > appointment.startTime) {
            list.add(i, appointmentClient)
            adapter.notifyItemInserted(i)
            isAdded = true
            break
        }
    }

    if (!isAdded) {
        if (list.isEmpty()) {
            list.add(appointmentClient)
            adapter.notifyItemInserted(0)
        } else if (appointment.startTime < list.first().appointment.startTime) {
            list.add(0, appointmentClient)
            adapter.notifyItemInserted(0)
        } else if (appointment.startTime > list.last().appointment.startTime) {
            list.add(appointmentClient)
            adapter.notifyItemInserted(list.size - 1)
        }
    }
}