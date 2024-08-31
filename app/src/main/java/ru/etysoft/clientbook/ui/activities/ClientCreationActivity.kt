package ru.etysoft.clientbook.ui.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.View
import androidx.activity.result.contract.ActivityResultContract
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import ru.etysoft.clientbook.R
import ru.etysoft.clientbook.databinding.ActivityClientCreationBinding
import ru.etysoft.clientbook.db.AppDatabase
import ru.etysoft.clientbook.db.daos.ClientDao
import ru.etysoft.clientbook.db.entities.Client
import ru.etysoft.clientbook.gloable_observe.GlobalDataChangeNotifier


class ClientCreationActivity : AppActivity() {

    private lateinit var binding: ActivityClientCreationBinding

    private var isClientUpdating: Boolean = false
    private var client: Client? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(ClientCreationContract.CLIENT_UPDATE)) {
            val client = Gson().fromJson(intent.getStringExtra(ClientCreationContract.CLIENT_UPDATE), Client::class.java)
            this.client = client
            binding.name.setText(client.name)
            binding.phoneNumber.setText(client.phoneNumber)
            isClientUpdating = true
            binding.title.setText(R.string.client_update)
        }

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

        lifecycleScope.launch {

            val nameClient = if (isClientUpdating) null else clientDao.getByName(newName)
            val phoneClient = if (isClientUpdating) null else clientDao.getByPhone(newPhoneNumber)

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
        lifecycleScope.launch {
            val newClient: Client

            if (isClientUpdating) {
                newClient = client!!
                client!!.apply {
                    name = newName
                    phoneNumber = newPhone
                }
                clientDao.update(newClient)
            } else {
                newClient = Client(newName, newPhone)
                clientDao.insertAll(newClient)
            }

            runOnUiThread {
                if (isClientUpdating) {
                    GlobalDataChangeNotifier.instance.notifyClientsChanged(newClient)
                } else {
                    GlobalDataChangeNotifier.instance.notifyClientsAdded(newClient)
                }

                val intent = Intent()
                intent.putExtra(
                        ClientCreationContract.CLIENT_RESULT, Gson().toJson(newClient))

                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }
}

class ClientCreationContract: ActivityResultContract<Client?, Client?>() {

    companion object {
        const val CLIENT_RESULT = "CLIENT_RESULT"
        const val CLIENT_UPDATE = "CLIENT_UPDATE"
    }

    override fun createIntent(context: Context, input: Client?): Intent {
        val intent = Intent(context, ClientCreationActivity::class.java)

        val gson = Gson()
        intent.putExtra(CLIENT_UPDATE, gson.toJson(input))

        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Client? {

        if (resultCode != Activity.RESULT_OK) return null

        if (intent == null) return null

        val gson = Gson()
        val json = intent.getStringExtra(CLIENT_RESULT)

        return gson.fromJson(json, Client::class.java)
    }

}