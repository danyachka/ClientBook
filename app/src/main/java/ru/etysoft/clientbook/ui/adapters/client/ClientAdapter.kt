package ru.etysoft.clientbook.ui.adapters.client

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.etysoft.clientbook.R
import ru.etysoft.clientbook.db.entities.Client
import ru.etysoft.clientbook.ui.adapters.ScrollListener
import ru.etysoft.clientbook.utils.Logger
class ClientAdapter : RecyclerView.Adapter<ClientViewHolder> {

    private val clientList: List<Client>

    private val inflater: LayoutInflater

    private val scrollListener: ScrollListener<Client>

    constructor(clientList: List<Client>,
                scrollListener: ScrollListener<Client>,
                context: Context) {
        this.clientList = clientList
        this.scrollListener = scrollListener

        inflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientViewHolder {
        val itemView = inflater.inflate(R.layout.client_element, parent, false)
        return ClientViewHolder(itemView)
    }

    override fun getItemCount() : Int {
        Logger.logDebug(this.javaClass.simpleName, "Clients item count = ${clientList.size}")

        return clientList.size
    }

    override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
        val client = clientList[position]

        if (position == 0) {
            scrollListener.onFirstScrolled(client)
        } else if (position == clientList.size - 1) {
            scrollListener.onLastScrolled(client)
        }

        Logger.logDebug(ClientAdapter::class.java.simpleName, "Holder bound (size = " +
                "$itemCount)")

        holder.bind(client)
    }
}

