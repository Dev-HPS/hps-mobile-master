package com.hastaprimasolusi.rana.adapter.page

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.adapter.holder.LoadMoreHolder
import com.hastaprimasolusi.rana.data.network.response.produk.ProdukModel
import com.hastaprimasolusi.rana.utils.convertCurrency
import com.hastaprimasolusi.rana.utils.loadImage
import kotlinx.android.synthetic.main.item_menu_produk.view.*

/**
 * Created By maasrahman on 2020-04-26
 */
class ProdukLoadMoreAdapter(private val listData: MutableList<ProdukModel?>, private val listener:(ProdukModel?) -> Unit, private val loadMore:() -> Unit)
    :RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    companion object {
        const val TYPE_DATA = 0
        const val LOAD_MORE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == TYPE_DATA){
            return ProdukHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_menu_produk, parent, false))
        }
        return LoadMoreHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_load_more, parent, false))
    }

    override fun getItemViewType(position: Int): Int {
        if(listData[position] == null) {
            return LOAD_MORE
        }
        return TYPE_DATA
    }

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ProdukHolder){
            holder.bindItem(listData[position], listener)
        }else if(holder is LoadMoreHolder){
            holder.bindItem(loadMore)
        }
    }

    inner class ProdukHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindItem(model: ProdukModel?, listener:(ProdukModel?) -> Unit){
            itemView.imgProduk.loadImage(model?.pRODPICMEDIUM.toString())
//            itemView.txtStok.text = "Stok ${model?.pRODSTOCK} ${model?.pRODUNITNAME}"
            val spannableString = SpannableString("${model?.pRODNAME} ${model?.pRODPRICE?.first()?.pRODUNITNAME}")
            spannableString.setSpan(StyleSpan(Typeface.BOLD), model?.pRODNAME?.length ?: 0, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            itemView.txtNama.text = model?.pRODNAME
            itemView.txtHarga.text = "${convertCurrency(model?.pRODPRICE?.first()?.pRODPRICE.toString(), 3, '.', model?.pRODCURRENCY.toString())}"
            itemView.setOnClickListener {
                listener(model)
            }
        }
    }
}