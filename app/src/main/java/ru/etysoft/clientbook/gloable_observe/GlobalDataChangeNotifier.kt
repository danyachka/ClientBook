package ru.etysoft.clientbook.gloable_observe

import ru.etysoft.clientbook.db.entities.AppointmentClient
import ru.etysoft.clientbook.db.entities.Client
import ru.etysoft.clientbook.db.entities.appointment.Appointment
import ru.etysoft.clientbook.utils.Logger


class GlobalDataChangeNotifier {

    companion object {
        private var _instance: GlobalDataChangeNotifier? = GlobalDataChangeNotifier()

        val instance: GlobalDataChangeNotifier
            get() = _instance!!
    }

    init {
        if (_instance != null) {
            throw Exception("GlobalAppointmentObserver's instance is already defined!")
        }
    }

    private val appointmentsListeners = ArrayList<GlobalAppointmentsChangingListener>()
    private val clientsListeners = ArrayList<GlobalClientChangingListener>()

    fun release() = appointmentsListeners.clear()

    fun registerAppointmentsListener(listener: GlobalAppointmentsChangingListener) {
        appointmentsListeners.add(listener)
    }

    fun removeAppointmentsListener(listener: GlobalAppointmentsChangingListener) {
        appointmentsListeners.remove(listener)
    }

    fun registerClientsListener(listener: GlobalClientChangingListener) {
        clientsListeners.add(listener)
    }

    fun removeClientsListener(listener: GlobalClientChangingListener) {
        clientsListeners.remove(listener)
    }


    fun notifyAppointmentsAdded(appointment: AppointmentClient) {
        Logger.logDebug(GlobalDataChangeNotifier::class.java.simpleName, "Notifying added: ${appointment.appointment}")
        for (listener in appointmentsListeners) {
            listener.onAppointmentAdded(appointment)
        }
    }

    fun notifyAppointmentsChanged(appointment: AppointmentClient) {
        Logger.logDebug(GlobalDataChangeNotifier::class.java.simpleName, "Notifying changed: ${appointment.appointment}")
        for (listener in appointmentsListeners) {
            listener.onAppointmentChanged(appointment)
        }
    }

    fun notifyAppointmentsRemoved(appointment: Appointment) {
        Logger.logDebug(GlobalDataChangeNotifier::class.java.simpleName, "Notifying removed: ${appointment.id}")
        for (listener in appointmentsListeners) {
            listener.onAppointmentRemoved(appointment)
        }
    }

    fun notifyClientsAdded(client: Client) {
        Logger.logDebug(GlobalDataChangeNotifier::class.java.simpleName, "Notifying added: ${client.name}")
        for (listener in clientsListeners) {
            listener.onClientAdded(client)
        }
    }

    fun notifyClientsChanged(client: Client) {
        Logger.logDebug(GlobalDataChangeNotifier::class.java.simpleName, "Notifying changed: ${client.name}")
        for (listener in clientsListeners) {
            listener.onClientChanged(client)
        }
    }

    fun notifyClientsRemoved(client: Client) {
        Logger.logDebug(GlobalDataChangeNotifier::class.java.simpleName, "Notifying added: ${client.name}")
        for (listener in clientsListeners) {
            listener.onClientRemoved(client)
        }
    }
}