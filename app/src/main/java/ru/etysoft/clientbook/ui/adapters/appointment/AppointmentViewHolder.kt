package ru.etysoft.clientbook.ui.adapters.appointment

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.etysoft.clientbook.R
import ru.etysoft.clientbook.db.entities.AppointmentClient
import ru.etysoft.clientbook.db.entities.appointment.NotificationStatus
import java.time.LocalDate
import java.time.ZoneId

class AppointmentViewHolder(
    itemView: View,
    private val today: LocalDate,
    private val zoneId: ZoneId,
    private val onAppointmentElementClicked: OnAppointmentElementClicked
): RecyclerView.ViewHolder(itemView) {

    private val appointmentElement: LinearLayout = itemView.findViewById(R.id.appointment_element)
    private val dateText: TextView = itemView.findViewById(R.id.date_text)
    private val timeText: TextView = itemView.findViewById(R.id.time_text)
    private val text: TextView
    private val clientNameText: TextView
    private val valueText: TextView

    private val notificationIcon: ImageView

    init {
        text = itemView.findViewById(R.id.text)
        clientNameText = itemView.findViewById(R.id.client_name)
        valueText= itemView.findViewById(R.id.value)
        notificationIcon = itemView.findViewById(R.id.notification_indicator)
    }

    fun bind(appointment: AppointmentClient, previous: AppointmentClient?) {

        appointmentElement.setOnClickListener {
            onAppointmentElementClicked(appointment, itemView)
        }

        dateText.visibility =
                if (previous == null) VISIBLE
                else if (appointment.appointment.isSameDay(previous.appointment, zoneId)) GONE
                else VISIBLE

        val yesterday = today.minusDays(1)
        val tomorrow = today.plusDays(1)
        if (dateText.visibility == VISIBLE) dateText.text = appointment.appointment
                .getDateText(itemView.context, today, tomorrow, yesterday, zoneId)

        timeText.text = appointment.appointment.getTimeText(zoneId)

        text.text = appointment.appointment.text

        clientNameText.text = appointment.client.name

        valueText.text = appointment.appointment.value.toString()

        if (appointment.appointment.notificationStatus == NotificationStatus.ENABLED) {
            notificationIcon.setImageDrawable(ContextCompat.
                getDrawable(itemView.context, R.drawable.ic_notifications))
        } else {
            notificationIcon.setImageDrawable(ContextCompat.
                getDrawable(itemView.context, R.drawable.ic_notifications_off))
        }
    }

}

typealias OnAppointmentElementClicked = (AppointmentClient, View) -> Unit
