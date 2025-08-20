package com.hastaprimasolusi.rana.adapter.page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.adapter.holder.LoadMoreHolder
import com.hastaprimasolusi.rana.data.network.response.produk.ProdukModel
import com.hastaprimasolusi.rana.utils.convertCurrency
import com.hastaprimasolusi.rana.utils.loadImage
import kotlinx.android.synthetic.main.item_menu_produk.view.*
import kotlin.properties.Delegates

/**
 * Created By maasrahman on 2020-04-26
 */
class ProdukMenuAdapter(private val listener: (ProdukModel?) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var itemLoading = 0

    companion object {
        const val TYPE_DATA = 0
        const val LOAD_MORE = 1
    }

    private var listData: List<ProdukModel?> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    fun addProgress(){
        itemLoading = listData.size
        val list = mutableListOf<ProdukModel?>()
        list.addAll(listData)
        list.add(null)
        listData = list
        notifyItemInserted(itemLoading)
    }

    fun removeProgress(){
        listData.dropLast(itemLoading)
        notifyItemRemoved(itemLoading)
    }

    fun updateData(produk: List<ProdukModel>) {
        listData = produk
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
        if(viewType == LOAD_MORE){
            return LoadMoreHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_load_more, parent, false))
        }
        return ProdukMenuHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_menu_produk, parent, false))
    }

    override fun getItemViewType(position: Int): Int {
        if(listData[position] == null){
            return LOAD_MORE
        }
        return TYPE_DATA
    }

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int){
        if(holder is LoadMoreHolder){
            holder.bindItem {  }
        }else if(holder is ProdukMenuHolder){
            holder.bindItem(listData[position], listener)
        }
    }

    inner class ProdukMenuHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindItem(model: ProdukModel?, listener:(ProdukModel?) -> Unit){
            itemView.imgProduk.loadImage(model?.pRODPICMEDIUM.toString())
//            itemView.txtStok.visibility = View.VISIBLE
            itemView.txtStok.text = "Stok ${model?.pRODSTOCK} ${model?.pRODUNITNAME}"
            itemView.txtNama.text = model?.pRODNAME
            itemView.txtHarga.text = convertCurrency(model?.pRODPRICE?.first()?.pRODPRICE.toString(), 3, '.', model?.pRODCURRENCY.toString())
            itemView.setOnClickListener {
                listener(model)
            }
        }
    }
}