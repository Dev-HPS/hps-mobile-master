package com.hastaprimasolusi.rana.adapter.page

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.adapter.holder.LoadMoreHolder
import com.hastaprimasolusi.rana.data.network.response.canvas.RiwayatCanvasModel
import com.hastaprimasolusi.rana.data.network.response.order.HistoryOrderModel
import com.hastaprimasolusi.rana.utils.UtilsPref
import com.hastaprimasolusi.rana.utils.convertCurrency
import com.hastaprimasolusi.rana.utils.convertCurrencyNo
import com.hastaprimasolusi.rana.utils.loadImage
import kotlinx.android.synthetic.main.item_riwayat_cnvs.view.*
import kotlin.properties.Delegates


/**
 * Created By maasrahman on 2020-04-26
 */
class RiwayatCnvsAdapter(private val listener: (RiwayatCanvasModel?) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var itemLoading = 0

    companion object {
        const val TYPE_DATA = 0
        const val LOAD_MORE = 1
    }

    private var listData: List<RiwayatCanvasModel?> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    fun addProgress(){
        itemLoading = listData.size
        val list = mutableListOf<RiwayatCanvasModel?>()
        list.addAll(listData)
        list.add(null)
        listData = list
        notifyItemInserted(itemLoading)
    }

    fun removeProgress(){
        listData.dropLast(itemLoading)
        notifyItemRemoved(itemLoading)
    }

    fun updateData(history: List<RiwayatCanvasModel>) {
        listData = history
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
        if(viewType == LOAD_MORE){
            return LoadMoreHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_load_more, parent, false))
        }
        return RiwayatHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_riwayat_cnvs, parent, false))
    }

    override fun getItemViewType(position: Int): Int {
        if(listData[position] == null){
           return LOAD_MORE
        }
        return TYPE_DATA
    }

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is LoadMoreHolder){
            holder.bindItem {  }
        }else if(holder is RiwayatHolder){
            holder.bindItem(listData[position], listener)
        }
    }

    inner class RiwayatHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindItem(model: RiwayatCanvasModel?, listener: (RiwayatCanvasModel?) -> Unit){
            itemView.txtStatus.text = model?.sTATUSTEXT

            val shape = GradientDrawable()
            shape.cornerRadius = 8f
            if(model?.sTATUSCOLOR.isNullOrEmpty()){
                shape.setColor(Color.parseColor("#b8b8b8"))
            } else {
                shape.setColor(Color.parseColor(model?.sTATUSCOLOR))
            }
            itemView.txtStatus.background = shape

            itemView.txtTgl.text = model?.dATETIMEHUMAN
            itemView.txtInvoice.text = model?.cODE
            itemView.imgProduk.loadImage(model?.iMAGE.toString())
            itemView.txtNamaProduk.text = model?.tITLE
            itemView.txtQtyProduk.text = "Qty ${model?.qTY1}"
            if(model?.qTY2.isNullOrEmpty() || model?.qTY2.toString() == "0"){
                itemView.txtProdukLain.visibility = View.GONE
            }else{
                itemView.txtProdukLain.visibility = View.VISIBLE
                itemView.txtProdukLain.text = "+${model?.qTY2} Item Lainnya"
            }
            itemView.txtHarga.text = "${convertCurrency(model?.tOTALAMT.toString(), 3, '.', UtilsPref.loadString("symbolCur"))}"
            itemView.setOnClickListener {
                listener(model)
            }
        }
    }
}