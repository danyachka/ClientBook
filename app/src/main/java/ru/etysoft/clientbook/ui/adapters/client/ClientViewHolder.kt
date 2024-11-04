package ru.etysoft.clientbook.ui.adapters.client

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.etysoft.clientbook.R
import ru.etysoft.clientbook.db.entities.Client

class ClientViewHolder(itemView: View,
                       private val listener: ClientViewHolderListener
) : ViewHolder(itemView) {

    private val imageView: ImageView = itemView.findViewById(R.id.client_image)
    private val nameText: TextView = itemView.findViewById(R.id.client_text)
    private val phoneText: TextView = itemView.findViewById(R.id.client_phone)

    fun bind(client: Client) {
        nameText.text = client.name

        phoneText.text = client.formatedPhoneNumber

        itemView.setOnClickListener {
            listener.onViewHolderSelected(client, this)
        }
    }
}

interface ClientViewHolderListener {
    fun onViewHolderSelected(client: Client, clientViewHolder: ClientViewHolder)

}