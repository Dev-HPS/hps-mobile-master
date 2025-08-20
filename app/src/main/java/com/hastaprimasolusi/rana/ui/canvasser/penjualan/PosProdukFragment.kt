package com.hastaprimasolusi.rana.ui.canvasser.penjualan

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.adapter.page.ProdukCnvsAdapter
import com.hastaprimasolusi.rana.data.ApiService
import com.hastaprimasolusi.rana.data.network.response.order.CartProdukModel
import com.hastaprimasolusi.rana.data.network.response.produk.CategoryModel
import com.hastaprimasolusi.rana.data.network.response.produk.ProdPriceModel
import com.hastaprimasolusi.rana.data.network.response.produk.ProdukModel
import com.hastaprimasolusi.rana.ui.canvasser.CanvasViewModel
import com.hastaprimasolusi.rana.ui.common.ProgDialog
import com.hastaprimasolusi.rana.utils.UtilsPref
import com.hastaprimasolusi.rana.utils.convertCurrency
import com.hastaprimasolusi.rana.utils.showAlert
import kotlinx.android.synthetic.main.dialog_jmlbarang.*
import kotlinx.android.synthetic.main.fragment_daftar_produk_cnvs.*
import org.koin.android.ext.android.inject

/**
 * Created By maasrahman on 6/7/20
 */
class PosProdukFragment: Fragment() {
    private val viewModel: CanvasViewModel by inject()
    private val apiService: ApiService by inject()
    private lateinit var adapter: ProdukCnvsAdapter
    private var model: CategoryModel? = null
    private val prog = ProgDialog().getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_daftar_produk_cnvs, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val bundle = arguments
        if(bundle != null){
            model = bundle.getParcelable("model")
        }

        adapter = ProdukCnvsAdapter { prod ->
            dialogInputBarang(prod)
        }
        recyclerProduk.layoutManager = LinearLayoutManager(activity)
        recyclerProduk.itemAnimator = DefaultItemAnimator()
        recyclerProduk.adapter = adapter

