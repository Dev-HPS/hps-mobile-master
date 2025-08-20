package com.hastaprimasolusi.rana.ui.canvasser.order

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.adapter.page.ProdukCnvsAdapter
import com.hastaprimasolusi.rana.data.ApiService
import com.hastaprimasolusi.rana.data.network.response.order.CartProdukModel
import com.hastaprimasolusi.rana.data.network.response.produk.CategoryModel
import com.hastaprimasolusi.rana.ui.canvasser.CanvasViewModel
import com.hastaprimasolusi.rana.ui.common.ProgDialog
import com.hastaprimasolusi.rana.utils.UtilsPref
import com.hastaprimasolusi.rana.utils.showAlert
import kotlinx.android.synthetic.main.dialog_jmlbarang.*
import kotlinx.android.synthetic.main.fragment_daftar_produk_cnvs.*
import org.koin.android.ext.android.inject

/**
 * Created By maasrahman on 6/1/20
 */
class DaftarProdukCnvsFragment: Fragment() {
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
            val dialog = Dialog(activity!!)
            dialog.setContentView(R.layout.dialog_jmlbarang)
            dialog.btnSimpan.setOnClickListener {
                if(dialog.etJumlah.text.isNullOrEmpty()){
                    showAlert(activity!!, getString(R.string.isianbelumlengkap))
                    return@setOnClickListener
                }
                viewModel.actionCart(prod?.pRODID.toString(), dialog.etJumlah.text.toString(), "") { type, response ->
                    when(response.rC){
                        "0000" -> {
                            UtilsPref.saveString(getString(R.string.currencySymbol), response.dATA?.cARTCURRENCY.toString())
//                            val objData = response.dATA
                            val cart = CartProdukModel()
                            cart.pRODCATEGORY = prod?.pRODCATEGORY
                            cart.pRODCODE = prod?.pRODCODE
                            cart.pRODDISCOUNT = prod?.pRODDISCOUNT
                            cart.pRODID = prod?.pRODID
                            cart.pRODNAME = prod?.pRODNAME

                            //DEVEL PRODUK
                           //cart.pRODPRICE = prod?.pRODPRICE
                            cart.pRODPRICELIST = prod?.pRODPRICELIST
                            cart.pRODPICSMALL = prod?.pRODPICSMALL
                            cart.pRODPICMEDIUM = prod?.pRODPICMEDIUM
                            cart.pRODQTY = dialog.etJumlah.text.toString()
                            viewModel.addCart(type, cart)
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
            dialog.show()
        }
        recyclerProduk.layoutManager = LinearLayoutManager(activity)
        recyclerProduk.itemAnimator = DefaultItemAnimator()
        recyclerProduk.adapter = adapter

        initViewModel()
    }

    private fun initViewModel(){
        viewModel.loadingCart.observe(viewLifecycleOwner, Observer {
            if(it){
                prog.show(activity!!)
            }else{
                prog.dismiss()
            }
        })
    }

    private fun getProduk(){
        progress.visibility = View.VISIBLE
        viewModel.getProdukLP(apiService, model?.cATEGORYID){ resp ->
            progress.visibility = View.GONE
            if(resp.rC == "0000"){
                resp.dATA?.let { prod ->
                    layoutNoData.visibility = if(prod.isEmpty()) View.VISIBLE else View.GONE
                    adapter.updateData(prod)
                    viewModel.addProductLp(model?.cATEGORYID.toString(), prod.toMutableList())
                }
            }else{
                layoutNoData.visibility = View.VISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val listData = viewModel.checkProdukLp(model?.cATEGORYID.toString())
        if(listData != null){
            adapter.updateData(listData)
        }else{
           getProduk()
        }
    }
}