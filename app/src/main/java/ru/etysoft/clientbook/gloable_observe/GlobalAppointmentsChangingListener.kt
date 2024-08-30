package ru.etysoft.clientbook.gloable_observe

import ru.etysoft.clientbook.db.daos.AppointmentDao
import ru.etysoft.clientbook.db.entities.AppointmentClient
import ru.etysoft.clientbook.ui.adapters.appointment.AppointmentAdapter
import ru.etysoft.clientbook.ui.adapters.appointment.AppointmentLoader

interface GlobalAppointmentsChangingListener {

    fun onAppointmentRemoved(appointmentClient: AppointmentClient)

    fun onAppointmentAdded(appointmentClient: AppointmentClient)

    fun onAppointmentChanged(appointmentClient: AppointmentClient)
}

fun removeFromList(appointmentClient: AppointmentClient,
                   list: ArrayList<AppointmentClient>,
                   adapter: AppointmentAdapter) {
    for (i: Int in 0..<list.size) {
        val ac = list[i]
        if (ac.appointment.id != appointmentClient.appointment.id) continue

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
        if (appointment.startTime < list.first().appointment.startTime) {
            list.add(0, appointmentClient)
            adapter.notifyItemInserted(0)
        } else if (appointment.startTime > list.last().appointment.startTime) {
            list.add(appointmentClient)
            adapter.notifyItemInserted(list.size - 1)
        }
    }
}