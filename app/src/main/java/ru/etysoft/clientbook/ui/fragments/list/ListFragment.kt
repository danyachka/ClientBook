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
import java.time.LocalDate
import java.time.ZoneId

class ListFragment(private var listener: ListFragmentListener) :
        Fragment(R.layout.fragment_list), ListFragmentContract.View {

    private var _binding: FragmentListBinding? = null

    private val binding: FragmentListBinding
        get() = _binding!!

    private lateinit var presenter: ListFragmentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)

        binding.recycler.layoutManager = LinearLayoutManager(context)
        presenter = ListFragmentPresenter(this.requireContext(), this,
                lifecycleScope, binding.recycler, activity = activity as AppCompatActivity)

        binding.buttonAdd.setOnClickListener {
            listener.showCreateBottomSheet()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        presenter.release()
        _binding = null
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

    override fun scrollTo(position: Int) {
        binding.recycler.layoutManager!!.scrollToPosition(position)
    }

    fun goToDate(date: LocalDate) {
        presenter.loadNear(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
    }
}