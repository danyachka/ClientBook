package ru.etysoft.clientbook.ui.adapters.client

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.etysoft.clientbook.R
import ru.etysoft.clientbook.db.entities.Client

class ClientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val imageView: ImageView
    private val nameText: TextView
    private val phoneText: TextView

    init {
        imageView = itemView.findViewById(R.id.client_image)
        nameText = itemView.findViewById(R.id.client_text)
        phoneText = itemView.findViewById(R.id.client_phone)
    }

    public fun bind(client: Client) {
        nameText.text = client.name
        phoneText.text = client.phoneNumber
    }
}