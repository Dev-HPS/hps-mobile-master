package com.hastaprimasolusi.rana.adapter.page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.data.local.DummyData
import com.hastaprimasolusi.rana.data.network.response.produk.CategoryModel
import kotlinx.android.synthetic.main.item_kategori.view.*
import kotlin.properties.Delegates

/**
 * Created By maasrahman on 2020-04-26
 */
class KategoriAdapter(private val listener: (CategoryModel) -> Unit) :
    RecyclerView.Adapter<KategoriAdapter.KategoriHolder>(){

    private var listData: List<CategoryModel> by Delegates.observable(emptyList()){_, _, _ ->
        notifyDataSetChanged()
    }

    fun updateData(list: List<CategoryModel>){
        listData = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KategoriHolder =
        KategoriHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_kategori, parent, false))

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: KategoriHolder, position: Int) = holder.bindItem(listData[position], listener)

    inner class KategoriHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindItem(model: CategoryModel, listener:(CategoryModel) -> Unit){
            itemView.txtKategori.text = model.cATEGORYNAME
            itemView.setOnClickListener {
                listener(model)
            }
        }
    }
}