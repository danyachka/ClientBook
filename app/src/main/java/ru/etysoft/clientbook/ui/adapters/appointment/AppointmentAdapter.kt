package ru.etysoft.clientbook.ui.adapters.appointment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.etysoft.clientbook.R
import ru.etysoft.clientbook.db.entities.AppointmentClient
import ru.etysoft.clientbook.db.entities.Client
import ru.etysoft.clientbook.db.entities.appointment.Appointment
import ru.etysoft.clientbook.ui.adapters.ScrollListener
import ru.etysoft.clientbook.ui.adapters.client.ClientViewHolder

class AppointmentAdapter: RecyclerView.Adapter<AppointmentViewHolder> {

    private val list: List<AppointmentClient>

    private val inflater: LayoutInflater

    private val scrollListener: ScrollListener<AppointmentClient>

    constructor(list: List<AppointmentClient>,
                scrollListener: ScrollListener<AppointmentClient>,
                context: Context) {
        this.list = list
        this.scrollListener = scrollListener

        inflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val itemView = inflater.inflate(R.layout.appointment_element, parent, false)
        return AppointmentViewHolder(itemView)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val appointment = list[position]
        val previous = if (position == 0) appointment else list[position - 1]

        if (position == 0) {
            scrollListener.onFirstScrolled(appointment)
        } else if (position == list.size - 1) {
            scrollListener.onLastScrolled(appointment)
        }

        holder.bind(appointment, previous)
    }
}