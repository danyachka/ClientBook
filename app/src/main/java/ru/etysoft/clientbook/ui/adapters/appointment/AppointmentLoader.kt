package ru.etysoft.clientbook.ui.adapters.appointment

import android.content.Context
import ru.etysoft.clientbook.db.AppDatabase
import ru.etysoft.clientbook.db.daos.AppointmentDao
import ru.etysoft.clientbook.db.entities.AppointmentClient
import ru.etysoft.clientbook.db.entities.Client
import ru.etysoft.clientbook.db.entities.appointment.Appointment
import ru.etysoft.clientbook.ui.activities.AppActivity

abstract class AppointmentLoader(
        private val list: ArrayList<AppointmentClient>,
        context: Context,
        private val adapter: AppointmentAdapter
) {

    companion object {
        const val MAX_COUNT = 150
    }

    protected val appointmentDao: AppointmentDao = AppDatabase
            .getDatabase(context).getAppointmentDao()

    private var isNewestLoaded = false

    private var isOldestLoaded = false

    abstract fun loadNewer(id: Long): ArrayList<AppointmentClient>

    abstract fun loadOlder(id: Long): ArrayList<AppointmentClient>

    abstract fun getClosest(time: Long): AppointmentClient

    fun loadNear(time: Long) {
        AppActivity.runBackground {
            val closest = getClosest(time)

            val newer = loadNewer(closest.appointment.id)
            val older = loadOlder(closest.appointment.id).reversed()

            isNewestLoaded = newer.size < AppointmentDao.LOADING_COUNT_HALF
            isOldestLoaded = older.size < AppointmentDao.LOADING_COUNT_HALF

            list.addAll(older)
            list.add(closest)
            list.addAll(newer)

            AppActivity.runOnUIThread {
                adapter.notifyDataSetChanged()
            }
        }
    }

    fun loadOlder() {
        if (isOldestLoaded) return
        if (list.isEmpty()) return
        AppActivity.runBackground {

            val older = loadOlder(list.first().appointment.id).reversed()

            isOldestLoaded = older.size < AppointmentDao.LOADING_COUNT_HALF

            AppActivity.runOnUIThread {
                list.addAll(0, older)
                adapter.notifyItemRangeInserted(0, older.size)

                if (list.size < MAX_COUNT) return@runOnUIThread

                list.subList(list.size - AppointmentDao.LOADING_COUNT_HALF, list.size).clear()
                adapter.notifyItemRangeRemoved(
                        list.size - AppointmentDao.LOADING_COUNT_HALF, AppointmentDao.LOADING_COUNT_HALF)
            }
        }
    }

    fun loadNewer() {
        if (isNewestLoaded) return
        if (list.isEmpty()) return
        AppActivity.runBackground {

            val newer = loadNewer(list.last().appointment.id)

            isNewestLoaded = newer.size < AppointmentDao.LOADING_COUNT_HALF

            AppActivity.runOnUIThread {
                list.addAll(newer)
                adapter.notifyItemRangeInserted(list.size - 1, newer.size)

                if (list.size < MAX_COUNT) return@runOnUIThread

                list.subList(0, AppointmentDao.LOADING_COUNT_HALF).clear()
                adapter.notifyItemRangeRemoved(
                        0, AppointmentDao.LOADING_COUNT_HALF)
            }
        }
    }

}


class MainFragmentLoader(private val list: ArrayList<AppointmentClient>,
                         context: Context,
                         private val adapter: AppointmentAdapter) :
        AppointmentLoader(list, context, adapter) {

    override fun loadNewer(id: Long): ArrayList<AppointmentClient> {
        return ArrayList(appointmentDao.getACNewer(id))
    }

    override fun loadOlder(id: Long): ArrayList<AppointmentClient> {
        return ArrayList(appointmentDao.getACOlder(id))
    }

    override fun getClosest(time: Long): AppointmentClient {
        return appointmentDao.getClosestACByTime(time)
    }
}

class ClientActivityLoader(
        private val list: ArrayList<AppointmentClient>,
        context: Context,
        private val adapter: AppointmentAdapter,
        private val client: Client) :
        AppointmentLoader(list, context, adapter) {

    override fun loadNewer(id: Long): ArrayList<AppointmentClient> {
        val answer = appointmentDao.getNewerClient(id, client.id)

        return convert(answer)
    }

    override fun loadOlder(id: Long): ArrayList<AppointmentClient> {
        val answer = appointmentDao.getOlderByClient(id, client.id)

        return convert(answer)
    }

    override fun getClosest(time: Long): AppointmentClient {
        return appointmentDao.getClosestACByTimeAndClient(time, clientId = client.id)
    }

    private fun convert(answer: List<Appointment>): ArrayList<AppointmentClient> {
        val result = ArrayList<AppointmentClient>(answer.size)

        for (appointment in answer) result.add(AppointmentClient(appointment, client))
        return result
    }
}
