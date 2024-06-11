package ru.etysoft.clientbook.ui.fragments.list

import android.content.Context
import ru.etysoft.clientbook.db.entities.appointment.Appointment

class ListFragmentPresenter: ListFragmentContract.Presenter {

    private lateinit var view: ListFragmentContract.View

    private var context: Context?

    constructor(context: Context?, view: ListFragmentContract.View) {
        this.context = context
        this.view = view
    }

    override fun deleteAppointment(appointment: Appointment) {
        TODO("Not yet implemented")
    }

    override fun updateAppointment(appointment: Appointment) {
        TODO("Not yet implemented")
    }
}