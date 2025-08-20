package com.hastaprimasolusi.rana.ui.mitra.keranjang.chekout

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.ui.canvasser.CanvasViewModel
import com.hastaprimasolusi.rana.ui.common.ProgDialog
import com.hastaprimasolusi.rana.ui.mitra.MitraViewModel
import com.hastaprimasolusi.rana.utils.showAlert
import kotlinx.android.synthetic.main.activity_pembayaran.*
import org.koin.android.ext.android.inject

/**
 * Created By maasrahman on 2020-05-01
 */
class PembayaranCnvsActivity: AppCompatActivity() {
    private val viewModel: CanvasViewModel by inject()
    private val progress = ProgDialog().getInstance()
    private var isFragmentAdded = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembayaran)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        initViewModel()
    }

    private fun initViewModel(){
        viewModel.showErrorBayar.observe(this, Observer {
            if(it == null) return@Observer
            showAlert(this@PembayaranCnvsActivity, it)
            viewModel.showErrorBayar.postValue(null)
        })

        viewModel.loadingHistoryDetail.observe(this, Observer {
            if(it) progress.show(this) else progress.dismiss()
        })

        viewModel.historyDetail.observe(this, Observer {
            if(it == null) return@Observer
            if(!isFragmentAdded){
                addFragment()
            }
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
        isFragmentAdded = true
        supportFragmentManager.beginTransaction()
            .add(R.id.frame, PembayaranCnvsFragment())
            .commit()
    }
}