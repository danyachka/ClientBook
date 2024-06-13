package ru.etysoft.clientbook.ui.adapters.client

import android.telephony.PhoneNumberUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.etysoft.clientbook.R
import ru.etysoft.clientbook.db.entities.Client

class ClientViewHolder(itemView: View,
                       private val listener: ClientViewHolderListener
) : ViewHolder(itemView) {

    private val imageView: ImageView
    private val nameText: TextView
    private val phoneText: TextView

    init {
        imageView = itemView.findViewById(R.id.client_image)
        nameText = itemView.findViewById(R.id.client_text)
        phoneText = itemView.findViewById(R.id.client_phone)
    }

    fun bind(client: Client) {
        nameText.text = client.name

        val formattedPhoneNumber = PhoneNumberUtils.formatNumber(client.phoneNumber)
        phoneText.text = formattedPhoneNumber

        itemView.setOnClickListener {
            listener.onViewHolderSelected(client, this)
        }
    }
}

interface ClientViewHolderListener {
    fun onViewHolderSelected(client: Client, clientViewHolder: ClientViewHolder)

}