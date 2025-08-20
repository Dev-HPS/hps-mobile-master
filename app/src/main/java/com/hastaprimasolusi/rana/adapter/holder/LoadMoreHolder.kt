package com.hastaprimasolusi.rana.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_load_more.view.*

class LoadMoreHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindItem(listener:() -> Unit){
        itemView.btnLoad.setOnClickListener {
            listener()
            itemView.progress.visibility = View.VISIBLE
            itemView.btnLoad.visibility = View.GONE
        }
    }
}