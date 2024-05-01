package ru.etysoft.clientbook.ui.fragments.calendar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.etysoft.clientbook.R
import ru.etysoft.clientbook.databinding.FragmentCalendarBinding
import ru.etysoft.clientbook.ui.activities.ClientListActivity

class CalendarFragment: Fragment(R.layout.fragment_calendar) {

    private var _binding: FragmentCalendarBinding? = null

    private val binding: FragmentCalendarBinding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)

        binding.searchClient.setOnClickListener {
            val intent = Intent(activity, ClientListActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}