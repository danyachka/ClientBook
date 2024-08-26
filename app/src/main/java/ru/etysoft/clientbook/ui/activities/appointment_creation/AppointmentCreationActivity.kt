package ru.etysoft.clientbook.ui.activities.appointment_creation

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import ru.etysoft.clientbook.R
import ru.etysoft.clientbook.databinding.ActivityAppointmentCreationBinding
import ru.etysoft.clientbook.db.AppDatabase
import ru.etysoft.clientbook.db.entities.AppointmentClient
import ru.etysoft.clientbook.db.entities.Client
import ru.etysoft.clientbook.db.entities.appointment.Appointment
import ru.etysoft.clientbook.db.entities.appointment.NotificationStatus
import ru.etysoft.clientbook.gloable_observe.GlobalAppointmentObserver
import ru.etysoft.clientbook.ui.activities.AppActivity
import ru.etysoft.clientbook.ui.activities.ClientSelectorContract
import ru.etysoft.clientbook.ui.components.CalendarWidget
import ru.etysoft.clientbook.ui.components.CalendarWidgetListener
import ru.etysoft.clientbook.utils.Logger
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.TextStyle
import java.util.Locale


class AppointmentCreationActivity : AppActivity(), CalendarWidgetListener {

    private lateinit var binding: ActivityAppointmentCreationBinding

    private var isCalendarShown = true

    private lateinit var resultLauncher: ActivityResultLauncher<Client?>

    private var startTime = Time(14, 0)
    private var duration = Time(1, 0)
    private var selectedLocalDate = LocalDate.now()

    private var pickedClient: Client? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppointmentCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        updateCalendar()
        fillDateText(LocalDate.now())

        fillTimeView(binding.startTime, startTime)
        fillTimeView(binding.duration, duration)

        binding.startTimeButton.setOnClickListener {
            val timePickerDialog = TimePickerDialog(this, { _, hourOfDay, minute ->
                startTime = Time(hourOfDay, minute)
                fillTimeView(binding.startTime, startTime)
            }, startTime.hour, startTime.minutes, true)
            timePickerDialog.show()
        }

        binding.durationButton.setOnClickListener {
            val timePickerDialog = TimePickerDialog(this, { _, hourOfDay, minute ->
                duration = Time(hourOfDay, minute)
                fillTimeView(binding.duration, duration)
            }, duration.hour, duration.minutes, true)
            timePickerDialog.show()
        }

        resultLauncher = registerForActivityResult(ClientSelectorContract()) { result ->
            if (result != null) onClientPicked(result)
        }

        binding.calendarButton.setOnClickListener {
            if (isCalendarShown) return@setOnClickListener
            isCalendarShown = true

            updateCalendar()
        }

        binding.clientPicker.setOnClickListener {
            resultLauncher.launch(null)
        }

        binding.buttonBack.setOnClickListener {
            finish()
        }

