package ru.etysoft.clientbook.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.etysoft.clientbook.R
import ru.etysoft.clientbook.databinding.ActivityMainBinding
import ru.etysoft.clientbook.ui.fragments.calendar.CalendarFragment
import ru.etysoft.clientbook.ui.fragments.list.ListFragment
import ru.etysoft.clientbook.ui.fragments.list.ListFragmentListener

class MainActivity : AppCompatActivity(), ListFragmentListener {

    private var binding: ActivityMainBinding? = null

    private var isListShowed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        if (savedInstanceState == null) {
            val bundle = Bundle()
            bundle.putLong(ListFragment.TIME_TO_SCROLL, ListFragment.CURRENT.toLong())
            supportFragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container, ListFragment::class.java, bundle)
                    .commit()
        }

        initBar()
    }

    private fun initBar() {

        binding?.listButton?.setOnClickListener {
            if (isListShowed) return@setOnClickListener

            isListShowed = true
            val bundle = Bundle()
            bundle.putLong(ListFragment.TIME_TO_SCROLL, ListFragment.DO_NOT_SCROLL)

            supportFragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container, ListFragment::class.java, bundle)
                    .commit()

            binding!!.calendarButton.setColorFilter(R.color.accent_dark)
            binding!!.listButton.setColorFilter(R.color.accent)
        }

        binding?.calendarButton?.setOnClickListener {
            if (!isListShowed) return@setOnClickListener

            isListShowed = false
            val bundle = Bundle()

            supportFragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container, CalendarFragment::class.java, bundle)
                    .commit()

            binding!!.listButton.setColorFilter(R.color.accent_dark)
            binding!!.calendarButton.setColorFilter(R.color.accent)
        }

    }

    override fun setLastScroll(scrollTime: Long) {
        TODO("Not yet implemented")
    }

}