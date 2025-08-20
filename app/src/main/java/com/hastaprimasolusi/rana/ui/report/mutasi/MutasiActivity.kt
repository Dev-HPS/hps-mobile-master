package com.hastaprimasolusi.rana.ui.report.mutasi

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.ui.report.ReportViewModel
import com.hastaprimasolusi.rana.ui.report.transaksi.TransReportFragment
import com.hastaprimasolusi.rana.utils.showAlert
import kotlinx.android.synthetic.main.activity_produk_detail.*
import org.koin.android.ext.android.inject

/**
 * Created by maasrahman on 16/09/20.
 */
class MutasiActivity: AppCompatActivity() {
    private val viewModel: ReportViewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mutasi)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initFragment()
    }

    private fun initFragment(){
        viewModel.showErrorReport.observe(this, Observer {
            if(it == null) return@Observer
            showAlert(this@MutasiActivity, it)
            viewModel.showErrorReport.postValue(null)
        })

        supportFragmentManager.beginTransaction()
            .add(R.id.frame, MutasiFragment())
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> onBackPressed()
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