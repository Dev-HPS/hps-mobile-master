package com.hastaprimasolusi.rana.adapter.page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.data.local.DummyData

/**
 * Created By maasrahman on 5/7/20
 */
class CanvasserAdapter(private val listData: MutableList<DummyData>) : RecyclerView.Adapter<CanvasserAdapter.CanvasHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CanvasHolder =
        CanvasHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_canvas, parent, false))

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: CanvasHolder, position: Int) = holder.bindItem(listData[position])

    inner class CanvasHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindItem(model: DummyData){

        }
    }
}