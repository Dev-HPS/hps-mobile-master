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
import com.hastaprimasolusi.rana.data.network.response.produk.ProdPriceModel
import com.hastaprimasolusi.rana.utils.UtilsPref
import com.hastaprimasolusi.rana.utils.convertCurrency
import kotlinx.android.synthetic.main.item_price.view.*

/**
 * Created by maasrahman on 06/11/20.
 */
class ProductPriceAdapter(private val listData: List<ProdPriceModel>): RecyclerView.Adapter<ProductPriceAdapter.ProductPriceHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductPriceHolder =
        ProductPriceHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_price, parent, false))

    override fun onBindViewHolder(holder: ProductPriceHolder, position: Int) = holder.bindItem(listData[position])

    override fun getItemCount(): Int = listData.size

    inner class ProductPriceHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindItem(model: ProdPriceModel){
            val spannableString = SpannableString("Stok : ${model.pRODSTOCK} ${model.pRODUNITNAME}")
            spannableString.setSpan(StyleSpan(Typeface.BOLD), 6, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            itemView.txtUnitName.text = model.pRODUNITNAME
            itemView.txtStok.text = spannableString
            itemView.txtHarga.text = convertCurrency(model.pRODPRICE.toString(), 3, '.',
                UtilsPref.loadString(itemView.context.getString(R.string.currencySymbol)))
        }
    }
}