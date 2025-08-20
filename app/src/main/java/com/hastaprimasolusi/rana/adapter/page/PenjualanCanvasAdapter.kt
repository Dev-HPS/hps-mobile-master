package com.hastaprimasolusi.rana.adapter.page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.data.local.DummyData
import kotlinx.android.synthetic.main.item_canvas_penjualan.view.*

/**
 * Created By maasrahman on 5/6/20
 */
class PenjualanCanvasAdapter(private val listData: MutableList<DummyData>) : RecyclerView.Adapter<PenjualanCanvasAdapter.PenjualanCanvasHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PenjualanCanvasHolder =
        PenjualanCanvasHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_canvas_penjualan, parent, false))

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: PenjualanCanvasHolder, position: Int) = holder.bindItem(listData[position])

    inner class PenjualanCanvasHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindItem(model: DummyData){
            itemView.layoutJumlah.setEndIconOnClickListener {
                val jml = itemView.etJumlah.text.toString().toInt() + 1
                itemView.etJumlah.setText(jml.toString())
            }
            itemView.layoutJumlah.setStartIconOnClickListener {
                if(itemView.etJumlah.text.toString() == "1"){

                }else{
                    val jml = itemView.etJumlah.text.toString().toInt() - 1
                    itemView.etJumlah.setText(jml.toString())
                }
            }
        }
    }
}