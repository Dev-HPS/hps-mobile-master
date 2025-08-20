package com.hastaprimasolusi.rana.ui.report.transaksi

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.data.network.response.order.OrderStatusModel
import com.hastaprimasolusi.rana.ui.report.ReportViewModel
import com.hastaprimasolusi.rana.utils.showAlert
import kotlinx.android.synthetic.main.activity_produk_detail.*
import org.koin.android.ext.android.inject

class TransReportActivity: AppCompatActivity() {
    private val viewModel: ReportViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trans_report)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initFragment()
    }

    private fun initFragment(){
        viewModel.dateEndParam = ""
        viewModel.dateStartParam = ""
        viewModel.searchParam = ""
        viewModel.statusParam = ""
        viewModel.typeParam = ""
        viewModel.offset = 0

        viewModel.showErrorReport.observe(this, Observer {
            if(it == null) return@Observer
            showAlert(this@TransReportActivity, it)
            viewModel.showErrorReport.postValue(null)
        })

        var listType = mutableListOf<OrderStatusModel>()
        listType.add(OrderStatusModel(sTSCODE = "2", sTSTEXT = "Pembelian"))
        listType.add(OrderStatusModel(sTSCODE = "3", sTSTEXT = "Penjualan"))
        viewModel.orderType.value = listType

        supportFragmentManager.beginTransaction()
            .add(R.id.frame,
                TransReportFragment()
            )
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount > 0){
            supportFragmentManager.popBackStack()
        }else{
            finish()
        }
    }
}