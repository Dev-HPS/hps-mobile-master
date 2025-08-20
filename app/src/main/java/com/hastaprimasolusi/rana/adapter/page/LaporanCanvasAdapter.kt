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
class LaporanCanvasAdapter(private val listData: MutableList<DummyData>) : RecyclerView.Adapter<LaporanCanvasAdapter.LaporanCanvasHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaporanCanvasHolder =
        LaporanCanvasHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_laporan_canvas, parent, false))

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: LaporanCanvasHolder, position: Int) = holder.bindItem(listData[position])

    inner class LaporanCanvasHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindItem(model: DummyData){

        }
    }
}