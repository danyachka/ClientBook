package ru.etysoft.clientbook.ui.fragments.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import ru.etysoft.clientbook.R
import ru.etysoft.clientbook.databinding.FragmentListBinding
import ru.etysoft.clientbook.db.entities.AppointmentClient
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)

        binding.recycler.layoutManager = LinearLayoutManager(context)
        presenter = ListFragmentPresenter(this.requireContext(), this, lifecycleScope, binding.recycler)

        binding.buttonAdd.setOnClickListener {
            listener.showCreateBottomSheet()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun onAppointmentAdded(appointmentClient: AppointmentClient) {
        presenter.onAppointmentAdded(appointmentClient)
    }

    override fun onAppointmentDeleted(appointmentClient: AppointmentClient) {
        TODO("Not yet implemented")
    }

    override fun onAppointmentChanged(appointmentClient: AppointmentClient) {
        presenter.updateAppointment(appointmentClient)
    }

    override fun updatePlaceHolder(isEmpty: Boolean) {
        if (isEmpty) {
            binding.recycler.visibility = View.GONE
            binding.placeholder.visibility = View.VISIBLE
        } else {
            binding.placeholder.visibility = View.GONE
            binding.recycler.visibility = View.VISIBLE
        }
    }
}