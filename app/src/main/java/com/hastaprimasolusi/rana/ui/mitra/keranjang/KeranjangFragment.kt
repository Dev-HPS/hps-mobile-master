package com.hastaprimasolusi.rana.ui.mitra.keranjang

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
import com.hastaprimasolusi.rana.adapter.page.KeranjangAdapter
import com.hastaprimasolusi.rana.data.network.response.order.CartProdukModel
import com.hastaprimasolusi.rana.ui.common.ProgDialog
import com.hastaprimasolusi.rana.ui.mitra.MainActivity
import com.hastaprimasolusi.rana.ui.mitra.MitraViewModel
import com.hastaprimasolusi.rana.utils.UtilsPref
import com.hastaprimasolusi.rana.utils.converNumber
import com.hastaprimasolusi.rana.utils.convertCurrency
import com.hastaprimasolusi.rana.utils.showalertConfirmation
import kotlinx.android.synthetic.main.dialog_cart_sukses.*
import kotlinx.android.synthetic.main.dialog_konfirmasi.*
import kotlinx.android.synthetic.main.dialog_konfirmasi.btnYa
import kotlinx.android.synthetic.main.dialog_konfirmasi.txtHeader
import kotlinx.android.synthetic.main.fragment_keranjang.*
import org.koin.android.ext.android.inject

/**
 * Created By maasrahman on 2020-04-26
 */
class KeranjangFragment: Fragment() {
    private val viewModel: MitraViewModel by inject()
    private lateinit var adapter: KeranjangAdapter
    private val progress = ProgDialog().getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_keranjang, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.cart14.postValue(false)
        adapter =
            KeranjangAdapter({ model, jml ->
                viewModel.updateCart(model, jml)
            }, {model ->
                val dialog = Dialog(activity!!)
                dialog.setContentView(R.layout.dialog_konfirmasi)
                dialog.txtHeader.text = getString(R.string.yakinmenghapusproduk)
                dialog.btnYa.setOnClickListener {
                    viewModel.deleteCart(model)
                    dialog.dismiss()
                }
                dialog.btnTidak.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            })

        recyclerKeranjang.layoutManager = LinearLayoutManager(activity)
        recyclerKeranjang.itemAnimator = DefaultItemAnimator()
        recyclerKeranjang.adapter = adapter

        initViewModel()
        btnBayar.setOnClickListener(listener)
        btnBelanja.setOnClickListener(listener)
    }

    private var listener = View.OnClickListener { view ->
        when(view.id){
            R.id.btnBayar -> {
                showalertConfirmation(activity!!, "Lanjutkan pemesanan?"){
                    viewModel.actionCheckOut {
                        val dialog = Dialog(activity!!)
                        dialog.setContentView(R.layout.dialog_cart_sukses)
                        dialog.txtHeader.text = it
                        dialog.btnOk.setOnClickListener {
                            viewModel.isHistoryLoading = false
                            viewModel.isCartLoading = false
                            layoutNoData.visibility = View.VISIBLE
                            layoutSummary.visibility = View.GONE
                            (activity as MainActivity).changeNav(R.id.navRiwayat)
                            dialog.dismiss()
                        }
                        dialog.show()
                    }
                }
            }
            R.id.btnBelanja -> {
                (activity as MainActivity).changeNav(R.id.navHome)
            }
        }
    }

    private fun updateSummary(data: List<CartProdukModel>){
        lblSubTotal.text = "Sub Total (${viewModel.cartData?.cARTTOTALQTY} Item)"
        txtSubTotal.text = convertCurrency(viewModel.cartData?.cARTTOTALSUB.toString(), 3, '.',
            viewModel.cartData?.cARTCURRENCY.toString())
        txtOngkir.text = convertCurrency(viewModel.cartData?.cARTONGKIR.toString(), 3, '.',
            viewModel.cartData?.cARTCURRENCY.toString())
        txtTotal.text = convertCurrency(viewModel.cartData?.cARTTOTALAMT.toString(), 3, '.',
            viewModel.cartData?.cARTCURRENCY.toString())
        UtilsPref.saveString(getString(R.string.currencySymbol), viewModel.cartData?.cARTCURRENCY.toString())
    }

    private fun initViewModel(){
        viewModel.loadingCart.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            swipe.isRefreshing = it
        })

        viewModel.listCart.observe(viewLifecycleOwner, Observer {
            if(it.isNullOrEmpty()){
                layoutSummary.visibility = View.GONE
                layoutNoData.visibility = View.VISIBLE
                return@Observer
            }
            layoutSummary.visibility = View.VISIBLE
            layoutNoData.visibility = View.GONE
            updateSummary(it)
            adapter.updateData(it)
            viewModel.isCartLoading = true
        })

        viewModel.cart14.observe(viewLifecycleOwner, Observer {
            layoutNoData.visibility = if(it) View.VISIBLE else View.GONE
        })

        viewModel.loadingCheckOut.observe(viewLifecycleOwner, Observer {
            if(it) progress.show(activity!!) else progress.dismiss()
        })

        if(!viewModel.isCartLoading){
            viewModel.getCart()
        }

        swipe.setOnRefreshListener {
            viewModel.getCart()
        }
    }

}