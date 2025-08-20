package com.hastaprimasolusi.rana.adapter.page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.data.network.response.canvas.ListTokoModel
import kotlinx.android.synthetic.main.item_toko.view.*
import kotlin.properties.Delegates

/**
 * Created by maasrahman on 25/02/22.
 */
class ListTokoAdapter(private val listener:(ListTokoModel) -> Unit):
    RecyclerView.Adapter<ListTokoAdapter.ListTokoHolder>() {

    private var listData: List<ListTokoModel> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    fun updateData(list: List<ListTokoModel>){
        listData = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListTokoHolder =
        ListTokoHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_toko, parent, false))

    override fun onBindViewHolder(holder: ListTokoHolder, position: Int) =
        holder.bindItem(listData[position], listener)

    override fun getItemCount(): Int = listData.size

    inner class ListTokoHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindItem(model: ListTokoModel, listener: (ListTokoModel) -> Unit){
            itemView.txtNama.text = "${model.nAME} (${model.cODE})"
            itemView.txtAlamat.text = model.aDDRESS
            itemView.setOnClickListener {
                listener(model)
            }
        }
    }
}