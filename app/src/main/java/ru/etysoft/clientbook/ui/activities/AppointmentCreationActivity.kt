package ru.etysoft.clientbook.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.etysoft.clientbook.R
import ru.etysoft.clientbook.databinding.ActivityAppointmentCreationBinding

class AppointmentCreationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppointmentCreationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppointmentCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}