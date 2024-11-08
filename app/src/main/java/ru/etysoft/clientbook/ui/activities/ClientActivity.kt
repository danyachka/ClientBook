package ru.etysoft.clientbook.ui.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import kotlinx.coroutines.launch
import ru.etysoft.clientbook.R
import ru.etysoft.clientbook.databinding.ActivityClientBinding
import ru.etysoft.clientbook.db.entities.AppointmentClient
import ru.etysoft.clientbook.db.entities.Client
import ru.etysoft.clientbook.db.entities.appointment.Appointment
import ru.etysoft.clientbook.global_observe.GlobalDataChangeNotifier
import ru.etysoft.clientbook.ui.adapters.ScrollListener
import ru.etysoft.clientbook.ui.adapters.appointment.AppointmentAdapter
import ru.etysoft.clientbook.ui.adapters.appointment.AppointmentLoaderListener
import ru.etysoft.clientbook.ui.adapters.appointment.ClientActivityLoader
import ru.etysoft.clientbook.global_observe.GlobalAppointmentsChangingListener
import ru.etysoft.clientbook.global_observe.GlobalClientChangingListener
import ru.etysoft.clientbook.global_observe.processListAddition
import ru.etysoft.clientbook.global_observe.removeFromList
import ru.etysoft.clientbook.utils.Logger
import ru.etysoft.clientbook.utils.TimeUtils

class ClientActivity : AppCompatActivity(), ScrollListener<AppointmentClient>,
        AppointmentLoaderListener, GlobalAppointmentsChangingListener, GlobalClientChangingListener {

    companion object {
        const val CLIENT_JSON = "CLIENT_JSON"
    }

    private lateinit var binding: ActivityClientBinding

    private lateinit var adapter: AppointmentAdapter

    private lateinit var loader: ClientActivityLoader

    private lateinit var client: Client

    private val list: ArrayList<AppointmentClient> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GlobalDataChangeNotifier.instance.registerAppointmentsListener(this)
        GlobalDataChangeNotifier.instance.registerClientsListener(this)

        client = Gson().fromJson(intent.getStringExtra(CLIENT_JSON), Client::class.java)
        binding.phone.text = client.formatedPhoneNumber
        binding.title.text = client.name

        binding.recycler.layoutManager = LinearLayoutManager(this)

        adapter = AppointmentAdapter(
                list = list,
                scrollListener = this,
                context = this,
                activity = this,
                scope = lifecycleScope
        )

        loader = ClientActivityLoader(list, this, adapter, client, lifecycleScope, this)
        adapter.loader = loader
        binding.recycler.adapter = adapter

        loader.loadNear(System.currentTimeMillis())

        initCount()

        binding.buttonBack.setOnClickListener {
            finish()
        }

        val editLauncher = registerForActivityResult(ClientCreationContract()) { _ -> }
        binding.edit.setOnClickListener {
            editLauncher.launch(client)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        GlobalDataChangeNotifier.instance.removeAppointmentsListener(this)
        GlobalDataChangeNotifier.instance.removeClientsListener(this)
    }

    private fun initCount() {
        lifecycleScope.launch {
            val time = System.currentTimeMillis()
            val newer = loader.loadNewerCount(time)
            val older = loader.loadOlderCount(time)

            runOnUiThread {
                binding.next.text = ContextCompat.getString(this@ClientActivity, R.string.client_page_next)
                        .replaceFirst("%t", "$newer")

                binding.all.text = ContextCompat.getString(this@ClientActivity, R.string.client_page_all)
                        .replaceFirst("%t", "${newer + older}")
            }
        }
    }

    private fun updatePlaceHolder(isEmpty: Boolean) {
        if (isEmpty) {
            binding.recycler.visibility = View.GONE
            binding.placeholder.visibility = View.VISIBLE
        } else {
            binding.placeholder.visibility = View.GONE
            binding.recycler.visibility = View.VISIBLE
        }
    }

    override fun onFirstScrolled(dataHolder: AppointmentClient) {
    }

    override fun onLastScrolled(dataHolder: AppointmentClient) {
    }

    override fun onLoaded(centerPos: Int, time: Long) {
        val firstPositionOfDay = TimeUtils().getFirstPositionOfDay(list, System.currentTimeMillis())

        binding.recycler.layoutManager!!.scrollToPosition(firstPositionOfDay)

        updatePlaceHolder(list.isEmpty())
    }

    override fun onOlderLoaded() {
        updatePlaceHolder(list.isEmpty())
    }

    override fun onNewerLoaded() {
        updatePlaceHolder(list.isEmpty())
    }

    override fun onAppointmentRemoved(appointment: Appointment) {
        if (appointment.clientId != client.id) return

        removeFromList(
                appointment = appointment,
                list = list,
                adapter = adapter)

        updatePlaceHolder(list.isEmpty())
    }

    override fun onAppointmentAdded(appointmentClient: AppointmentClient) {
        if (appointmentClient.client.id != client.id) return

        Logger.logDebug(ClientActivity::class.java.simpleName, "Appointment added")

        processListAddition(
                appointmentClient = appointmentClient,
                loader = loader,
                list = list,
                adapter = adapter)

        updatePlaceHolder(list.isEmpty())
    }

    override fun onAppointmentChanged(appointmentClient: AppointmentClient) {
        if (appointmentClient.client.id != client.id) return
        removeFromList(
                appointment = appointmentClient.appointment,
                list = list,
                adapter = adapter)

        onAppointmentAdded(appointmentClient)
    }

    override fun onClientRemoved(client: Client) {
        // wtf?
    }

    override fun onClientAdded(client: Client) {
        // pass
    }

    override fun onClientChanged(client: Client) {
        if (client.id != this.client.id) return
        this.client = client

        binding.phone.text = client.formatedPhoneNumber
        binding.title.text = client.name
    }
}