package ru.etysoft.clientbook.ui.fragments.list

import ru.etysoft.clientbook.db.entities.appointment.Appointment

interface ListFragmentContract {

    interface View {

        fun notifyItemsInserted(from: Int, count: Int)

        fun notifyItemDeleted(pos: Int)

        fun onAppointmentAdded(appointment: Appointment)

        fun onAppointmentDeleted(appointment: Appointment)

        fun onAppointmentChanged(appointment: Appointment)

    }

    interface Presenter {

        fun loadOlder(time: Long)

        fun loadNewer(time: Long)

        fun loadNear(time: Long)

        fun deleteAppointment(appointment: Appointment)

        fun updateAppointment(appointment: Appointment)

    }
}