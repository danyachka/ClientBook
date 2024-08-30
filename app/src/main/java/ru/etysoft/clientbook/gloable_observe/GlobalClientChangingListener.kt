package ru.etysoft.clientbook.gloable_observe

import ru.etysoft.clientbook.db.entities.AppointmentClient
import ru.etysoft.clientbook.db.entities.Client

interface GlobalClientChangingListener {

    fun onClientRemoved(client: Client)

    fun onClientAdded(client: Client)

    fun onClientChanged(client: Client)
}