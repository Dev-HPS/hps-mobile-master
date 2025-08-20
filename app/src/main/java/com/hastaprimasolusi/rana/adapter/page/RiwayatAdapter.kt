package com.hastaprimasolusi.rana.adapter.page

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.adapter.holder.LoadMoreHolder
import com.hastaprimasolusi.rana.data.network.response.order.HistoryOrderModel
import com.hastaprimasolusi.rana.data.network.response.produk.ProdukModel
import com.hastaprimasolusi.rana.utils.*
import kotlinx.android.synthetic.main.item_riwayat.view.*
import kotlin.properties.Delegates

/**
 * Created By maasrahman on 2020-04-26
 */
class RiwayatAdapter(private val listener: (HistoryOrderModel?) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var itemLoading = 0

    companion object {
        const val TYPE_DATA = 0
        const val LOAD_MORE = 1
    }

    private var listData: List<HistoryOrderModel?> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    fun addProgress(){
        itemLoading = listData.size
        val list = mutableListOf<HistoryOrderModel?>()
        list.addAll(listData)
        list.add(null)
        listData = list
        notifyItemInserted(itemLoading)
    }

    fun removeProgress(){
        listData.dropLast(itemLoading)
        notifyItemRemoved(itemLoading)
    }

    fun updateData(history: List<HistoryOrderModel>) {
        listData = history
    }

    override fun getItemViewType(position: Int): Int {
        if(listData[position] == null){
            return LOAD_MORE
        }
        return TYPE_DATA
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == LOAD_MORE){
            return LoadMoreHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_load_more, parent, false))
        }
        return RiwayatHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_riwayat, parent, false))
    }

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is RiwayatHolder){
            holder.bindItem(listData[position], listener)
        }else if(holder is LoadMoreHolder){
            holder.bindItem {  }
        }
    }

    inner class RiwayatHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindItem(model: HistoryOrderModel?, listener: (HistoryOrderModel?) -> Unit){
            itemView.txtStatus.text = model?.oRDERSTATUSTEXT

            val shape = GradientDrawable()
            shape.cornerRadius = 8f
            if(model?.oRDERSTATUSCOLOR.isNullOrEmpty()){
                shape.setColor(Color.parseColor("#b8b8b8"))
            } else {
                shape.setColor(Color.parseColor(model?.oRDERSTATUSCOLOR))
            }
            itemView.txtStatus.background = shape

            itemView.txtTgl.text = convertDateTime(model?.oRDERDATE.toString(), "dd MMM yyyy")
            itemView.txtInvoice.text = model?.oRDERNO
            itemView.imgProduk.loadImage(model?.oRDERPROD1PICSMALL.toString())
            itemView.txtNamaProduk.text = model?.oRDERPROD1NAME
            itemView.txtQtyProduk.text = "Qty ${model?.oRDERPROD1QTY}"
            if(model?.oRDERPROD2QTY.toString() == "0"){
                itemView.txtProdukLain.visibility = View.GONE
            }else{
                itemView.txtProdukLain.visibility = View.VISIBLE
                itemView.txtProdukLain.text = "+${model?.oRDERPROD2QTY} Item Lainnya"
            }
            if(model?.oRDERTOTALPAYAMT.toString() != "0"){
                itemView.txtHarga.text = "${convertCurrency(model?.oRDERTOTALPAYAMT.toString(), 3, '.', UtilsPref.loadString("symbolCur"))}"
            }else{
                itemView.txtHarga.text = "${convertCurrency(model?.oRDERTOTALAMT.toString(), 3, '.', UtilsPref.loadString("symbolCur"))}"
            }
            UtilsPref.saveString("symbolCur", model?.oRDERCURRENCY.toString())
            itemView.setOnClickListener {
                listener(model)
            }
        }
    }
}