        initViewModel()
    }

    private fun dialogInputBarang(prod: ProdukModel?){
//        val selectedPrice = MutableLiveData<ProdUnitModel>()
        val selectedPrice = MutableLiveData<ProdPriceModel>()
        val dialog = Dialog(activity!!)
        dialog.setContentView(R.layout.dialog_jmlbarang)
        dialog.lblJenisBeli.text = "Jenis Penjualan"
//        stok view
//        dialog.txtStokDialog.visibility = View.VISIBLE
        dialog.btnSimpan.setOnClickListener {
            if(dialog.etJumlah.text.isNullOrEmpty()) {
                dialog.etJumlah.error = getString(R.string.isianbelumlengkap)
                return@setOnClickListener
            }else if(selectedPrice.value == null){
                showAlert(activity!!, getString(R.string.jenispembelianbelum))
                return@setOnClickListener
            }
//            validasi stok barang
//            else if((dialog.etJumlah.text.toString().toIntOrNull() ?: 0) > (selectedPrice.value?.pRODSTOCK.toString().toIntOrNull() ?: 0)){
//                dialog.etJumlah.error = "Jumlah pesanan melebihi Stok ${selectedPrice.value?.pRODSTOCK}"
//                return@setOnClickListener
//            }
//            viewModel.actionPos(prod?.pRODID.toString(), dialog.etJumlah.text.toString(), selectedPrice.value?.unitId.toString()) { type, response ->
            viewModel.actionPos(prod?.pRODID.toString(), dialog.etJumlah.text.toString(), selectedPrice.value?.pRODUNIT.toString()) { type, response ->
            when(response.rC){
                    "0000" -> {
                        response.dATA?.cURRENCY?.let { currency ->
                            UtilsPref.saveString(getString(R.string.currencySymbol), currency)
                        }
                        val objData = response.dATA
                        val cart = CartProdukModel()
                        cart.pRODCATEGORY = prod?.pRODCATEGORY
                        cart.pRODCODE = prod?.pRODCODE
                        cart.pRODDISCOUNT = prod?.pRODDISCOUNT
                        cart.iD = prod?.pRODID
                        cart.pRODNAME = prod?.pRODNAME

                        //DEVEL PRICE
                        val priceList = mutableListOf<ProdPriceModel>()
                        //Digunakan karena PROD_UNIT tanpa PRICE
//                        val rowPrice = prod?.pRODPRICE?.find { row -> row.pRODUNIT == selectedPrice.value?.unitId }
//                        if(rowPrice != null){
//                            priceList.add(ProdPriceModel(pRODQTY = dialog.etJumlah.text.toString(), pRODUNIT = selectedPrice.value?.unitId,
//                                pRODUNITNAME = selectedPrice.value?.unitName))
                        priceList.add(ProdPriceModel(pRODQTY = dialog.etJumlah.text.toString(), pRODUNIT = selectedPrice.value?.pRODUNIT,
                            pRODUNITNAME = selectedPrice.value?.pRODUNITNAME))
//                        }

                        cart.pRODPRICE = priceList
                        //cart.pRODPRICE = prod?.pRODPRICE
                        cart.pRODPRICELIST = prod?.pRODPRICELIST
                        cart.pRODPICSMALL = prod?.pRODPICSMALL
                        cart.pRODPICMEDIUM = prod?.pRODPICMEDIUM
                        cart.pRODQTY = dialog.etJumlah.text.toString()
                        cart.tOTALQTY = objData?.tOTALQTY
                        cart.tOTALAMT = objData?.tOTALAMT
                        viewModel.addPos(type, cart)
                    }
                    "0001" -> {
                        viewModel.isUnAuthorized.postValue(true)
                    }
                    else -> {
                        showAlert(activity!!, response.rCM.toString())
                    }
                }
            }
            dialog.dismiss()
        }
        dialog.btnBatal.setOnClickListener {
            dialog.dismiss()
        }
        selectedPrice.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            dialog.lblHarga.text = "Harga ${it.pRODUNITNAME}"
            dialog.txtHarga.text = convertCurrency(it.pRODPRICE.toString(), 3, '.',
                UtilsPref.loadString(getString(R.string.currencySymbol)))
//            dialog.txtStokDialog.text = "Stok ${it.pRODSTOCK}"
        })

//        prod?.pRODUNITS?.forEach { row ->
        prod?.pRODPRICE?.forEach { row ->
            val chip = activity?.layoutInflater?.inflate(R.layout.item_chip, null, false) as Chip
            chip.text = row.pRODUNITNAME
            chip.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked){
                    selectedPrice.postValue(row)
                }
            }
            dialog.chipGroup.addView(chip)
        }

//        dialog.lblHarga.visibility = View.GONE
//        dialog.txtHarga.visibility = View.GONE
        dialog.show()
    }

    private fun initViewModel(){
        viewModel.loadingPos.observe(viewLifecycleOwner, Observer {
            if(it){
                prog.show(activity!!)
            }else{
                prog.dismiss()
            }
        })
    }

    private fun getProduk(){
        if(progress != null) progress.visibility = View.VISIBLE
        viewModel.getProduk(apiService, model?.cATEGORYID){ resp ->
            if(progress != null) progress.visibility = View.GONE
            if(resp.rC == "0000"){
                resp.dATA?.let { prod ->
                    if(layoutNoData != null){
                        layoutNoData.visibility = if(prod.isEmpty()) View.VISIBLE else View.GONE
                    }
                    adapter.updateData(prod)
                    viewModel.addProduct(model?.cATEGORYID.toString(), prod.toMutableList())
                }
            }else{
                if(layoutNoData != null){
                    layoutNoData.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val listData = viewModel.checkProduct(model?.cATEGORYID.toString())
        if(listData != null){
            adapter.updateData(listData)
        }else{
            getProduk()
        }
    }
}