package com.hastaprimasolusi.rana.adapter.page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.data.local.DummyData
import com.hastaprimasolusi.rana.data.local.SummaryModel
import com.hastaprimasolusi.rana.utils.converNumber
import com.hastaprimasolusi.rana.utils.convertCurrency
import kotlinx.android.synthetic.main.item_summary.view.*

/**
 * Created By maasrahman on 5/6/20
 */
class SummaryAdapter(private val listData: MutableList<SummaryModel>) : RecyclerView.Adapter<SummaryAdapter.SummaryHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryHolder =
        SummaryHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_summary, parent, false))

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: SummaryHolder, position: Int) = holder.bindItem(listData[position])

    inner class SummaryHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindItem(model: SummaryModel){
            itemView.txtJenis.text = model.jenis
            itemView.txtJumlah.text = converNumber(model.jmlTransaksi.toString(), 3, '.')
        }
    }
}