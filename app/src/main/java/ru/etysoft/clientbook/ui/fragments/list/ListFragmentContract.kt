package ru.etysoft.clientbook.ui.fragments.list

import ru.etysoft.clientbook.db.entities.AppointmentClient
import ru.etysoft.clientbook.db.entities.appointment.Appointment

interface ListFragmentContract {

    interface View {

        fun onAppointmentAdded(appointmentClient: AppointmentClient)

        fun onAppointmentDeleted(appointmentClient: AppointmentClient)

        fun onAppointmentChanged(appointmentClient: AppointmentClient)

        fun updatePlaceHolder(isEmpty: Boolean)
    }

    interface Presenter {

        fun deleteAppointment(appointmentClient: AppointmentClient)

        fun updateAppointment(appointmentClient: AppointmentClient)

        fun onAppointmentAdded(appointmentClient: AppointmentClient)

    }
}