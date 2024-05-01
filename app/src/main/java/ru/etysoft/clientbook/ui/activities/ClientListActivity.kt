package ru.etysoft.clientbook.ui.activities

import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import ru.etysoft.clientbook.R
import ru.etysoft.clientbook.databinding.ActivityClientListBinding
import ru.etysoft.clientbook.db.AppDatabase
import ru.etysoft.clientbook.db.daos.ClientDao
import ru.etysoft.clientbook.db.entities.Client
import ru.etysoft.clientbook.ui.adapters.ScrollListener
import ru.etysoft.clientbook.ui.adapters.client.ClientAdapter
import ru.etysoft.clientbook.utils.Logger


class ClientListActivity : AppActivity(), ScrollListener<Client> {

    private lateinit var binding: ActivityClientListBinding

    private lateinit var adapter: ClientAdapter

    private lateinit var clientDao: ClientDao

    private val clientList: ArrayList<Client> = ArrayList()

    private var searchType: SearchType = SearchType.BY_NAME

    private var isLoadingNow = false

    private var isAllLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clientDao = AppDatabase.getDatabase(this).getClientDao()

        adapter = ClientAdapter(clientList, this, this)

        binding.recycler.adapter = adapter

        onNewSearch()

        binding.searchTypeName.setOnClickListener {
            if (searchType == SearchType.BY_NAME) return@setOnClickListener

            val res = onTypeSwitched(SearchType.BY_NAME)

            if (!res) return@setOnClickListener

            hideKeyboard()

            binding.searchTypeName.setColorFilter(R.color.accent, PorterDuff.Mode.SRC_ATOP)
            binding.searchTypePhone.setColorFilter(R.color.accent_medium, PorterDuff.Mode.SRC_ATOP)
        }

        binding.searchTypePhone.setOnClickListener {
            if (searchType == SearchType.BY_PHONE) return@setOnClickListener

            val res = onTypeSwitched(SearchType.BY_PHONE)

            if (!res) return@setOnClickListener

            hideKeyboard()

            binding.searchTypeName.setColorFilter(R.color.accent_medium, PorterDuff.Mode.SRC_ATOP)
            binding.searchTypePhone.setColorFilter(R.color.accent, PorterDuff.Mode.SRC_ATOP)
        }

        binding.search.setOnEditorActionListener { _: TextView, actionId: Int, _: KeyEvent? ->
            if (actionId != EditorInfo.IME_ACTION_SEARCH) return@setOnEditorActionListener false
            if (isLoadingNow) return@setOnEditorActionListener false

            onNewSearch()
            hideKeyboard()

            return@setOnEditorActionListener true
        }
    }

    private fun onNewSearch() {
        runBackground {
            val text = binding.search.text.toString()

            isLoadingNow = true
            val answer = if (searchType == SearchType.BY_NAME) {
                clientDao.getLatestByName(text)
            } else {
                clientDao.getLatestByPhone(text)
            }

            Logger.logDebug(ClientListActivity::class.java.simpleName, "Answer size = " + answer.size)

            isAllLoaded = answer.isEmpty()

            runOnUiThread {
                if (clientList.isNotEmpty()) {
                    val size = clientList.size
                    clientList.clear()
                    adapter.notifyItemRangeRemoved(0, size)
                }

                clientList.addAll(answer)
                adapter.notifyItemRangeInserted(0, answer.size)

                isLoadingNow = false
            }
        }
    }

    private fun onTypeSwitched(type: SearchType): Boolean {
        if (isLoadingNow) return false

        searchType = type
        binding.search.setText("")
        onNewSearch()
        return true
    }

    override fun onFirstScrolled(dataHolder: Client) {
    }

    override fun onLastScrolled(dataHolder: Client) {
        if (isAllLoaded) return

        runBackground {
            isLoadingNow = true
            val answer = if (searchType == SearchType.BY_NAME) {
                clientDao.getNextByName(binding.search.text.toString(), dataHolder.id)
            } else {
                clientDao.getNextByPhone(binding.search.text.toString(), dataHolder.id)
            }

            if (answer.isEmpty()) {
                isAllLoaded = true
                return@runBackground
            }

            runOnUiThread {
                val size = clientList.size
                clientList.addAll(answer)
                adapter.notifyItemRangeInserted(size, answer.size)

                isLoadingNow = false
            }
        }
    }

    private fun hideKeyboard() {
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    enum class SearchType {
        BY_NAME,

        BY_PHONE
    }
}