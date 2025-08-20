package com.hastaprimasolusi.rana.adapter.page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.data.local.DummyData

/**
 * Created By maasrahman on 2020-05-02
 */
class PesananCanvasAdapter(private val listData: MutableList<DummyData>, private val listener: () -> Unit) :
    RecyclerView.Adapter<PesananCanvasAdapter.PesananCanvasHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PesananCanvasHolder =
        PesananCanvasHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_pesanan_canvas, parent, false))

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: PesananCanvasHolder, position: Int) = holder.bindItem(listData[position], listener)

    inner class PesananCanvasHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindItem(model: DummyData, listener: () -> Unit){

        }
    }
}