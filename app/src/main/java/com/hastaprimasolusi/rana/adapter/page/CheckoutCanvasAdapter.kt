package com.hastaprimasolusi.rana.adapter.page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.data.local.DummyData

/**
 * Created By maasrahman on 5/6/20
 */
class CheckoutCanvasAdapter(private val listData: MutableList<DummyData>) : RecyclerView.Adapter<CheckoutCanvasAdapter.CheckoutCanvasHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutCanvasHolder =
        CheckoutCanvasHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_canvas_checkout, parent, false))

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: CheckoutCanvasHolder, position: Int) = holder.bindItem(listData[position])

    inner class CheckoutCanvasHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindItem(model: DummyData){

        }
    }
}