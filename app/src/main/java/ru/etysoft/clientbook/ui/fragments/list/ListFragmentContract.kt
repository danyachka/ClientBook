package ru.etysoft.clientbook.ui.fragments.list

import ru.etysoft.clientbook.db.entities.appointment.Appointment

interface ListFragmentContract {

    interface View {

        fun onAppointmentAdded(appointment: Appointment)

        fun onAppointmentDeleted(appointment: Appointment)

        fun onAppointmentChanged(appointment: Appointment)

    }

    interface Presenter {

        fun deleteAppointment(appointment: Appointment)

        fun updateAppointment(appointment: Appointment)

    }
}