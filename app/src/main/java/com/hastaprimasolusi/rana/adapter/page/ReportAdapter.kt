package com.hastaprimasolusi.rana.adapter.page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.adapter.holder.LoadMoreHolder
import com.hastaprimasolusi.rana.data.network.response.order.ReportTransModel
import com.hastaprimasolusi.rana.utils.convertCurrency
import com.hastaprimasolusi.rana.utils.convertDateTimeZone
import kotlinx.android.synthetic.main.item_report.view.*
import org.jetbrains.anko.textColor
import kotlin.properties.Delegates

class ReportAdapter(private val role: String): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemLoading = 0

    companion object {
        const val TYPE_DATA = 0
        const val LOAD_MORE = 1
    }

    private var listData: List<ReportTransModel?> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    fun addProgress(){
        itemLoading = listData.size
        val list = mutableListOf<ReportTransModel?>()
        list.addAll(listData)
        list.add(null)
        listData = list
        notifyItemInserted(itemLoading)
    }

    fun removeProgress(){
        listData.dropLast(itemLoading)
        notifyItemRemoved(itemLoading)
    }

    fun updateData(report: List<ReportTransModel>) {
        listData = report
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
        return ReportHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_report, parent, false))
    }

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is LoadMoreHolder){
            holder.bindItem {  }
        }else if(holder is ReportHolder){
            listData[position]?.let { holder.bindItem(role, it) }
        }
    }

    inner class ReportHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindItem(role: String, model: ReportTransModel){
            itemView.txtNoBayar.text = model.tRXCODE
            itemView.txtTanggal.text = convertDateTimeZone(model.tRXDATE.toString(), "dd MM yyyy HH:mm")
            itemView.txtJumlah.text = convertCurrency(model.tRXAMOUNT.toString(), 3, '.', model.tRXCURRENCY.toString())
            itemView.txtTotal.text = convertCurrency(model.tRXTOTAL.toString(), 3, '.', model.tRXCURRENCY.toString())
            if(model.tRXADMIN.isNullOrEmpty()){
                itemView.lblAdmin.visibility = View.GONE
                itemView.txtAdmin.visibility = View.GONE
            }else{
                itemView.lblAdmin.visibility = View.VISIBLE
                itemView.txtAdmin.visibility = View.VISIBLE
                itemView.txtAdmin.text = convertCurrency(model.tRXADMIN.toString(), 3, '.', model.tRXCURRENCY.toString())
            }
            if(role == "canvasser" || role =="spg" || role =="msr"){
                itemView.txtType.visibility = View.VISIBLE
                itemView.txtOutlet.visibility = View.VISIBLE
                itemView.txtType.text = "${model.tRXTYPETEXT} ${if(model.tRXEC?.isNotEmpty() == true) " - ${model.tRXEC}" else ""}"
                itemView.txtOutlet.text = model.tRXOUTLET
            }
            itemView.txtStatusBayar.text = model.tRXSTATUS
            when {
                model.tRXSTATUS?.toLowerCase()?.contains("sukses") == true -> {
                    itemView.txtStatusBayar.textColor = ContextCompat.getColor(itemView.context, R.color.teal)
                }
                model.tRXSTATUS?.toLowerCase()?.contains("belum") == true -> {
                    itemView.txtStatusBayar.textColor = ContextCompat.getColor(itemView.context, R.color.deep_orange)
                }
                model.tRXSTATUS?.toLowerCase()?.contains("pending") == true -> {
                    itemView.txtStatusBayar.textColor = ContextCompat.getColor(itemView.context, R.color.amber)
                }
                else -> {
                    itemView.txtStatusBayar.textColor = ContextCompat.getColor(itemView.context, R.color.blue_grey)
                }
            }

        }
    }
}