package com.hastaprimasolusi.rana.ui.mitra.keranjang.chekout

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.ui.common.ProgDialog
import com.hastaprimasolusi.rana.ui.mitra.MitraViewModel
import com.hastaprimasolusi.rana.utils.showAlert
import kotlinx.android.synthetic.main.activity_pembayaran.*
import org.koin.android.ext.android.inject

/**
 * Created By maasrahman on 2020-05-01
 */
class PembayaranActivity: AppCompatActivity() {
    private val viewModel: MitraViewModel by inject()
    private val progress = ProgDialog().getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembayaran)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val bundle = intent.extras
        if(bundle != null){
            val id = bundle.getString("id", "")
            viewModel.getHistoryDetail(id)
        }

        initViewModel()
    }

    private fun initViewModel(){
        viewModel.showErrorBayar.observe(this, Observer {
            if(it == null) return@Observer
            showAlert(this@PembayaranActivity, it)
            viewModel.showErrorBayar.postValue(null)
        })

        viewModel.isUnAuthorized.observe(this, Observer {
            if(it){
                finish()
            }
        })

        viewModel.historyDetail.observe(this, Observer {
            if(it == null) return@Observer
            addFragment()
        })

        viewModel.loadingHistoryDetail.observe(this, Observer {
            if(it) progress.show(this@PembayaranActivity) else progress.dismiss()
        })
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount > 0){
            supportFragmentManager.popBackStack()
        }else{
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addFragment(){
        supportFragmentManager.beginTransaction()
            .add(R.id.frame, PembayaranFragment())
            .commit()
    }
}