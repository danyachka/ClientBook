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
        private val scope: LifecycleCoroutineScope,
        private val listener: AppointmentLoaderListener
) {

    companion object {
        const val MAX_COUNT = 150
    }

    protected val appointmentDao: AppointmentDao = AppDatabase
            .getDatabase(context).getAppointmentDao()

    var isNewestLoaded = false

    var isOldestLoaded = false

    abstract suspend fun loadNewer(id: Long): ArrayList<AppointmentClient>

    abstract suspend fun loadOlder(id: Long): ArrayList<AppointmentClient>

    abstract suspend fun getClosest(time: Long): AppointmentClient?

    fun loadNear(time: Long) = scope.launch {
        val result = getClosest(time)
        val closest = if (result != null) result else {
            isNewestLoaded = true
            isOldestLoaded = true
            return@launch
        }

        val newer = loadNewer(closest.appointment.startTime)
        val older = loadOlder(closest.appointment.startTime).reversed()

        isNewestLoaded = newer.size < AppointmentDao.LOADING_COUNT_HALF
        isOldestLoaded = older.size < AppointmentDao.LOADING_COUNT_HALF

        list.addAll(older)
        list.add(closest)
        list.addAll(newer)

        withContext(Dispatchers.Main) {
            adapter.notifyDataSetChanged()
            listener.onLoaded()
        }
    }

    fun loadOlder() = scope.launch {
        if (isOldestLoaded) return@launch
        if (list.isEmpty()) return@launch

        val older = loadOlder(list.first().appointment.startTime).reversed()

        isOldestLoaded = older.size < AppointmentDao.LOADING_COUNT_HALF

        withContext(Dispatchers.Main) {
            list.addAll(0, older)
            adapter.notifyItemRangeInserted(0, older.size)

            if (list.size < MAX_COUNT) return@withContext

            list.subList(list.size - AppointmentDao.LOADING_COUNT_HALF, list.size).clear()
            adapter.notifyItemRangeRemoved(
                    list.size - AppointmentDao.LOADING_COUNT_HALF, AppointmentDao.LOADING_COUNT_HALF)
            listener.onOlderLoaded()
        }

    }

    fun loadNewer() = scope.launch {
        if (isNewestLoaded) return@launch
        if (list.isEmpty()) return@launch

        val newer = loadNewer(list.last().appointment.startTime)

        isNewestLoaded = newer.size < AppointmentDao.LOADING_COUNT_HALF

        withContext(Dispatchers.Main) {
            list.addAll(newer)
            adapter.notifyItemRangeInserted(list.size - 1, newer.size)

            if (list.size < MAX_COUNT) return@withContext

            list.subList(0, AppointmentDao.LOADING_COUNT_HALF).clear()
            adapter.notifyItemRangeRemoved(
                    0, AppointmentDao.LOADING_COUNT_HALF)
            listener.onNewerLoaded()
        }

    }

}


class MainFragmentLoader(
        list: ArrayList<AppointmentClient>,
        context: Context,
        adapter: AppointmentAdapter,
        scope: LifecycleCoroutineScope,
        listener: AppointmentLoaderListener
) :
        AppointmentLoader(list, context, adapter, scope, listener) {

    override suspend fun loadNewer(startTime: Long): ArrayList<AppointmentClient> {
        return ArrayList(appointmentDao.getACNewer(startTime))
    }

    override suspend fun loadOlder(startTime: Long): ArrayList<AppointmentClient> {
        return ArrayList(appointmentDao.getACOlder(startTime))
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
        scope: LifecycleCoroutineScope,
        listener: AppointmentLoaderListener
) :
        AppointmentLoader(list, context, adapter, scope, listener) {

    override suspend fun loadNewer(startTime: Long): ArrayList<AppointmentClient> {
        val answer = appointmentDao.getNewerClient(startTime, client.id)

        return convert(answer)
    }

    override suspend fun loadOlder(startTime: Long): ArrayList<AppointmentClient> {
        val answer = appointmentDao.getOlderByClient(startTime, client.id)

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

interface AppointmentLoaderListener {

    fun onLoaded()

    fun onOlderLoaded()

    fun onNewerLoaded()
}
