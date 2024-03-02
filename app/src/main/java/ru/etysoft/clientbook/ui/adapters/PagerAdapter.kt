package ru.etysoft.clientbook.ui.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.etysoft.clientbook.ui.fragments.calendar.CalendarFragment
import ru.etysoft.clientbook.ui.fragments.list.ListFragment
import ru.etysoft.clientbook.ui.fragments.list.ListFragmentListener
import ru.etysoft.clientbook.utils.Logger

class PagerAdapter: FragmentStateAdapter {

    private lateinit var listFragment: ListFragment

    private lateinit var calendarFragment: CalendarFragment

    private final var listFragmentListener: ListFragmentListener

    constructor(fragmentManager: FragmentManager,
                lifecycle: Lifecycle, listener: ListFragmentListener) : super(fragmentManager, lifecycle) {
                    listFragmentListener = listener
    }

    override fun getItemCount(): Int {
        return 2;
    }

    override fun createFragment(position: Int): Fragment {

        val fragment: Fragment

        if (position == 0) {
            calendarFragment = CalendarFragment()
            fragment = calendarFragment
        } else {
            listFragment = ListFragment(listFragmentListener)
            fragment = listFragment
        }

        return fragment
    }




}