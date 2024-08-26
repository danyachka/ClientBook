package ru.etysoft.clientbook.ui.fragments.list

import android.content.Context
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import ru.etysoft.clientbook.db.entities.AppointmentClient
import ru.etysoft.clientbook.ui.adapters.ScrollListener
import ru.etysoft.clientbook.ui.adapters.appointment.AppointmentAdapter
import ru.etysoft.clientbook.ui.adapters.appointment.AppointmentLoaderListener
import ru.etysoft.clientbook.gloable_observe.GlobalAppointmentObserver
import ru.etysoft.clientbook.gloable_observe.GlobalAppointmentsChangingListener
import ru.etysoft.clientbook.ui.adapters.appointment.MainFragmentLoader
import ru.etysoft.clientbook.utils.Logger

class ListFragmentPresenter: ListFragmentContract.Presenter,
        ScrollListener<AppointmentClient>, AppointmentLoaderListener,
        GlobalAppointmentsChangingListener {

    private val view: ListFragmentContract.View

    private var context: Context?

    private val adapter: AppointmentAdapter

    private val loader: MainFragmentLoader

    private val list: ArrayList<AppointmentClient> = ArrayList()

    private val scope: LifecycleCoroutineScope

    constructor(context: Context,
                view: ListFragmentContract.View,
                scope: LifecycleCoroutineScope,
                recyclerView: RecyclerView) {
        this.context = context
        this.view = view

        this.scope = scope

        adapter = AppointmentAdapter(
                list = list,
                scrollListener = this,
                context = context
        )

        loader = MainFragmentLoader(list, context, adapter, scope, this)
        adapter.loader = loader
        recyclerView.adapter = adapter

        loader.loadNear(System.currentTimeMillis())

        GlobalAppointmentObserver.instance.registerListener(this)
    }

    override fun release() {
        GlobalAppointmentObserver.instance.removeListener(this)
    }

    override fun onAppointmentRemoved(appointmentClient: AppointmentClient) {
        TODO("Not yet implemented")
    }

    override fun onAppointmentChanged(appointmentClient: AppointmentClient) {
        removeFromList(
                appointmentClient = appointmentClient,
                list = list,
                adapter = adapter)

        onAppointmentAdded(appointmentClient)
    }

    override fun onAppointmentAdded(appointmentClient: AppointmentClient) {
        Logger.logDebug(ListFragmentPresenter::class.java.simpleName, "Appointment added")

        processListAddition(
                appointmentClient = appointmentClient,
                loader = loader,
                list = list,
                adapter = adapter)

        view.updatePlaceHolder(list.isEmpty())
    }

    override fun loadNear(time: Long) {
        loader.loadNear(time)
    }

    override fun onFirstScrolled(dataHolder: AppointmentClient) {

    }

    override fun onLastScrolled(dataHolder: AppointmentClient) {

    }

    override fun onLoaded(centerPos: Int, time: Long) {
        Logger.logDebug(ListFragmentPresenter::class.java.simpleName, "onLoaded called: ${list.size}")
        view.updatePlaceHolder(list.isEmpty())

        if (list.isEmpty()) return

        var position = 0
        var closest = list[0].appointment
        for (i: Int in 0..<list.size) {
            val appointment = list[i].appointment

            if (kotlin.math.abs(closest.startTime - time) > kotlin.math.abs(appointment.startTime - time)) {
                position = i
                closest = appointment
            }
        }

        view.scrollTo(position)
    }

    override fun onOlderLoaded() {
        Logger.logDebug(ListFragmentPresenter::class.java.simpleName, "onOlderLoaded called: ${list.size}")
        view.updatePlaceHolder(list.isEmpty())
    }

    override fun onNewerLoaded() {
        Logger.logDebug(ListFragmentPresenter::class.java.simpleName, "onNewerLoaded called: ${list.size}")
        view.updatePlaceHolder(list.isEmpty())
    }
}