package ru.etysoft.clientbook.ui.activities

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.View
import ru.etysoft.clientbook.R
import ru.etysoft.clientbook.databinding.ActivityClentCreationBinding
import ru.etysoft.clientbook.db.AppDatabase
import ru.etysoft.clientbook.db.daos.ClientDao
import ru.etysoft.clientbook.db.entities.Client
import ru.etysoft.clientbook.utils.Logger


class ClientCreationActivity : AppActivity() {

    private lateinit var binding: ActivityClentCreationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClentCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.phoneNumber.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        binding.confirm.setOnClickListener {
            onFinishClicked()
        }

        binding.buttonBack.setOnClickListener {
            finish()
        }
    }

    private fun onFinishClicked() {
        val clientDao = AppDatabase.getDatabase(this).getClientDao()

        val newName: String = binding.name.text.toString()

        val phoneString = binding.phoneNumber.text.toString();
        val newPhoneNumber: String = phoneString.replace("[^0-9]".toRegex(), "")

        if (newName == "") {
            binding.nameError.visibility = View.VISIBLE
            binding.nameError.setText(R.string.client_creation_name_error_no_text)
            return
        }

        runBackground {

            val nameClient = clientDao.getByName(newName)
            val phoneClient = clientDao.getByPhone(newPhoneNumber)

            runOnUiThread {
                var isFinishAvailable = true

                if (nameClient != null) {
                    binding.nameError.visibility = View.VISIBLE
                    binding.nameError.setText(R.string.client_creation_name_error)
                    isFinishAvailable = false
                } else {
                    binding.nameError.visibility = View.GONE
                }

                if (phoneClient != null && newPhoneNumber != "") {
                    binding.phoneError.visibility = View.VISIBLE
                    isFinishAvailable = false
                } else {
                    binding.phoneError.visibility = View.GONE
                }

                if (isFinishAvailable) saveAndFinish(newName, newPhoneNumber, clientDao)
            }

        }
    }

    private fun saveAndFinish(newName: String, newPhone: String, clientDao: ClientDao) {
        runBackground {
            val client = Client(newName, newPhone)

            clientDao.insertAll(client)

            runOnUiThread {
                finish()
            }
        }
    }
}