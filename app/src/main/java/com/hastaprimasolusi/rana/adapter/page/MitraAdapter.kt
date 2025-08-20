package com.hastaprimasolusi.rana.adapter.page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.data.local.DummyData

/**
 * Created By maasrahman on 5/8/20
 */
class MitraAdapter(private val listData: MutableList<DummyData>) : RecyclerView.Adapter<MitraAdapter.MitraHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MitraHolder =
        MitraHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_mitra, parent, false))

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: MitraHolder, position: Int) = holder.bindItem(listData[position])

    inner class MitraHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindItem(model: DummyData){

        }
    }
}