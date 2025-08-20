package com.hastaprimasolusi.rana.adapter.page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.adapter.holder.LoadMoreHolder
import com.hastaprimasolusi.rana.data.network.response.produk.ProdukModel
import com.hastaprimasolusi.rana.utils.UtilsPref
import com.hastaprimasolusi.rana.utils.convertCurrency
import kotlinx.android.synthetic.main.item_produk_cnvs.view.*
import kotlin.properties.Delegates

/**
 * Created By maasrahman on 6/3/20
 */
class ProdukCnvsAdapter(private val listener: (ProdukModel?) -> Unit) :
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

    fun updateData(prod: List<ProdukModel>) {
        listData = prod
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == LOAD_MORE){
            LoadMoreHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_load_more, parent, false))
        }
        return ProdukCnvsHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_produk_cnvs, parent, false))
    }

    override fun getItemCount(): Int = listData.size

    override fun getItemViewType(position: Int): Int {
        if(listData[position] == null){
            return LOAD_MORE
        }
        return TYPE_DATA
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int){
        if(holder is ProdukCnvsHolder){
            holder.bindItem(listData[position], listener)
        }else if(holder is LoadMoreHolder){
            holder.bindItem {  }
        }
    }

    inner class ProdukCnvsHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindItem(model: ProdukModel?, listener:(ProdukModel?) -> Unit) {
            itemView.etJumlah.setText(model?.jumlah.toString())
            itemView.lblJumlah.visibility = if(model?.jumlah ?: 0 > 0) View.VISIBLE else View.GONE
            itemView.etJumlah.visibility = if(model?.jumlah ?: 0 > 0) View.VISIBLE else View.GONE
//            itemView.btnJmlPesan.text = if(model.jumlah > 0) "Ubah" else "Tambah"
            itemView.txtNamaProduk.text = model?.pRODNAME
//            itemView.txtStok.text = "Stok ${model?.pRODPRICE?.first()?.pRODSTOCK}"
            itemView.txtStok.text = model?.pRODDESCRIPTION
//            itemView.txtHarga.text = convertCurrency(model?.pRODPRICE?.first()?.pRODPRICE.toString(), 3, '.', UtilsPref.loadString("symbolCur"))
            itemView.btnJmlPesan.setOnClickListener {
                listener(model)
            }
            itemView.setOnClickListener {
                listener(model)
            }
        }
    }
}