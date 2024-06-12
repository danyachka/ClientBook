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

    private val listFragment: ListFragment

    private val calendarFragment: CalendarFragment

    private val listFragmentListener: ListFragmentListener

    constructor(fragmentManager: FragmentManager,
                lifecycle: Lifecycle, listener: ListFragmentListener) : super(fragmentManager, lifecycle) {
        listFragmentListener = listener

        calendarFragment = CalendarFragment()

        listFragment = ListFragment(listFragmentListener)
    }

    override fun getItemCount(): Int {
        return 2;
    }

    override fun createFragment(position: Int): Fragment {

        val fragment = if (position == 0) {
            calendarFragment
        } else {
            listFragment
        }

        Logger.logDebug(javaClass.simpleName, "Fragment created")

        return fragment
    }




}