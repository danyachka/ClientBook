package ru.etysoft.clientbook.ui.fragments.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import ru.etysoft.clientbook.R
import ru.etysoft.clientbook.databinding.FragmentListBinding
import ru.etysoft.clientbook.db.entities.appointment.Appointment
import ru.etysoft.clientbook.ui.activities.MainActivity
import ru.etysoft.clientbook.ui.bottomsheets.SelectorBottomSheet

class ListFragment(private var listener: ListFragmentListener) :
        Fragment(R.layout.fragment_list), ListFragmentContract.View {

    companion object {
        const val TIME_TO_SCROLL = "TIME_TO_SCROLL"
        const val CURRENT: Long = 0
        const val DO_NOT_SCROLL: Long = 1
    }

    private var _binding: FragmentListBinding? = null

    private val binding: FragmentListBinding
        get() = _binding!!

    private lateinit var presenter: ListFragmentContract.Presenter

    private val appointmentList = ArrayList<Appointment>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)

        binding.buttonAdd.setOnClickListener {
            listener.showCreateBottomSheet()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = ListFragmentPresenter(this.requireContext(), this, lifecycleScope)

    }

    override fun onDestroy() {
        super.onDestroy()

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