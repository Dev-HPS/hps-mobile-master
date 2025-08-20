package com.hastaprimasolusi.rana.adapter.page

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.data.local.DummyData
import kotlinx.android.synthetic.main.item_menu_produk.view.*

/**
 * Created By maasrahman on 2020-04-26
 */
class ProdukDiskonAdapter(private val listData: MutableList<DummyData>, private val listener: () -> Unit) :
    RecyclerView.Adapter<ProdukDiskonAdapter.ProdukMenuHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdukMenuHolder =
        ProdukMenuHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_diskon_produk, parent, false))

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: ProdukMenuHolder, position: Int) = holder.bindItem(listData[position], listener)

    inner class ProdukMenuHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindItem(model: DummyData, listener:() -> Unit){
            Glide.with(itemView)
                .load(R.drawable.contoh_produk)
                .into(itemView.imgProduk)
            itemView.txtHargaPot.visibility = View.VISIBLE
            itemView.txtHargaPot.text = "Rp 20.000"
            itemView.txtHargaPot.paintFlags = itemView.txtHargaPot.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            itemView.setOnClickListener {
                listener()
            }
        }
    }
}