package ru.etysoft.clientbook.ui.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import ru.etysoft.clientbook.R
import ru.etysoft.clientbook.databinding.ActivityAppointmentCreationBinding
import ru.etysoft.clientbook.db.entities.AppointmentClient

class AppointmentCreationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppointmentCreationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppointmentCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        

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