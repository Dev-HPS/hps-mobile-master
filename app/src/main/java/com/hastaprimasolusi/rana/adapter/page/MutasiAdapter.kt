package com.hastaprimasolusi.rana.adapter.page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.adapter.holder.LoadMoreHolder
import com.hastaprimasolusi.rana.data.network.response.report.MutasiDetailModel
import com.hastaprimasolusi.rana.utils.UtilsPref
import com.hastaprimasolusi.rana.utils.convertCurrency
import com.hastaprimasolusi.rana.utils.convertDateTimeZone
import kotlinx.android.synthetic.main.item_mutasi.view.*
import kotlin.properties.Delegates

/**
 * Created by maasrahman on 16/09/20.
 */
class MutasiAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemLoading = 0

    companion object {
        const val TYPE_DATA = 0
        const val LOAD_MORE = 1
    }

    private var listData: List<MutasiDetailModel?> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    fun addProgress(){
        itemLoading = listData.size
        val list = mutableListOf<MutasiDetailModel?>()
        list.addAll(listData)
        list.add(null)
        listData = list
        notifyItemInserted(itemLoading)
    }

    fun removeProgress(){
        listData.dropLast(itemLoading)
        notifyItemRemoved(itemLoading)
    }

    fun updateData(report: List<MutasiDetailModel>) {
        listData = report
    }

    override fun getItemViewType(position: Int): Int {
        if(listData[position] == null){
            return LOAD_MORE
        }
        return TYPE_DATA
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == ReportAdapter.LOAD_MORE){
            return LoadMoreHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_load_more, parent, false))
        }
        return MutasiHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_mutasi, parent, false))
    }

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is LoadMoreHolder){
            holder.bindItem {  }
        }else if(holder is MutasiHolder){
            listData[position]?.let { holder.bindItem(it) }
        }
    }

    inner class MutasiHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindItem(model: MutasiDetailModel){
            val symbol = UtilsPref.loadString(itemView.context.getString(R.string.currencySymbol))
            itemView.txtKode.text = model.mUTTRXCODE
            itemView.txtTgl.text = convertDateTimeZone(model.mUTTRXDATE.toString(), "dd MMM yyyy HH:mm")
            itemView.txtDesc.text = model.mUTTRXDESC
            if(model.mUTDEBIT.isNullOrEmpty() || model.mUTDEBIT.toString() == "0"){
                itemView.txtNominal.text = "(+) ${convertCurrency(model.mUTKREDIT.toString(), 3, '.', 
                    UtilsPref.loadString(itemView.context.getString(R.string.currencySymbol)))}"
            }else{
                itemView.txtNominal.text = "(-) ${convertCurrency(model.mUTDEBIT.toString(), 3, '.', symbol)}"
            }
            itemView.txtSaldo.text = convertCurrency(model.mUTSALDO.toString(), 3, '.', symbol)
        }
    }
}