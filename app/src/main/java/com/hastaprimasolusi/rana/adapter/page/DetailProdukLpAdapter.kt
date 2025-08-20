package com.hastaprimasolusi.rana.adapter.page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.data.network.response.order.HistoryDetailProdModel
import com.hastaprimasolusi.rana.data.network.response.order.OrderProdModel
import com.hastaprimasolusi.rana.utils.UtilsPref
import com.hastaprimasolusi.rana.utils.convertCurrency
import com.hastaprimasolusi.rana.utils.convertCurrencyNo
import com.hastaprimasolusi.rana.utils.loadImage
import kotlinx.android.synthetic.main.item_detail_produk_lp.view.*
import kotlin.properties.Delegates

/**
 * Created By maasrahman on 2020-04-26
 */
class DetailProdukLpAdapter(private val orderStatus: Int) :
    RecyclerView.Adapter<DetailProdukLpAdapter.KeranjangHolder>(){

    private var listData: List<OrderProdModel> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    fun updateData(produk: List<OrderProdModel>) {
        listData = produk
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeranjangHolder =
        KeranjangHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_detail_produk_lp, parent, false))

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: KeranjangHolder, position: Int) = holder.bindItem(listData[position], orderStatus)

    inner class KeranjangHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindItem(model: OrderProdModel, orderStatus: Int){
            itemView.imgProduk.loadImage(model.pRODPICSMALL.toString())
            itemView.txtNamaProduk.text = model.pRODNAME
            itemView.txtHarga.text = convertCurrency(model.oRDERPRODPRICE.toString(), 3, '.', UtilsPref.loadString("symbolCur"))
            itemView.txtJumlah.text = "${model.oRDERPRODQTY} ${model.oRDERPRODUNITNAME}"
            itemView.txtTotalHarga.text = convertCurrency(if(model.oRDERPAYAMT.toString() == "0") model.oRDERPRODAMT.toString() else
                model.oRDERPAYAMT.toString(), 3, '.', UtilsPref.loadString("symbolCur"))
            itemView.txtJumlahStok.text = "${model.oRDERPRODSTOCK} ${model.oRDERPRODUNITNAME}"
            if(model.oRDERPRODSTOCK.toString().toIntOrNull() ?: 0 < model.oRDERPRODQTY.toString().toIntOrNull() ?: 0){
                itemView.bgRed.visibility = View.VISIBLE
                itemView.txtProdText.visibility = View.VISIBLE
                itemView.txtProdText.text = model.oRDERPRODTEXT
            }else{
                itemView.bgRed.visibility = View.GONE
                itemView.txtProdText.visibility = View.GONE
            }
            if(orderStatus == 1 || orderStatus == 10){
                itemView.check.visibility = View.VISIBLE
                itemView.check.setOnCheckedChangeListener { _, isChecked ->
                    model.isChecked = isChecked
                }
            }else{
                itemView.check.visibility = View.GONE
            }
        }
    }
}