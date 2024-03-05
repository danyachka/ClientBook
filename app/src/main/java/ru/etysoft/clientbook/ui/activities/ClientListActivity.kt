package ru.etysoft.clientbook.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.etysoft.clientbook.R

class ClientListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_list)
    }
}