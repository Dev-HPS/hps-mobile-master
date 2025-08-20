package com.hastaprimasolusi.rana.ui.produk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.material.chip.Chip
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.data.network.response.order.CartProdukModel
import com.hastaprimasolusi.rana.data.network.response.produk.ProdPriceModel
import com.hastaprimasolusi.rana.data.network.response.produk.ProdukModel
import com.hastaprimasolusi.rana.ui.common.ProgDialog
import com.hastaprimasolusi.rana.ui.mitra.MitraViewModel
import com.hastaprimasolusi.rana.utils.*
import kotlinx.android.synthetic.main.fragment_produk_detail.*
import kotlinx.android.synthetic.main.fragment_produk_detail.chipGroup
import kotlinx.android.synthetic.main.fragment_produk_detail.txtHarga
import org.koin.android.ext.android.inject

/**
 * Created By maasrahman on 2020-04-26
 */
class ProdukDetailFragment: Fragment() {
    private val viewModel: MitraViewModel by inject()
    private var model: ProdukModel? = null
    private var progress = ProgDialog().getInstance()
    private var stokQty = 0
    private var selectedPrice = MutableLiveData<ProdPriceModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.detailproduk)
        return inflater.inflate(R.layout.fragment_produk_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val bundle = arguments
        model = bundle?.getParcelable("model")
        initData()
        btnBeli.setOnClickListener {
            if(selectedPrice.value == null){
                showAlert(activity!!, getString(R.string.jenispembelianbelum))
                return@setOnClickListener
            }
            var min = selectedPrice.value?.pRODUNITMIN?.toInt() ?: 0
            var max = selectedPrice.value?.pRODUNITMAX?.toInt() ?: 0
            if(min > etJumlah2.text.toString().toIntOrNull() ?: 0){
                showAlert(activity!!, "${selectedPrice.value?.pRODUNITNAME} minimum pembelian $min")
                return@setOnClickListener
            }
            if(max > 0 && etJumlah2.text.toString().toIntOrNull() ?: 0 > max){
                showAlert(activity!!, "${selectedPrice.value?.pRODUNITNAME} maksimum pembelian $max")
                return@setOnClickListener
            }
            actionCart()
        }
        viewModel.produkDetailModel.postValue(null)
        txtStok.text = "Stok"
        viewModel.getProdukDetail(model?.pRODID.toString())
        viewModel.produkDetailModel.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            txtStok.text = "Stok ${it.pRODPRICE?.first()?.pRODSTOCK} ${it.pRODPRICE?.first()?.pRODUNITNAME}"
        })
        etJumlah2.setSelection(etJumlah2.text.toString().length)
    }

    private fun actionCart(){
        viewModel.actionCart(model?.pRODID.toString(), etJumlah2.text.toString(), selectedPrice.value?.pRODUNIT.toString()) { type, response ->
            when(response.rC){
                "0000" -> {
                    val prod = CartProdukModel()
                    prod.pRODCATEGORY = model?.pRODCATEGORY
                    prod.pRODCODE = model?.pRODCODE
                    prod.pRODDISCOUNT = model?.pRODDISCOUNT
                    prod.pRODID = model?.pRODID
                    prod.pRODNAME = model?.pRODNAME

                    //DEVEL PRICE
                    var priceList = mutableListOf<ProdPriceModel>()
                    selectedPrice.value?.let { price ->
                        price.pRODQTY = etJumlah2.text.toString()
                        priceList.add(price)
                    }
                    prod.pRODPRICE = priceList
                    prod.pRODPRICELIST = model?.pRODPRICELIST
                    prod.pRODPICSMALL = model?.pRODPICSMALL
                    prod.pRODPICMEDIUM = model?.pRODPICMEDIUM
//                    prod.pRODQTY =
                    viewModel.addCart(type, prod)
                    activity?.finish()
                }
                "0001" -> {
                    viewModel.isUnAuthorized.postValue(true)
                }
                else -> {
                    showAlert(activity!!, response.rCM.toString())
                }
            }
        }
    }

    private fun initData(){
        if(chipGroup.childCount <= 0){
            model?.pRODPRICE?.forEach { row ->
                val chip = activity?.layoutInflater?.inflate(R.layout.item_chip, null, false) as Chip
                chip.text = row.pRODUNITNAME
                chip.setOnCheckedChangeListener { _, isChecked ->
                    if(isChecked){
                        selectedPrice.postValue(row)
                    }
                }
                chipGroup.addView(chip)
            }
        }

        imgProduk.loadImage(model?.pRODPICMEDIUM.toString())

        txtNamaProduk.text = "${model?.pRODNAME} (${model?.pRODPRICE?.first()?.pRODUNITNAME})"
        txtKategori.text = model?.pRODCATEGORYNAME
        txtDeskripsi.text = model?.pRODDESCRIPTION

        txtHarga.text = convertCurrency(model?.pRODPRICE?.first()?.pRODPRICE.toString(), 3, '.',
            UtilsPref.loadString(getString(R.string.currencySymbol)))
        txtStok.text = "Stok ${model?.pRODPRICE?.first()?.pRODSTOCK.toString()} ${model?.pRODPRICE?.first()?.pRODUNITNAME.toString()}"

        viewModel.showError.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            showalertInformation(activity!!, it){
                viewModel.showError.postValue(null)
            }
        })

        selectedPrice.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            txtHarga.text = convertCurrency(it.pRODPRICE.toString(), 3, '.',
                UtilsPref.loadString(getString(R.string.currencySymbol)))
            txtStok.text = "Stok ${it.pRODSTOCK} ${it.pRODUNITNAME}"
            txtNamaProduk.text = "${model?.pRODNAME} (${it?.pRODUNITNAME})"
            etJumlah2.setText(it.pRODUNITMIN)
            etJumlah2.setSelection(it.pRODUNITMIN?.length ?: 0)
            //Update Stok with Cart
//            val listCart = viewModel.listCart.value
//            val jmlBeli = listCart?.find { it.pRODID == model?.pRODID }?.pRODQTY
//            model?.pRODSTOCK?.let { stok ->
//                stokQty = stok.toInt().minus(jmlBeli?.toIntOrNull() ?: 0)
//            }
        })

        viewModel.loadingCart.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            if(it) progress.show(activity!!) else progress.dismiss()
        })

        btnAdd.setOnClickListener {
            var jml = etJumlah2.text.toString().toInt()
            if(selectedPrice.value == null) {
                showAlert(activity!!, getString(R.string.jenispembelianbelum))
                return@setOnClickListener
            }
            if(selectedPrice.value?.pRODUNITMAX == "0"){
                jml = etJumlah2.text.toString().toInt() + 1
            }else if(jml < selectedPrice.value?.pRODUNITMAX?.toInt() ?: 1){
                jml = etJumlah2.text.toString().toInt() + 1
            }
            etJumlah2.setText(jml.toString())
        }
        btnDel.setOnClickListener {
            if(selectedPrice.value == null) {
                showAlert(activity!!, getString(R.string.jenispembelianbelum))
                return@setOnClickListener
            }
            if(etJumlah2.text.toString().toInt() > selectedPrice.value?.pRODUNITMIN?.toInt() ?: 1){
                val jml = etJumlah2.text.toString().toInt() - 1
                etJumlah2.setText(jml.toString())
            }
        }
    }
}