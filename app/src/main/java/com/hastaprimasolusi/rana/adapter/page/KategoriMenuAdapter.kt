package com.hastaprimasolusi.rana.adapter.page

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.data.local.KategoriMenuModel
import com.hastaprimasolusi.rana.data.network.response.produk.CategoryModel
import kotlinx.android.synthetic.main.item_menu_kategori.view.*
import kotlin.properties.Delegates

/**
 * Created By maasrahman on 2020-04-26
 */
class KategoriMenuAdapter(private val listener:(CategoryModel) -> Unit) :
    RecyclerView.Adapter<KategoriMenuAdapter.KategoriMenuHolder>(){

    private var listData: List<CategoryModel> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    fun updateData(category: List<CategoryModel>) {
        listData = category
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KategoriMenuHolder =
        KategoriMenuHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_menu_kategori, parent, false))

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: KategoriMenuHolder, position: Int) = holder.bindItem(listData[position], listener)

    inner class KategoriMenuHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindItem(model: CategoryModel, listener: (CategoryModel) -> Unit){
            itemView.txtKategori.text = model.cATEGORYNAME
            if(model.cATEGORYNAME == "Semua"){
                Glide.with(itemView)
                    .load(R.drawable.ic_loadmore)
                    .into(itemView.imgKategori)
                itemView.imgKategori.setColorFilter(Color.parseColor("#009688"))
            }else{
                Glide.with(itemView)
                    .load(model.cATEGORYICON)
                    .apply(RequestOptions().error(R.drawable.no_image))
                    .into(itemView.imgKategori)
            }
            itemView.setOnClickListener {
                listener(model)
            }
        }
    }
}