package ru.etysoft.clientbook.ui.fragments.list

import android.content.Context
import androidx.lifecycle.LifecycleCoroutineScope
import ru.etysoft.clientbook.db.entities.AppointmentClient
import ru.etysoft.clientbook.db.entities.appointment.Appointment
import ru.etysoft.clientbook.ui.adapters.ScrollListener
import ru.etysoft.clientbook.ui.adapters.appointment.AppointmentAdapter
import ru.etysoft.clientbook.ui.adapters.appointment.AppointmentLoaderListener
import ru.etysoft.clientbook.ui.adapters.appointment.MainFragmentLoader

class ListFragmentPresenter: ListFragmentContract.Presenter,
        ScrollListener<AppointmentClient>, AppointmentLoaderListener {

    private val view: ListFragmentContract.View

    private var context: Context?

    private val adapter: AppointmentAdapter

    private val list: ArrayList<AppointmentClient> = ArrayList()

    private val scope: LifecycleCoroutineScope

    constructor(context: Context, view: ListFragmentContract.View, scope: LifecycleCoroutineScope) {
        this.context = context
        this.view = view

        this.scope = scope

        adapter = AppointmentAdapter(
                list = list,
                scrollListener = this,
                context = context
        )

        val loader = MainFragmentLoader(list, context, adapter, scope, this)
        adapter.loader = loader
        loader.loadNear(System.currentTimeMillis())
    }

    override fun deleteAppointment(appointment: Appointment) {
        TODO("Not yet implemented")
    }

    override fun updateAppointment(appointment: Appointment) {
        TODO("Not yet implemented")
    }

    override fun onFirstScrolled(dataHolder: AppointmentClient) {

    }

    override fun onLastScrolled(dataHolder: AppointmentClient) {

    }

    override fun onLoaded() {

    }

    override fun onOlderLoaded() {
        TODO("Not yet implemented")
    }

    override fun onNewerLoaded() {
        TODO("Not yet implemented")
    }
}