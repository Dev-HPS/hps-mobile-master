package com.hastaprimasolusi.rana.adapter.page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.data.network.response.LpModel
import kotlinx.android.synthetic.main.item_lp.view.*
import kotlin.properties.Delegates

/**
 * Created By maasrahman on 7/20/20
 */
class LpAdapter(private val listener: (LpModel) -> Unit): RecyclerView.Adapter<LpAdapter.LpHolder>() {

    private var listData: List<LpModel> by Delegates.observable(emptyList()){_, _, _ ->
        notifyDataSetChanged()
    }

    fun update(data: List<LpModel>){
        listData = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LpHolder =
        LpHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_lp, parent, false))

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: LpHolder, position: Int) = holder.bindItem(listData[position], listener)

    inner class LpHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindItem(model: LpModel, listener:(LpModel) -> Unit){
            itemView.txtNama.text = model.lPNAME
            itemView.txtNamaPemilik.text = model.lPOWNERNAME
            itemView.txtKode.text = model.lPCODE
            itemView.setOnClickListener {
                listener(model)
            }
        }
    }
}