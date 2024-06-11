package ru.etysoft.clientbook.ui.adapters.appointment

import android.content.Context
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.etysoft.clientbook.db.AppDatabase
import ru.etysoft.clientbook.db.daos.AppointmentDao
import ru.etysoft.clientbook.db.entities.AppointmentClient
import ru.etysoft.clientbook.db.entities.Client
import ru.etysoft.clientbook.db.entities.appointment.Appointment

abstract class AppointmentLoader(
        private val list: ArrayList<AppointmentClient>,
        context: Context,
        private val adapter: AppointmentAdapter,
        private val scope: LifecycleCoroutineScope
) {

    companion object {
        const val MAX_COUNT = 150
    }

    protected val appointmentDao: AppointmentDao = AppDatabase
            .getDatabase(context).getAppointmentDao()

    private var isNewestLoaded = false

    private var isOldestLoaded = false

    abstract suspend fun loadNewer(id: Long): ArrayList<AppointmentClient>

    abstract suspend fun loadOlder(id: Long): ArrayList<AppointmentClient>

    abstract suspend fun getClosest(time: Long): AppointmentClient?

    fun loadNear(time: Long) = scope.launch {
        val closest = getClosest(time) ?: return@launch

        val newer = loadNewer(closest.appointment.id)
        val older = loadOlder(closest.appointment.id).reversed()

        isNewestLoaded = newer.size < AppointmentDao.LOADING_COUNT_HALF
        isOldestLoaded = older.size < AppointmentDao.LOADING_COUNT_HALF

        list.addAll(older)
        list.add(closest)
        list.addAll(newer)

        withContext(Dispatchers.Main) {
            adapter.notifyDataSetChanged()
        }
    }

    fun loadOlder() = scope.launch {
        if (isOldestLoaded) return@launch
        if (list.isEmpty()) return@launch

        val older = loadOlder(list.first().appointment.id).reversed()

        isOldestLoaded = older.size < AppointmentDao.LOADING_COUNT_HALF

        withContext(Dispatchers.Main) {
            list.addAll(0, older)
            adapter.notifyItemRangeInserted(0, older.size)

            if (list.size < MAX_COUNT) return@withContext

            list.subList(list.size - AppointmentDao.LOADING_COUNT_HALF, list.size).clear()
            adapter.notifyItemRangeRemoved(
                    list.size - AppointmentDao.LOADING_COUNT_HALF, AppointmentDao.LOADING_COUNT_HALF)
        }

    }

    fun loadNewer() = scope.launch {
        if (isNewestLoaded) return@launch
        if (list.isEmpty()) return@launch

        val newer = loadNewer(list.last().appointment.id)

        isNewestLoaded = newer.size < AppointmentDao.LOADING_COUNT_HALF

        withContext(Dispatchers.Main) {
            list.addAll(newer)
            adapter.notifyItemRangeInserted(list.size - 1, newer.size)

            if (list.size < MAX_COUNT) return@withContext

            list.subList(0, AppointmentDao.LOADING_COUNT_HALF).clear()
            adapter.notifyItemRangeRemoved(
                    0, AppointmentDao.LOADING_COUNT_HALF)
        }

    }

}


class MainFragmentLoader(
        list: ArrayList<AppointmentClient>,
        context: Context,
        adapter: AppointmentAdapter,
        scope: LifecycleCoroutineScope
) :
        AppointmentLoader(list, context, adapter, scope) {

    override suspend fun loadNewer(id: Long): ArrayList<AppointmentClient> {
        return ArrayList(appointmentDao.getACNewer(id))
    }

    override suspend fun loadOlder(id: Long): ArrayList<AppointmentClient> {
        return ArrayList(appointmentDao.getACOlder(id))
    }

    override suspend fun getClosest(time: Long): AppointmentClient? {
        return appointmentDao.getClosestACByTime(time)
    }
}

class ClientActivityLoader(
        list: ArrayList<AppointmentClient>,
        context: Context,
        adapter: AppointmentAdapter,
        private val client: Client,
        scope: LifecycleCoroutineScope
) :
        AppointmentLoader(list, context, adapter, scope) {

    override suspend fun loadNewer(id: Long): ArrayList<AppointmentClient> {
        val answer = appointmentDao.getNewerClient(id, client.id)

        return convert(answer)
    }

    override suspend fun loadOlder(id: Long): ArrayList<AppointmentClient> {
        val answer = appointmentDao.getOlderByClient(id, client.id)

        return convert(answer)
    }

    override suspend fun getClosest(time: Long): AppointmentClient? {
        return appointmentDao.getClosestACByTimeAndClient(time, clientId = client.id)
    }

    private fun convert(answer: List<Appointment>): ArrayList<AppointmentClient> {
        val result = ArrayList<AppointmentClient>(answer.size)

        for (appointment in answer) result.add(AppointmentClient(appointment, client))
        return result
    }
}
