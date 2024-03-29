package ru.etysoft.clientbook.ui.fragments.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.etysoft.clientbook.R
import ru.etysoft.clientbook.databinding.FragmentCalendarBinding
import ru.etysoft.clientbook.databinding.FragmentListBinding
import ru.etysoft.clientbook.ui.fragments.list.ListFragmentContract
import ru.etysoft.clientbook.ui.fragments.list.ListFragmentListener
import ru.etysoft.clientbook.ui.fragments.list.ListFragmentPresenter

class CalendarFragment: Fragment(R.layout.fragment_calendar) {

    private var binding: FragmentCalendarBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCalendarBinding.inflate(inflater, container, false)

        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding = null
    }
}