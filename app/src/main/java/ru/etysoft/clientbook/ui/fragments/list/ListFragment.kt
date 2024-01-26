package ru.etysoft.clientbook.ui.fragments.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.etysoft.clientbook.R
import ru.etysoft.clientbook.databinding.FragmentListBinding
import ru.etysoft.clientbook.db.entities.appointment.Appointment
import ru.etysoft.clientbook.ui.activities.MainActivity
import ru.etysoft.clientbook.ui.bottomsheets.SelectorBottomSheet

class ListFragment: Fragment(R.layout.fragment_list), ListFragmentContract.View {

    companion object {
        const val TIME_TO_SCROLL = "TIME_TO_SCROLL"
        const val CURRENT: Long = 0
        const val DO_NOT_SCROLL: Long = 1
    }

    private var binding: FragmentListBinding? = null

    private var listener: ListFragmentListener? = null

    private lateinit var presenter: ListFragmentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)

        binding!!.buttonAdd.setOnClickListener {
            var selectorBottomSheet = SelectorBottomSheet();
            selectorBottomSheet.show((activity as MainActivity).supportFragmentManager, "selector_bottom_sheet")
        }

        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listener = activity as ListFragmentListener

        presenter = ListFragmentPresenter(this.context, this)

    }

    override fun onDestroy() {
        super.onDestroy()

        listener = null
    }

    override fun notifyItemsInserted(from: Int, count: Int) {
        TODO("Not yet implemented")
    }

    override fun notifyItemDeleted(pos: Int) {
        TODO("Not yet implemented")
    }

    override fun onAppointmentAdded(appointment: Appointment) {
        TODO("Not yet implemented")
    }

    override fun onAppointmentDeleted(appointment: Appointment) {
        TODO("Not yet implemented")
    }

    override fun onAppointmentChanged(appointment: Appointment) {
        TODO("Not yet implemented")
    }
}