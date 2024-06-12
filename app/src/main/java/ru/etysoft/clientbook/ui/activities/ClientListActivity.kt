package ru.etysoft.clientbook.ui.activities

import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import ru.etysoft.clientbook.R
import ru.etysoft.clientbook.databinding.ActivityClientListBinding
import ru.etysoft.clientbook.db.AppDatabase
import ru.etysoft.clientbook.db.daos.ClientDao
import ru.etysoft.clientbook.db.entities.Client
import ru.etysoft.clientbook.ui.adapters.ScrollListener
import ru.etysoft.clientbook.ui.adapters.client.ClientAdapter
import ru.etysoft.clientbook.utils.Logger
import kotlin.concurrent.thread


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
        val start = System.currentTimeMillis()
        binding = ActivityClientListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clientDao = AppDatabase.getDatabase(this).getClientDao()

        binding.recycler.layoutManager = LinearLayoutManager(this)
        adapter = ClientAdapter(clientList, this, this)

        binding.recycler.adapter = adapter

        onNewSearch()

        binding.searchType.setOnClickListener {
            val res = onTypeSwitched(searchType)

            if (!res) return@setOnClickListener

            hideKeyboard()

            if (searchType == SearchType.BY_NAME) {
                searchType = SearchType.BY_PHONE
                binding.searchType.setImageDrawable(ContextCompat.getDrawable(
                        applicationContext, R.drawable.ic_phone))

                binding.search.setHint(R.string.search_by_phone_number)
            } else {
                searchType = SearchType.BY_NAME
                binding.searchType.setImageDrawable(ContextCompat.getDrawable(
                        applicationContext, R.drawable.ic_person))

                binding.search.setHint(R.string.search_by_name)
            }
        }

        binding.search.setOnEditorActionListener { _: TextView, actionId: Int, _: KeyEvent? ->
            Logger.logDebug(this.javaClass.simpleName, "Action0 = $actionId")
            if (actionId != EditorInfo.IME_ACTION_SEARCH) return@setOnEditorActionListener false
            Logger.logDebug(this.javaClass.simpleName, "Action1 = $actionId")
            if (isLoadingNow) return@setOnEditorActionListener false
            Logger.logDebug(this.javaClass.simpleName, "Action2 = $actionId")

            onNewSearch()
            hideKeyboard()

            return@setOnEditorActionListener true
        }

        Logger.logDebug(javaClass.simpleName, "Launch time: ${System.currentTimeMillis() - start}")
    }

    private fun onNewSearch() = lifecycleScope.launch {
        val type = searchType
        val text = binding.search.text.toString()

        isLoadingNow = true
        val answer = if (searchType == SearchType.BY_NAME) {
            clientDao.getLatestByName(text)
        } else {
            clientDao.getLatestByPhone(text)
        }

        isLoadingNow = false

        Logger.logDebug(ClientListActivity::class.java.simpleName, "Answer size = " + answer.size)

        isAllLoaded = answer.isEmpty()

        runOnUiThread {
            if (searchType != type) return@runOnUiThread
            if (clientList.isNotEmpty()) {
                val size = clientList.size
                clientList.clear()
                adapter.notifyItemRangeRemoved(0, size)
            }

            clientList.addAll(answer)
            adapter.notifyItemRangeInserted(0, clientList.size)
            Logger.logDebug(ClientListActivity::class.java.simpleName,
                    "Items inserted (0, ${adapter.itemCount})")
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

        lifecycleScope.launch {
            val type = searchType
            isLoadingNow = true
            val answer = if (searchType == SearchType.BY_NAME) {
                clientDao.getNextByName(binding.search.text.toString(), dataHolder.id)
            } else {
                clientDao.getNextByPhone(binding.search.text.toString(), dataHolder.id)
            }

            isLoadingNow = false

            if (answer.isEmpty()) {
                isAllLoaded = true
                return@launch
            }

            runOnUiThread {
                if (searchType != type) return@runOnUiThread
                val size = clientList.size
                clientList.addAll(answer)
                adapter.notifyItemRangeInserted(size, answer.size)

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