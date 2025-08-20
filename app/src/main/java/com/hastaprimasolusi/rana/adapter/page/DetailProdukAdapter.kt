package com.hastaprimasolusi.rana.adapter.page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.data.network.response.order.HistoryDetailProdModel
import com.hastaprimasolusi.rana.utils.UtilsPref
import com.hastaprimasolusi.rana.utils.convertCurrency
import com.hastaprimasolusi.rana.utils.convertCurrencyNo
import com.hastaprimasolusi.rana.utils.loadImage
import kotlinx.android.synthetic.main.item_detail_produk.view.*
import kotlin.properties.Delegates

/**
 * Created By maasrahman on 2020-04-26
 */
class DetailProdukAdapter :
    RecyclerView.Adapter<DetailProdukAdapter.KeranjangHolder>(){

    private var listData: List<HistoryDetailProdModel> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    fun updateData(produk: List<HistoryDetailProdModel>) {
        listData = produk
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeranjangHolder =
        KeranjangHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_detail_produk, parent, false))

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: KeranjangHolder, position: Int) = holder.bindItem(listData[position])

    inner class KeranjangHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindItem(model: HistoryDetailProdModel){
            itemView.imgProduk.loadImage(model.pRODPICSMALL.toString())
            itemView.txtNamaProduk.text = model.pRODNAME
            itemView.txtHarga.text = convertCurrency(model.oRDERPRODPRICE?.first()?.pRODPRICE.toString(), 3, '.', UtilsPref.loadString("symbolCur"))
            itemView.txtJumlah.text = "${model.oRDERPRODPRICE?.first()?.pRODQTY} ${model.oRDERPRODPRICE?.first()?.pRODUNITNAME}"
            itemView.txtTotalHarga.text = convertCurrency(model.oRDERPRODPRICE?.first()?.pRODTOTALAMT.toString(), 3, '.', UtilsPref.loadString("symbolCur"))
            itemView.txtTersedia.text = "${model.oRDERPRODPRICE?.first()?.pRODQTY} ${model.oRDERPRODPRICE?.first()?.pRODUNITNAME}"
            if(model.oRDERPAYQTY.toString() > model.oRDERPRODQTY.toString()){
                itemView.bgRed.visibility = View.VISIBLE
                itemView.txtProdText.visibility = View.VISIBLE
                itemView.txtProdText.text = model.oRDERPRODTEXT
            }else{
                itemView.bgRed.visibility = View.GONE
                itemView.txtProdText.visibility = View.GONE
            }
        }
    }
}