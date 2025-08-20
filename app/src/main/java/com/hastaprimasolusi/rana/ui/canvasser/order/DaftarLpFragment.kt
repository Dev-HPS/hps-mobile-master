package com.hastaprimasolusi.rana.ui.canvasser.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.adapter.page.LpAdapter
import com.hastaprimasolusi.rana.data.network.response.LpModel
import com.hastaprimasolusi.rana.data.network.response.order.CartProdukModel
import com.hastaprimasolusi.rana.ui.canvasser.CanvasViewModel
import com.hastaprimasolusi.rana.ui.canvasser.kembalibarang.CanvasListOpnameFragment
import com.hastaprimasolusi.rana.utils.UtilsPref
import com.hastaprimasolusi.rana.utils.convertCurrency
import com.hastaprimasolusi.rana.utils.convertCurrencyNo
import com.hastaprimasolusi.rana.utils.showalertConfirmation
import kotlinx.android.synthetic.main.fragment_daftar_lp.*
import org.koin.android.ext.android.inject

/**
 * Created By maasrahman on 7/20/20
 */
class DaftarLpFragment: Fragment() {
    private val viewModel: CanvasViewModel by inject()
    private lateinit var adapter: LpAdapter
    private var listData = mutableListOf<LpModel>()
    private var isOpname = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.daftarlp)
        return inflater.inflate(R.layout.fragment_daftar_lp, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val bundle = arguments
        isOpname = bundle != null
        adapter = LpAdapter {
            viewModel.selectedLp = it
            if(isOpname){
                toOpname()
            }else{
                toProduk()
            }
        }
        recyclerLp.layoutManager = LinearLayoutManager(activity)
        recyclerLp.itemAnimator = DefaultItemAnimator()
        recyclerLp.adapter = adapter
        btnDetail.setOnClickListener(listener)
        btnTambah.setOnClickListener(listener)
        initViewModel()
    }

    private fun toProduk(){
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.frame, DaftarProdukListFragment())
            ?.addToBackStack(null)
            ?.commit()
    }

    private fun toOpname(){
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.frame, CanvasListOpnameFragment())
            ?.addToBackStack(null)
            ?.commit()
    }

    private val listener = View.OnClickListener { view ->
        when(view.id){
            R.id.btnDetail -> {
                val selectedLp = listData.single { it.lPCODE == viewModel.cartData?.cARTLPCODE }
                viewModel.selectedLp = selectedLp
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.frame, DetailOrderFragment())
                    ?.addToBackStack(null)
                    ?.commit()
            }
            R.id.btnTambah -> {
                if(viewModel.selectedLp != null){
                    toProduk()
                }else{
                    val selectedLp = listData.single { it.lPCODE == viewModel.cartData?.cARTLPCODE }
                    viewModel.selectedLp = selectedLp
                    toProduk()
                }
            }
        }
    }

    private fun initViewModel(){
        viewModel.loadingProses.observe(viewLifecycleOwner, Observer {
            swipe.isRefreshing = it
        })

        viewModel.listLp.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            listData.clear()
            listData.addAll(it)
            adapter.update(it)
        })

        viewModel.listCart.observe(viewLifecycleOwner, Observer {
            if(it.isNullOrEmpty()){
                layoutSummary.visibility = View.GONE
            }else{
                layoutSummary.visibility = View.VISIBLE
                updateSummary(it)
            }
        })

        if(viewModel.listLp.value == null && adapter.itemCount == 0){
            viewModel.getLp()
        }

        swipe.setOnRefreshListener {
            viewModel.getLp()
        }
    }

    private fun updateSummary(data: List<CartProdukModel>){
        var jml = 0
        var jmlItem = 0
        data.forEach {
            val totalAmount = it.tOTALAMT.toString().toIntOrNull()
            val totalQty = it.tOTALQTY.toString().toIntOrNull()
            val prodPrice = it.pRODPRICE?.first()?.pRODPRICE.toString().toIntOrNull() ?: 0
            val prodQty = it.pRODPRICE?.first()?.pRODQTY.toString().toIntOrNull() ?: 0

            jml = totalAmount ?: jml + (prodQty * prodPrice)
            jmlItem = totalQty ?: jmlItem + prodQty
        }
        txtJmlItem.text = convertCurrencyNo(jmlItem.toString(), 3, '.')
        txtTotal.text = convertCurrency(jml.toString(), 3, '.', UtilsPref.loadString(getString(R.string.currencySymbol)))
    }
}