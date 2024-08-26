package ru.etysoft.clientbook.ui.fragments.list

import ru.etysoft.clientbook.db.entities.AppointmentClient

interface ListFragmentContract {

    interface View {

        fun updatePlaceHolder(isEmpty: Boolean)

        fun scrollTo(position: Int)
    }

    interface Presenter {

        fun loadNear(time: Long)

        fun release()

    }
}