package ru.etysoft.clientbook.ui.adapters.appointment

import android.media.Image
import android.opengl.Visibility
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.etysoft.clientbook.R
import ru.etysoft.clientbook.db.entities.AppointmentClient
import ru.etysoft.clientbook.db.entities.appointment.Appointment
import ru.etysoft.clientbook.db.entities.appointment.NotificationStatus

class AppointmentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val dateText: TextView
    private val timeText: TextView
    private val nameText: TextView
    private val clientNameText: TextView
    private val valueText: TextView

    private val notificationIcon: ImageView

    init {
        dateText = itemView.findViewById(R.id.date_text)
        timeText = itemView.findViewById(R.id.time_text)
        nameText = itemView.findViewById(R.id.name)
        clientNameText = itemView.findViewById(R.id.client_name)
        valueText= itemView.findViewById(R.id.value)
        notificationIcon = itemView.findViewById(R.id.notification_indicator)
    }

    fun bind(appointment: AppointmentClient, previous: AppointmentClient) {

        dateText.visibility = if (appointment.appointment.isSameDay(previous.appointment)) GONE
                                else VISIBLE
        dateText.text = appointment.appointment.dateText

        timeText.text = appointment.appointment.timeText

        nameText.text = appointment.client.name

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