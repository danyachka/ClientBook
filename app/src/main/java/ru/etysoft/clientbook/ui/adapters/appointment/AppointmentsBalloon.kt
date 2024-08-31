package ru.etysoft.clientbook.ui.adapters.appointment

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.radius.RadiusLayout
import ru.etysoft.clientbook.R
import ru.etysoft.clientbook.db.entities.AppointmentClient
import ru.etysoft.clientbook.db.entities.Client
import ru.etysoft.clientbook.db.entities.appointment.Appointment

class AppointmentsBalloon(
        private val balloonOpenListener: BalloonListener,
        context: Context,
        private val appointmentClient: AppointmentClient
) {
    private var isOpened = false

    private val balloon: Balloon = Balloon.Builder(context)
            .setLayout(R.layout.appoointment_balloon)
            .setBalloonAnimation(BalloonAnimation.OVERSHOOT)
            .setIsVisibleArrow(false)
            .build()

    fun createBalloon(view: View) {
        if (isOpened) return
        isOpened = true

        val layout = balloon.getContentView() as RadiusLayout
        layout.isFocusable = false

        layout.background = ContextCompat.getDrawable(view.context, R.drawable.tooltip_drawable)
        val clientOpen = layout.findViewById<LinearLayout>(R.id.client_row)
        val editRow = layout.findViewById<LinearLayout>(R.id.edit_row)
        val deleteRow = layout.findViewById<LinearLayout>(R.id.delete_row)

        clientOpen.setOnClickListener {
            balloonOpenListener.onBalloonOpenClientClicked(appointmentClient.client)
            balloon.dismiss()
        }

        editRow.setOnClickListener {
            balloonOpenListener.onBalloonEditAppointmentClicked(appointmentClient)
            balloon.dismiss()
        }

        deleteRow.setOnClickListener {
            balloonOpenListener.onBalloonAppointmentRemoveClicked(appointmentClient.appointment)
            balloon.dismiss()
        }

        balloon.setOnBalloonDismissListener {
            balloonOpenListener.onBalloonClosed()
        }

        balloon.showAlignBottom(view)
    }

}

interface BalloonListener {

    fun onBalloonClosed()

    fun onBalloonAppointmentRemoveClicked(appointment: Appointment)

    fun onBalloonOpenClientClicked(client: Client)

    fun onBalloonEditAppointmentClicked(appointmentClient: AppointmentClient)

}