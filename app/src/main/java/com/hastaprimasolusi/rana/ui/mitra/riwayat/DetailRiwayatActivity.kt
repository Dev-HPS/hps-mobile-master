package com.hastaprimasolusi.rana.ui.mitra.riwayat

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.data.network.response.order.HistoryOrderModel
import com.hastaprimasolusi.rana.ui.mitra.MitraViewModel
import com.hastaprimasolusi.rana.utils.showAlert
import com.hastaprimasolusi.rana.utils.showalertInformation
import kotlinx.android.synthetic.main.activity_detail_riwayat.*
import org.koin.android.ext.android.inject

/**
 * Created By maasrahman on 5/20/20
 */
class DetailRiwayatActivity: AppCompatActivity() {
    private val viewModel: MitraViewModel by inject()
    private var orderId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_riwayat)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val bundle = intent.extras
        if(bundle != null){
            viewModel.historyDetail.postValue(null)
            orderId = bundle.getString("id")
            addFragment()
        }else{
            showalertInformation(this@DetailRiwayatActivity, getString(R.string.terjadikesalahansaatmemuatdata)){
                finish()
            }
        }
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
        viewModel.showErrorDetail.observe(this, Observer {
            if(it == null) return@Observer
            showAlert(this@DetailRiwayatActivity, it)
            viewModel.showErrorDetail.postValue(null)
        })

        val frag = DetailRiwayatFragment()
        val args = Bundle()
        args.putString("id", orderId)
        frag.arguments = args
        supportFragmentManager.beginTransaction()
            .add(R.id.frame, frag)
            .commit()
    }
}