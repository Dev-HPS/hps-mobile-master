package com.hastaprimasolusi.rana.adapter.page

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.data.network.response.order.HistoryDetailStatusModel
import com.hastaprimasolusi.rana.data.network.response.order.OrderHistoriesModel
import com.hastaprimasolusi.rana.utils.convertDateTime
import kotlinx.android.synthetic.main.item_status.view.*
import org.jetbrains.anko.backgroundColor
import kotlin.properties.Delegates

/**
 * Created By maasrahman on 5/21/20
 */
class StatusPesananLpAdapter: RecyclerView.Adapter<StatusPesananLpAdapter.StatusPesananHolder>() {

    private var listData: List<OrderHistoriesModel> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    fun updateData(status: List<OrderHistoriesModel>) {
        listData = status
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusPesananHolder =
        StatusPesananHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_status, parent, false))

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: StatusPesananHolder, position: Int) = holder.bindItem(listData[position], position)

    inner class StatusPesananHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindItem(model: OrderHistoriesModel, pos: Int){
            if(pos == 0){
                itemView.imgDot.setColorFilter(Color.parseColor("#009688"))
                itemView.viewLine.backgroundColor = Color.parseColor("#009688")
                itemView.txtTanggal.setTextColor(Color.parseColor("#009688"))
            }else{
                itemView.txtTanggal.setTextColor(Color.parseColor("#686868"))
                itemView.imgDot.setColorFilter(Color.parseColor("#d8d8d8"))
                itemView.viewLine.backgroundColor = Color.parseColor("#d8d8d8")
            }
            itemView.viewLine.visibility = if(pos == (listData.size - 1)) View.GONE else View.VISIBLE
            itemView.txtStatus.text = model.sTATUSTEXT
            itemView.txtTanggal.text = convertDateTime(model.sTATUSDATE.toString(), "dd MMM yyy HH:mm")
        }
    }
}