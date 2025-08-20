package com.hastaprimasolusi.rana.adapter.page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.data.network.response.DeliveryCourierModel
import com.hastaprimasolusi.rana.utils.loadImage
import kotlinx.android.synthetic.main.item_courier.view.*
import kotlin.properties.Delegates

/**
 * Created By maasrahman on 5/30/20
 */
class CourierAdapter(private val listener: (DeliveryCourierModel) -> Unit) : RecyclerView.Adapter<CourierAdapter.CourierHolder>() {

    private var listData: List<DeliveryCourierModel> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    fun updateData(history: List<DeliveryCourierModel>) {
        listData = history
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourierHolder =
        CourierHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_courier, parent, false))

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: CourierHolder, position: Int) = holder.bindItem(listData[position], listener)

    inner class CourierHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindItem(model: DeliveryCourierModel, listener: (DeliveryCourierModel) -> Unit){
            itemView.imgLogo.loadImage(model.dELMETHODPROFILE.toString())
            itemView.txtProvider.text = model.dELMETHOD
            itemView.txtNama.text = model.dELMETHODNAME
            itemView.setOnClickListener {
                listener(model)
            }
        }
    }
}