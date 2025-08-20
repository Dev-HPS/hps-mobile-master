package com.hastaprimasolusi.rana.adapter.page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.data.network.response.canvas.RiwayatCnvsProdModel
import com.hastaprimasolusi.rana.utils.UtilsPref
import com.hastaprimasolusi.rana.utils.convertCurrency
import com.hastaprimasolusi.rana.utils.convertCurrencyNo
import com.hastaprimasolusi.rana.utils.loadImage
import kotlinx.android.synthetic.main.item_detail_produk_cnvs.view.*
import kotlin.properties.Delegates

/**
 * Created By maasrahman on 2020-04-26
 */
class DetailProdukCnvsAdapter :
    RecyclerView.Adapter<DetailProdukCnvsAdapter.KeranjangHolder>(){

    private var listData: List<RiwayatCnvsProdModel> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    fun updateData(produk: List<RiwayatCnvsProdModel>) {
        listData = produk
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeranjangHolder =
        KeranjangHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_detail_produk_cnvs, parent, false))

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: KeranjangHolder, position: Int) = holder.bindItem(listData[position])

    inner class KeranjangHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindItem(model: RiwayatCnvsProdModel){
            itemView.imgProduk.loadImage(model.iMAGE.toString())
            itemView.txtNamaProduk.text = model.tITLE
            itemView.txtHarga.text = convertCurrency(model.pRICE.toString(), 3, '.', UtilsPref.loadString("symbolCur"))
            itemView.txtJumlah.text = "${model.qTY} ${model.uNIT}"
            itemView.txtTotalHarga.text = convertCurrency(model.sUBTOTALAMT.toString(), 3, '.', UtilsPref.loadString("symbolCur"))
        }
    }
}