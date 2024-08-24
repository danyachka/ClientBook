package ru.etysoft.clientbook.ui.fragments.list

import android.content.Context
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import ru.etysoft.clientbook.db.daos.AppointmentDao
import ru.etysoft.clientbook.db.entities.AppointmentClient
import ru.etysoft.clientbook.db.entities.appointment.Appointment
import ru.etysoft.clientbook.ui.adapters.ScrollListener
import ru.etysoft.clientbook.ui.adapters.appointment.AppointmentAdapter
import ru.etysoft.clientbook.ui.adapters.appointment.AppointmentLoaderListener
import ru.etysoft.clientbook.ui.adapters.appointment.MainFragmentLoader
import ru.etysoft.clientbook.utils.Logger

class ListFragmentPresenter: ListFragmentContract.Presenter,
        ScrollListener<AppointmentClient>, AppointmentLoaderListener {

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
    }

    override fun deleteAppointment(appointmentClient: AppointmentClient) {
        TODO("Not yet implemented")
    }

    override fun updateAppointment(appointmentClient: AppointmentClient) {
        for (i: Int in 0..<list.size) {
            val ac = list[i]
            if (ac.appointment.id != appointmentClient.appointment.id) continue

            list.removeAt(i)
            adapter.notifyItemRemoved(i)
            break
        }

        onAppointmentAdded(appointmentClient)
    }

    override fun onAppointmentAdded(appointmentClient: AppointmentClient) {
        Logger.logDebug(ListFragmentPresenter::class.java.simpleName, "Appointment added")
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

        for (i: Int in 0..<list.size) {
            val thisAppointment = list[i].appointment

            if (thisAppointment.startTime > appointment.startTime) {
                list.add(i, appointmentClient)
                adapter.notifyItemInserted(i)
                break
            }
        }

        view.updatePlaceHolder(list.isEmpty())
    }

    override fun onFirstScrolled(dataHolder: AppointmentClient) {

    }

    override fun onLastScrolled(dataHolder: AppointmentClient) {

    }

    override fun onLoaded(centerPos: Int) {
        Logger.logDebug(ListFragmentPresenter::class.java.simpleName, "onLoaded called: ${list.size}")
        view.updatePlaceHolder(list.isEmpty())
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