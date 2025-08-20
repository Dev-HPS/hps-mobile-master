package com.hastaprimasolusi.rana.ui.canvasser.order

import android.app.Dialog
import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.adapter.page.ProdukCnvsAdapter
import com.hastaprimasolusi.rana.data.ApiService
import com.hastaprimasolusi.rana.data.network.response.order.CartProdukModel
import com.hastaprimasolusi.rana.data.network.response.order.CartResponse
import com.hastaprimasolusi.rana.data.network.response.produk.ProdPriceModel
import com.hastaprimasolusi.rana.data.network.response.produk.ProdukModel
import com.hastaprimasolusi.rana.ui.canvasser.CanvasViewModel
import com.hastaprimasolusi.rana.ui.common.ProgDialog
import com.hastaprimasolusi.rana.utils.*
import kotlinx.android.synthetic.main.dialog_jmlbarang.*
import kotlinx.android.synthetic.main.dialog_jmlbarang.chipGroup
import kotlinx.android.synthetic.main.fragment_daftar_produk_list.*
import org.koin.android.ext.android.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created By maasrahman on 6/1/20
 */
class DaftarProdukListFragment: Fragment() {
    private val viewModel: CanvasViewModel by inject()
    private val apiService: ApiService by inject()
    private lateinit var adapter: ProdukCnvsAdapter
    private val progress = ProgDialog().getInstance()
    private var limit = 20
    private var offset = 0
    private var isWaitingData = false
    private var isLoadMore = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.orderbarang)
        return inflater.inflate(R.layout.fragment_daftar_produk_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(ComponentName(activity!!, ResultProdukCnvsActivity::class.java)))
        searchView.queryHint = getString(R.string.cariproduk)
        val etSearch = searchView.findViewById(androidx.appcompat.R.id.search_src_text) as EditText
        etSearch.hint = getString(R.string.cariproduk)

        adapter = ProdukCnvsAdapter { prod ->
            if(viewModel.cartData != null && viewModel.cartData?.cARTLPCODE != prod?.pRODLPCODE){
                showalertConfirmation(activity!!, "Untuk menambahkan Produk dari LP berbeda maka Cart sebelumnya akan dihapus, Lanjutkan?"){
                    prod?.let {
                        deleteAllCart(it)
                    }
                }
                return@ProdukCnvsAdapter
            }
            prod?.let {
                showDialogProd(it)
            }
        }
        recyclerProduk.layoutManager = LinearLayoutManager(activity)
        recyclerProduk.itemAnimator = DefaultItemAnimator()
        recyclerProduk.adapter = adapter
        recyclerProduk.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (!isWaitingData && isLoadMore) {
                    if (linearLayoutManager != null &&
                        linearLayoutManager.findLastCompletelyVisibleItemPosition() == adapter.itemCount - 1) {
                        adapter.addProgress()
                        isWaitingData = true
                        viewModel.getProdukNew(viewModel.selectedLp?.lPCODE, limit.toString(), offset.toString(), false){
                            initData(it, false)
                        }
                    }
                }
            }
        })

        btnDetail.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.frame, DetailOrderFragment())
                ?.addToBackStack(null)
                ?.commit()
        }
        initViewModel()
    }

    private fun initData(list: List<ProdukModel>, isLoading: Boolean){
        layoutNoData.visibility = View.GONE
        isLoadMore = list.size.rem(limit) == 0
        if(!isLoading) adapter.removeProgress()
        if(isWaitingData){
            adapter.removeProgress()
            isWaitingData = false
            offset += limit
        }
        adapter.updateData(list)
    }

    private fun deleteAllCart(prod: ProdukModel){
        var isShowDialog = false
        val listCart = viewModel.listCart.value
        var countSuccess= listCart?.size ?: 0
        progress.show(activity!!)
        listCart?.forEach {
//            println("CEK CART ${Gson().toJson(it)}")
            apiService.actionCartDeleteCall(it.pRODID.toString()).enqueue(object: Callback<CartResponse>{
                override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                    progress.dismiss()
                    showAlert(activity!!, t.message.toString())
                }

                override fun onResponse(
                    call: Call<CartResponse>,
                    response: Response<CartResponse>
                ) {
                    progress.dismiss()
                    val resp = response.body()
                    if(resp?.rC == "0000"){
                        val list = viewModel.listCart.value
                        list?.remove(it)
                        viewModel.listCart.postValue(list)

                        if(countSuccess > 1){
                            countSuccess -= 1
                        }else{
                            if(!isShowDialog){
                                showDialogProd(prod)
                                isShowDialog = true
                            }
                        }
                    }
                }
            })
        }
    }

    private fun showDialogProd(prod: ProdukModel){
        var min = 0
        var max = 0
        progress.dismiss()
        val selectedPrice = MutableLiveData<ProdPriceModel>()
        val dialog = Dialog(activity!!)
        dialog.setContentView(R.layout.dialog_jmlbarang)
        dialog.btnSimpan.setOnClickListener {
            if(dialog.etJumlah.text.isNullOrEmpty()){
                dialog.etJumlah.error = getString(R.string.wajibdiisi)
                return@setOnClickListener
            }else if(selectedPrice.value == null){
                showAlert(activity!!, getString(R.string.jenispembelianbelum))
                return@setOnClickListener
            }else if(min > 0){
                if(min > dialog.etJumlah.text.toString().toIntOrNull() ?: 0){
                    showAlert(activity!!, "${selectedPrice.value?.pRODUNITNAME} minimum pembelian $min")
                    return@setOnClickListener
                }
                if(max > 0 && dialog.etJumlah.text.toString().toIntOrNull() ?: 0 > max){
                    showAlert(activity!!, "${selectedPrice.value?.pRODUNITNAME} maksimum pembelian $max")
                    return@setOnClickListener
                }
            }
            viewModel.actionCart(prod.pRODID.toString(), dialog.etJumlah.text.toString(), selectedPrice.value?.pRODUNIT.toString()) { type, response ->
                when(response.rC){
                    "0000" -> {
                        response.dATA?.cARTCURRENCY?.let { currency ->
                            UtilsPref.saveString(getString(R.string.currencySymbol), currency)
                        }
                        val cart = CartProdukModel()
                        cart.pRODCATEGORY = prod.pRODCATEGORY
                        cart.pRODCODE = prod.pRODCODE
                        cart.pRODDISCOUNT = prod.pRODDISCOUNT
                        cart.pRODID = prod.pRODID
                        cart.pRODNAME = prod.pRODNAME

                        //DEVEL PRICE
                        var priceList = mutableListOf<ProdPriceModel>()
                        selectedPrice.value?.apply {
                            pRODQTY = dialog.etJumlah.text.toString()
                        }
                        selectedPrice.value?.let {
                            priceList.add(it)
                        }
                        cart.pRODPRICE = priceList
                        cart.pRODPRICELIST = prod.pRODPRICELIST
                        cart.pRODPICSMALL = prod.pRODPICSMALL
                        cart.pRODPICMEDIUM = prod.pRODPICMEDIUM
                        viewModel.addCart(type, cart)
                    }
                    "0001" -> {
                        viewModel.isUnAuthorized.postValue(true)
                    }
                    else -> {
                        showAlert(activity!!, response.rCM.toString())
                    }
                }
                dialog.dismiss()
            }
        }

        dialog.btnBatal.setOnClickListener {
            dialog.dismiss()
        }

        selectedPrice.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            min = it.pRODUNITMIN?.toIntOrNull() ?: 0
            max = it.pRODUNITMAX?.toIntOrNull() ?: 0
            dialog.lblHarga.text = "Harga ${it.pRODUNITNAME}"
            dialog.txtHarga.text = convertCurrency(it.pRODPRICE.toString(), 3, '.',
                UtilsPref.loadString(getString(R.string.currencySymbol)))
            if(min > 0){
                dialog.etJumlah.setText(min.toString())
                dialog.etJumlah.setSelection(dialog.etJumlah.text?.length ?: 0)
            }
        })

        prod.pRODPRICE?.forEach { row ->
            val chip = activity?.layoutInflater?.inflate(R.layout.item_chip, null, false) as Chip
            chip.text = row.pRODUNITNAME
            chip.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked){
                    selectedPrice.postValue(row)
                }
            }
            dialog.chipGroup.addView(chip)
        }

        dialog.lblHarga.text = "Harga (${prod.pRODPRICE?.first()?.pRODUNITNAME})"
        dialog.txtHarga.text = convertCurrency(prod.pRODPRICE?.first()?.pRODPRICE.toString(), 3, '.',
            UtilsPref.loadString(getString(R.string.currencySymbol)))

        dialog.show()
    }

    private fun initViewModel(){
        layoutNoData.visibility = View.VISIBLE

        viewModel.loadingCart.observe(viewLifecycleOwner, Observer {
            if(it) progress.show(activity!!) else progress.dismiss()
        })

        viewModel.loadingProses.observe(viewLifecycleOwner, Observer {
            swipe.isRefreshing = it
        })

        viewModel.listCart.observe(viewLifecycleOwner, Observer {
            if(it.isNullOrEmpty()){
                layoutSummary.visibility = View.GONE
            }else{
                layoutSummary.visibility = View.VISIBLE
                updateSummary(it)
            }
        })

//        val listProduk = viewModel.loadProdukLp(viewModel.selectedLp?.lPCODE)
//        if(listProduk == null){
            isWaitingData = true
            viewModel.getProdukNew(viewModel.selectedLp?.lPCODE, limit.toString(), offset.toString(), true){
                initData(it, true)
            }
//        }else{
//            layoutNoData.visibility = View.GONE
//            adapter.updateData(listProduk)
//        }

        swipe.setOnRefreshListener {
            offset = 0
            viewModel.getProdukNew(viewModel.selectedLp?.lPCODE, limit.toString(), offset.toString(), true) {
                initData(it, true)
            }
        }
    }

    private fun updateSummary(data: List<CartProdukModel>){
        var jml = 0
        var jmlItem = 0
        data.forEach {
            println(Gson().toJson(it))
            jml += (it.pRODPRICE?.first()?.pRODQTY.toString().toInt() * it.pRODPRICE?.first()?.pRODPRICE.toString().toInt())
            jmlItem += it.tOTALQTY.toString().toIntOrNull() ?: it.pRODPRICE?.first()?.pRODQTY.toString().toInt()
        }
        txtJmlItem.text = convertCurrencyNo(jmlItem.toString(), 3, '.')
        txtTotal.text = convertCurrency(jml.toString(), 3, '.', UtilsPref.loadString(getString(R.string.currencySymbol)))
    }
}