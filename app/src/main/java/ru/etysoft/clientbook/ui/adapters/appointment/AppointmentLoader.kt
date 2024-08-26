package ru.etysoft.clientbook.ui.adapters.appointment

import android.content.Context
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
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

        MainScope().launch {
            list.clear()

            list.addAll(older)
            list.add(closest)
            list.addAll(newer)

            adapter.notifyDataSetChanged()
            listener.onLoaded(older.size, time)
        }
    }

    fun loadOlder() = scope.launch {
        if (isOldestLoaded) return@launch
        if (list.isEmpty()) return@launch

        val older = loadOlder(list.first().appointment.startTime).reversed()

        isOldestLoaded = older.size < AppointmentDao.LOADING_COUNT_HALF

        MainScope().launch {
            list.addAll(0, older)
            adapter.notifyItemRangeInserted(0, older.size)

            if (list.size < MAX_COUNT) return@launch

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

        MainScope().launch {
            list.addAll(newer)
            adapter.notifyItemRangeInserted(list.size - 1, newer.size)

            if (list.size < MAX_COUNT) return@launch

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
        scope: LifecycleCoroutineScope,
        listener: AppointmentLoaderListener
) :
        AppointmentLoader(list, context, adapter, scope, listener) {

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

    suspend fun loadNewerCount(time: Long): Int = appointmentDao.countNewerForClient(client.id, time)

    suspend fun loadOlderCount(time: Long): Int = appointmentDao.countOlderForClient(client.id, time)
}

interface AppointmentLoaderListener {

    fun onLoaded(centerPos: Int, time: Long)

    fun onOlderLoaded()

    fun onNewerLoaded()
}
