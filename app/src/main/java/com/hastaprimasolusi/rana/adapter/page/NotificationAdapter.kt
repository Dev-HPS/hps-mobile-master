package com.hastaprimasolusi.rana.adapter.page

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.data.local.MessageModel
import com.hastaprimasolusi.rana.utils.convertDateTime
import kotlinx.android.synthetic.main.item_notification.view.*
import kotlin.properties.Delegates

/**
 * Created By maasrahman on 6/23/20
 */
class NotificationAdapter(private val listener:(MessageModel) -> Unit): RecyclerView.Adapter<NotificationAdapter.NotificationHolder>() {

    private var listData: List<MessageModel> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    fun updateData(messages: List<MessageModel>) {
        listData = messages
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder =
        NotificationHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false))

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) = holder.bindItem(listData[position], listener)

    inner class NotificationHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindItem(model: MessageModel, listener: (MessageModel) -> Unit){
            itemView.txtTanggal.text = convertDateTime(model.createdAt, "dd MMM HH:mm")
            itemView.txtTitle.text = "${model.title} (${model.code})"
            itemView.txtMessage.text = model.message
            itemView.icDot.visibility = if(model.isRead == "0") View.VISIBLE else View.GONE
            itemView.txtTitle.setTypeface(itemView.txtTitle.typeface, if(model.isRead == "0") Typeface.BOLD else Typeface.NORMAL)
            itemView.setOnClickListener {
                listener(model)
            }
        }
    }
}