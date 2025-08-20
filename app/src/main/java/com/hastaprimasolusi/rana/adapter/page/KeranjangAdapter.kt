package com.hastaprimasolusi.rana.adapter.page

import android.graphics.Typeface
import android.icu.lang.UProperty.INT_START
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.recyclerview.widget.RecyclerView
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.data.network.response.order.CartProdukModel
import com.hastaprimasolusi.rana.utils.UtilsPref
import com.hastaprimasolusi.rana.utils.convertCurrency
import com.hastaprimasolusi.rana.utils.loadImage
import kotlinx.android.synthetic.main.item_keranjang.view.*
import kotlin.properties.Delegates


/**
 * Created By maasrahman on 2020-04-26
 */
class KeranjangAdapter(private val updateCart:(model: CartProdukModel, jml: String) -> Unit,
                       private val delete:(id: CartProdukModel) -> Unit) :
    RecyclerView.Adapter<KeranjangAdapter.KeranjangHolder>(){

    private var listData: List<CartProdukModel> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    fun updateData(produk: List<CartProdukModel>) {
        listData = produk
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeranjangHolder =
        KeranjangHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_keranjang, parent, false))

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: KeranjangHolder, position: Int) = holder.bindItem(listData[position], updateCart, delete)

    inner class KeranjangHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindItem(model: CartProdukModel, updateCart: (model: CartProdukModel, jml: String) -> Unit,
                     delete: (CartProdukModel) -> Unit){
            itemView.imgProduk.loadImage(model.pRODPICSMALL.toString())
            itemView.etJumlah.setText(model.pRODPRICE?.first()?.pRODQTY.toString())
            val str = SpannableStringBuilder("${model.pRODNAME} (${model.pRODPRICE?.first()?.pRODUNITNAME})")
            str.setSpan(
                StyleSpan(Typeface.BOLD),
                model.pRODNAME?.length ?: 0,
                str.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            itemView.txtNamaProduk.text = str
            itemView.txtHarga.text = convertCurrency(model.pRODPRICE?.first()?.pRODPRICE.toString(), 3, '.', UtilsPref.loadString("symbolCur"))
            itemView.layoutJumlah.setEndIconOnClickListener {
                val jml = (itemView.etJumlah.text.toString().toIntOrNull() ?: 0) + 1
                itemView.etJumlah.setText(jml.toString())
                updateCart(model, jml.toString())
            }
            itemView.layoutJumlah.setStartIconOnClickListener {
                if(itemView.etJumlah.text.toString() == "1"){
                    delete(model)
                }else{
                    val jml = (itemView.etJumlah.text.toString().toIntOrNull() ?: 2) - 1
                    itemView.etJumlah.setText(jml.toString())
                    updateCart(model, jml.toString())
                }
            }
            itemView.imgDelete.setOnClickListener {
                delete(model)
            }
        }
    }
}