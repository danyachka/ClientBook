package ru.etysoft.clientbook.ui.activities

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import ru.etysoft.clientbook.R
import ru.etysoft.clientbook.databinding.ActivityMainBinding
import ru.etysoft.clientbook.db.entities.AppointmentClient
import ru.etysoft.clientbook.ui.adapters.PagerAdapter
import ru.etysoft.clientbook.ui.bottomsheets.SelectorBottomSheet
import ru.etysoft.clientbook.ui.fragments.list.ListFragmentListener
import ru.etysoft.clientbook.utils.Logger
import java.time.LocalDate

class MainActivity : AppActivity(), ListFragmentListener {

    private lateinit var binding: ActivityMainBinding

    private var isListShowed = true

    private lateinit var pagerAdapter: PagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createPager()
        initBar()
    }

    private fun initBar() {

        binding.listButton.setOnClickListener {
            if (isListShowed) return@setOnClickListener
            binding.pager.currentItem = 1

            isListShowed = true
        }

        binding.calendarButton.setOnClickListener {
            if (!isListShowed) return@setOnClickListener
            binding.pager.currentItem = 0

            isListShowed = false
        }

    }

    private fun createPager() {
        val viewPager = binding.pager

        pagerAdapter = PagerAdapter(supportFragmentManager, lifecycle, this)
        viewPager.adapter = pagerAdapter

        viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 1) {
                    colorListButton()
                } else {
                    colorCalendarButton()
                }
            }
        })

        viewPager.currentItem = 1
    }

    override fun showCreateBottomSheet() {
        val selectorBottomSheet = SelectorBottomSheet()
        selectorBottomSheet.show(supportFragmentManager, "selector_bottom_sheet")
    }

    private fun colorListButton() {
        binding.listButton.setColorFilter(ContextCompat.getColor(applicationContext, R.color.accent))
        binding.calendarButton.setColorFilter(ContextCompat.getColor(applicationContext, R.color.accent_dark))

        Logger.logDebug(MainActivity::class.java.simpleName + "_bar", "List is shown")
    }

    private fun colorCalendarButton() {
        binding.listButton.setColorFilter(ContextCompat.getColor(applicationContext, R.color.accent_dark))
        binding.calendarButton.setColorFilter(ContextCompat.getColor(applicationContext, R.color.accent))

        Logger.logDebug(MainActivity::class.java.simpleName + "_bar", "Calendar is shown")
    }

    fun scrollToDate(date: LocalDate) {
        binding.pager.currentItem = 1
        pagerAdapter.listFragment.goToDate(date)
    }

}