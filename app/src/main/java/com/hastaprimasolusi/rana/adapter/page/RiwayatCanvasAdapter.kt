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
class RiwayatCanvasAdapter(private val listData: MutableList<DummyData>) : RecyclerView.Adapter<RiwayatCanvasAdapter.RiwayatCanvasHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiwayatCanvasHolder =
        RiwayatCanvasHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_riwayat_canvas, parent, false))

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: RiwayatCanvasHolder, position: Int) = holder.bindItem(listData[position])

    inner class RiwayatCanvasHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindItem(model: DummyData){

        }
    }
}