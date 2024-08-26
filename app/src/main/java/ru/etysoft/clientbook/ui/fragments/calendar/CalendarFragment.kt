package ru.etysoft.clientbook.ui.fragments.calendar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import ru.etysoft.clientbook.R
import ru.etysoft.clientbook.databinding.FragmentCalendarBinding
import ru.etysoft.clientbook.ui.activities.ClientListActivity
import ru.etysoft.clientbook.ui.activities.MainActivity
import ru.etysoft.clientbook.ui.components.CalendarWidget
import ru.etysoft.clientbook.ui.components.CalendarWidgetListener
import ru.etysoft.clientbook.utils.Logger
import java.time.LocalDate

class CalendarFragment: Fragment(R.layout.fragment_calendar), CalendarWidgetListener {

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

        binding.calendarWidget.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                CalendarWidget(this@CalendarFragment)
            }
        }

        Logger.logDebug(javaClass.simpleName, "Fragment view created")


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun onCalendarClicked(selectedLocalDate: LocalDate) {
        (activity as MainActivity).scrollToDate(selectedLocalDate)
    }
}