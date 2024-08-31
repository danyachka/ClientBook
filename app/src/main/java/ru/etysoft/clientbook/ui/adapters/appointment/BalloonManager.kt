package ru.etysoft.clientbook.ui.adapters.appointment

import android.app.Activity
import android.content.Intent
import android.view.View
import com.google.gson.Gson
import ru.etysoft.clientbook.db.AppDatabase
import ru.etysoft.clientbook.db.entities.AppointmentClient
import ru.etysoft.clientbook.db.entities.Client
import ru.etysoft.clientbook.db.entities.appointment.Appointment
import ru.etysoft.clientbook.ui.activities.ClientActivity
import ru.etysoft.clientbook.ui.activities.appointment_creation.AppointmentCreationActivity
import ru.etysoft.clientbook.ui.activities.appointment_creation.AppointmentCreationContract

class BalloonManagerImplementation(
        private val activity: Activity
): BalloonListener, BalloonManager() {

    private var isBalloonShown = false

    private val appointmentDao = AppDatabase.getDatabase(activity).getAppointmentDao()

    override fun openBall0on(appointmentClient: AppointmentClient, view: View) {
        if (isBalloonShown) return
        isBalloonShown = true

        val appointmentsBalloon = AppointmentsBalloon(
                balloonOpenListener = this,
                appointmentClient = appointmentClient,
                context = activity
        )

        appointmentsBalloon.createBalloon(view)
    }

    override fun onBalloonClosed() {
        isBalloonShown = false
    }

    override fun onBalloonAppointmentRemoveClicked(appointment: Appointment) {
        TODO("Open alertDialog")
    }

    override fun onBalloonOpenClientClicked(client: Client) {
        val intent = Intent(activity, ClientActivity::class.java)
        intent.putExtra(ClientActivity.CLIENT_JSON,
                Gson().toJson(client))
        activity.startActivity(intent)
    }

    override fun onBalloonEditAppointmentClicked(appointmentClient: AppointmentClient) {
        val intent = Intent(activity, AppointmentCreationActivity::class.java)
        intent.putExtra(AppointmentCreationContract.APPOINTMENT_CLIENT_UPDATE,
                Gson().toJson(appointmentClient))
        activity.startActivity(intent)
    }
}

abstract class BalloonManager {
    abstract fun openBall0on(appointmentClient: AppointmentClient, view: View)
}