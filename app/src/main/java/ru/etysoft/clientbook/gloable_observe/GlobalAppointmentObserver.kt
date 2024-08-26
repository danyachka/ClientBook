package ru.etysoft.clientbook.gloable_observe

import androidx.compose.animation.core.rememberInfiniteTransition
import ru.etysoft.clientbook.db.entities.AppointmentClient
import ru.etysoft.clientbook.utils.Logger


class GlobalAppointmentObserver {

    companion object {
        private var _instance: GlobalAppointmentObserver? = GlobalAppointmentObserver()

        val instance: GlobalAppointmentObserver
            get() = _instance!!
    }

    init {
        if (_instance != null) {
            throw Exception("GlobalAppointmentObserver's instance is already defined!")
        }
    }

    private val listeners = ArrayList<GlobalAppointmentsChangingListener>()

    fun release() = listeners.clear()

    fun registerListener(listener: GlobalAppointmentsChangingListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: GlobalAppointmentsChangingListener) {
        listeners.remove(listener)
    }

    fun notifyAdded(appointment: AppointmentClient) {
        Logger.logDebug(GlobalAppointmentObserver::class.java.simpleName, "Notifying added: ${appointment.appointment}")
        for (listener in listeners) {
            listener.onAppointmentAdded(appointment)
        }
    }

    fun notifyRemoved(appointment: AppointmentClient) {
        Logger.logDebug(GlobalAppointmentObserver::class.java.simpleName, "Notifying added: ${appointment.appointment}")
        for (listener in listeners) {
            listener.onAppointmentRemoved(appointment)
        }
    }
}