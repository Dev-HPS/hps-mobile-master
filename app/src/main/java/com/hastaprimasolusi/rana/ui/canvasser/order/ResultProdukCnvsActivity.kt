package com.hastaprimasolusi.rana.ui.canvasser.order

import android.app.Dialog
import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.adapter.page.ProdukCnvsAdapter
import com.hastaprimasolusi.rana.data.network.response.order.CartProdukModel
import com.hastaprimasolusi.rana.data.network.response.produk.ProdPriceModel
import com.hastaprimasolusi.rana.data.network.response.produk.ProdukModel
import com.hastaprimasolusi.rana.ui.canvasser.CanvasViewModel
import com.hastaprimasolusi.rana.ui.common.ProgDialog
import com.hastaprimasolusi.rana.utils.UtilsPref
import com.hastaprimasolusi.rana.utils.convertCurrency
import com.hastaprimasolusi.rana.utils.showAlert
import com.hastaprimasolusi.rana.utils.showalertInformation
import kotlinx.android.synthetic.main.activity_list_kategori.toolbar
import kotlinx.android.synthetic.main.activity_result_produk.*
import kotlinx.android.synthetic.main.dialog_jmlbarang.*
import org.koin.android.ext.android.inject

/**
 * Created By maasrahman on 5/27/20
 */
class ResultProdukCnvsActivity: AppCompatActivity() {
    private val viewModel: CanvasViewModel by inject()
    private lateinit var adapter: ProdukCnvsAdapter
    private val prog = ProgDialog().getInstance()
    private var limit = 20
    private var offset = 0
    private var isWaitingData = false
    private var isLoadMore = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_produk)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = getString(R.string.pencarianproduk)

        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            query?.let { initViewModel(it) }
        }else{
            showalertInformation(this, getString(R.string.terjadikesalahansaatmemuatdata)){
                finish()
            }
        }
    }

    private fun initViewModel(paramString: String) {
        adapter = ProdukCnvsAdapter { prod ->
            prod?.let { it1 -> showDialogProd(it1) }
        }
        recyclerProduk.layoutManager = LinearLayoutManager(this)
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
                        viewModel.getProdukBySearch(viewModel.selectedLp?.lPCODE.toString(), paramString, limit.toString(), offset.toString(), false)
                    }
                }
            }
        })
        viewModel.showErrorProd.observe(this, Observer {
            if (it == null) return@Observer
            viewModel.showErrorProd.postValue(null)
            adapter.removeProgress()
            showAlert(this@ResultProdukCnvsActivity, it)
        })

        viewModel.loadingProduk.observe(this, Observer {
            progress.visibility = if(it) View.VISIBLE else View.GONE
        })

        viewModel.loadingCart.observe(this, Observer {
            if(it) prog.show(this@ResultProdukCnvsActivity) else prog.dismiss()
        })

        viewModel.listProdukSearch.observe(this, Observer {
            if(it == null) return@Observer
            isLoadMore = it.size.rem(limit) == 0
            if(isWaitingData){
                adapter.removeProgress()
                isWaitingData = false
                offset += limit
            }
            adapter.updateData(it)
        })

        if(!isWaitingData){
            isWaitingData = true
            viewModel.getProdukBySearch(viewModel.selectedLp?.lPCODE.toString(), paramString, limit.toString(), offset.toString(), true)
        }
    }

    private fun showDialogProd(prod: ProdukModel){
        var min = 0
        var max = 0
        prog.dismiss()
        val selectedPrice = MutableLiveData<ProdPriceModel>()
        val dialog = Dialog(this@ResultProdukCnvsActivity)
        dialog.setContentView(R.layout.dialog_jmlbarang)
        dialog.btnSimpan.setOnClickListener {
            if(dialog.etJumlah.text.isNullOrEmpty()){
                dialog.etJumlah.error = getString(R.string.wajibdiisi)
                return@setOnClickListener
            }else if(selectedPrice.value == null){
                showAlert(this@ResultProdukCnvsActivity, getString(R.string.jenispembelianbelum))
                return@setOnClickListener
            }else if(min > 0){
                if(min > dialog.etJumlah.text.toString().toIntOrNull() ?: 0){
                    showAlert(this@ResultProdukCnvsActivity, "${selectedPrice.value?.pRODUNITNAME} minimum pembelian $min")
                    return@setOnClickListener
                }
                if(max > 0 && dialog.etJumlah.text.toString().toIntOrNull() ?: 0 > max){
                    showAlert(this@ResultProdukCnvsActivity, "${selectedPrice.value?.pRODUNITNAME} maksimum pembelian $max")
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
                        showAlert(this@ResultProdukCnvsActivity, response.rCM.toString())
                    }
                }
                dialog.dismiss()
            }
        }

        dialog.btnBatal.setOnClickListener {
            dialog.dismiss()
        }

        selectedPrice.observe(this, Observer {
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
            val chip = layoutInflater.inflate(R.layout.item_chip, null, false) as Chip
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

    override fun onBackPressed() {
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}