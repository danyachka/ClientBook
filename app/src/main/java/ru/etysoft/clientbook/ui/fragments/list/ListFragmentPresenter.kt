package ru.etysoft.clientbook.ui.fragments.list

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import ru.etysoft.clientbook.db.entities.AppointmentClient
import ru.etysoft.clientbook.db.entities.appointment.Appointment
import ru.etysoft.clientbook.global_observe.GlobalDataChangeNotifier
import ru.etysoft.clientbook.global_observe.GlobalAppointmentsChangingListener
import ru.etysoft.clientbook.global_observe.processListAddition
import ru.etysoft.clientbook.global_observe.removeFromList
import ru.etysoft.clientbook.ui.adapters.ScrollListener
import ru.etysoft.clientbook.ui.adapters.appointment.AppointmentAdapter
import ru.etysoft.clientbook.ui.adapters.appointment.AppointmentLoaderListener
import ru.etysoft.clientbook.ui.adapters.appointment.MainFragmentLoader
import ru.etysoft.clientbook.utils.Logger
import ru.etysoft.clientbook.utils.TimeUtils

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
                recyclerView: RecyclerView,
                activity: Activity) {
        this.context = context
        this.view = view

        this.scope = scope

        adapter = AppointmentAdapter(
                list = list,
                scrollListener = this,
                context = context,
                activity = activity,
                scope = scope
        )

        loader = MainFragmentLoader(list, context, adapter, scope, this)
        adapter.loader = loader
        recyclerView.adapter = adapter

        loader.loadNear(System.currentTimeMillis())

        GlobalDataChangeNotifier.instance.registerAppointmentsListener(this)
    }

    override fun release() {
        GlobalDataChangeNotifier.instance.removeAppointmentsListener(this)
    }

    override fun onAppointmentRemoved(appointment: Appointment) {
        removeFromList(
                appointment = appointment,
                list = list,
                adapter = adapter)

        view.updatePlaceHolder(list.isEmpty())
    }

    override fun onAppointmentChanged(appointmentClient: AppointmentClient) {
        removeFromList(
                appointment = appointmentClient.appointment,
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

        val centerAppointment = if (centerPos == list.size) {
            list.last()
        } else {
            list[centerPos]
        }

        val firstPositionOfDay = TimeUtils().getFirstPositionOfDay(list, centerAppointment.appointment.startTime)

        view.scrollTo(firstPositionOfDay)
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