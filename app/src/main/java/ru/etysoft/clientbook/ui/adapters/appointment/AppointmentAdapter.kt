package ru.etysoft.clientbook.ui.adapters.appointment

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import ru.etysoft.clientbook.R
import ru.etysoft.clientbook.db.entities.AppointmentClient
import ru.etysoft.clientbook.ui.adapters.ScrollListener
import ru.etysoft.clientbook.utils.Logger
import java.time.LocalDate
import java.time.ZoneId

class AppointmentAdapter(
    private val list: List<AppointmentClient>,
    private val scrollListener: ScrollListener<AppointmentClient>,
    scope: LifecycleCoroutineScope,
    context: Context,
    activity: Activity
) : RecyclerView.Adapter<AppointmentViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    lateinit var loader: AppointmentLoader

    private val balloonManager: BalloonManager = BalloonManagerImplementation(activity, scope)

    private val zoneId: ZoneId = ZoneId.systemDefault()
    private val today: LocalDate = LocalDate.now(zoneId)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val itemView = inflater.inflate(R.layout.appointment_element, parent, false)
        return AppointmentViewHolder(itemView,
                today = today,
                zoneId = zoneId) { appointmentClient, view ->
            balloonManager.openBall0on(appointmentClient, view)
        }
    }

    override fun getItemCount(): Int {
        Logger.logDebug(AppointmentAdapter::class.java.simpleName, "getItemCount called: ${list.size}")
        return list.size
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        Logger.logDebug(AppointmentAdapter::class.java.simpleName, "Binding appointment element at pos = $position")

        val appointment = list[position]
        val previous = if (position == 0) null else list[position - 1]

        notifyListeners(position, appointment)

        holder.bind(appointment, previous)
    }

    private fun notifyListeners(position: Int, element: AppointmentClient) {
        if (list.isEmpty()) return;

        if (position == 0) {
            scrollListener.onFirstScrolled(element)
            loader.loadOlder()
        } else if (position == list.size - 1) {
            scrollListener.onLastScrolled(element)
            loader.loadNewer()
        }
    }
}