        binding.confirm.setOnClickListener {
            onConfirm()
        }

    }

    private fun onClientPicked(client: Client) {
        binding.clientPicker.visibility = View.GONE
        binding.clientChosenLayout.visibility = View.VISIBLE

        binding.clientName.text = client.name
        binding.clientPhone.text = client.phoneNumber

        pickedClient = client
    }

    override fun onCalendarClicked(selectedLocalDate: LocalDate) {
        if (!isCalendarShown) return
        isCalendarShown = false

        this.selectedLocalDate = selectedLocalDate
        updateCalendar()

        fillDateText(selectedLocalDate)
    }

    private fun updateCalendar() = binding.calendar.setContent {
        CalendarWidget(listener = this, setBottomPadding = false, isVisible = isCalendarShown)
    }

    private fun fillDateText(date: LocalDate) {
        val s = ContextCompat.getString(this, R.string.appointment_creation_date)
                .replaceFirst("%w", date.dayOfWeek.
                    getDisplayName(TextStyle.FULL_STANDALONE, Locale("ru")))
                .replaceFirst("%d", date.dayOfMonth.toString())
                .replaceFirst("%m", date.month.getDisplayName(TextStyle.FULL_STANDALONE, Locale("ru")))
                .replaceFirst("%y", date.year.toString())

        binding.date.text = s
    }

    private fun fillTimeView(view: TextView, time: Time) {
        view.text = time.toString()
    }

    private fun onConfirm() {
        binding.textError.visibility = View.GONE
        binding.valueError.visibility = View.GONE
        binding.timeError.visibility = View.GONE

        val text: String = binding.text.text.toString().trim()
        val costString: String = binding.value.text.toString()

        if (text.isEmpty()) {
            binding.textError.visibility = View.VISIBLE
            return
        }

        if (costString.isEmpty()) {
            binding.valueError.visibility = View.VISIBLE
            return
        }

        if (pickedClient == null) {
            binding.clientError.visibility = View.VISIBLE
            return
        }

        lifecycleScope.launch {
            val startTime: Long = selectedLocalDate.atTime(startTime.hour, startTime.minutes).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val endTime: Long = startTime + duration.toMillis()

            val appointmentDao = AppDatabase.getDatabase(this@AppointmentCreationActivity).getAppointmentDao()
            val closest = appointmentDao.getBetween(startTime, endTime)

            // if exists
            if (closest != null) {
                runOnUiThread {
                    // if exists
                    showTimeError(closest)
                }
                return@launch
            }

            val cost: Int = costString.toInt()

            val appointment = Appointment(text, cost, pickedClient!!.id, startTime, endTime, NotificationStatus.ENABLED)
            val id: Long = appointmentDao.insert(appointment)
            appointment.id = id

            Logger.logDebug(AppointmentCreationActivity::class.java.simpleName, "Created an Appointment: $appointment")

            runOnUiThread {
                val appointmentClient = AppointmentClient(appointment = appointment, client = pickedClient!!)
                GlobalAppointmentObserver.instance.notifyAdded(appointmentClient)

                val resultIntent = Intent()
                val gson = Gson()
                resultIntent.putExtra(AppointmentCreationContract.APPOINTMENT_CLIENT_RESULT,
                        gson.toJson(appointmentClient))

                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
    }

    private fun showTimeError(appointmentClient: AppointmentClient) {
        val zoneId = ZoneId.systemDefault() // or specify a specific zone, e.g., ZoneId.of("America/New_York")
        val startDateTime: ZonedDateTime = Instant.ofEpochSecond(appointmentClient.appointment.startTime).atZone(zoneId)
        val endDateTime: ZonedDateTime = Instant.ofEpochSecond(appointmentClient.appointment.endTime).atZone(zoneId)

        val startTime = Time(startDateTime.hour, startDateTime.minute)
        val endTime = Time(endDateTime.hour, endDateTime.minute)

        val s = ContextCompat.getString(this, R.string.appointment_creation_date_error)
                .replaceFirst("%t", appointmentClient.appointment.text)
                .replaceFirst("%c", appointmentClient.client.name)
                .replaceFirst("%s", startTime.toString())
                .replaceFirst("%e", endTime.toString())

        binding.timeError.text = s
    }
}

class AppointmentCreationContract: ActivityResultContract<AppointmentClient?, AppointmentClient?>() {

    companion object {
        const val APPOINTMENT_CLIENT_RESULT = "APPOINTMENT_CLIENT_RESULT"
    }

    override fun createIntent(context: Context, input: AppointmentClient?): Intent {
        val intent = Intent(context, AppointmentCreationActivity::class.java)

        val gson = Gson()
        intent.putExtra(APPOINTMENT_CLIENT_RESULT, gson.toJson(input))

        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): AppointmentClient? {

        if (resultCode != Activity.RESULT_OK) return null

        if (intent == null) return null

        val gson = Gson()
        val json = intent.getStringExtra(APPOINTMENT_CLIENT_RESULT)

        return gson.fromJson(json, AppointmentClient::class.java)
    }

}

class Time(val hour: Int, val minutes: Int) {
    override fun toString(): String {
        return String.format("%02d:%02d", hour, minutes)
    }

    fun toMillis(): Long {
        return  ((hour * 60 + minutes) * 60 * 1000).toLong()
    }
}