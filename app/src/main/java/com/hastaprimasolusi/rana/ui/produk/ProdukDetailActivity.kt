package com.hastaprimasolusi.rana.ui.produk

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.data.network.response.produk.ProdukModel
import com.hastaprimasolusi.rana.ui.mitra.MitraViewModel
import com.hastaprimasolusi.rana.utils.showalertInformation
import kotlinx.android.synthetic.main.activity_produk_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created By maasrahman on 2020-04-26
 */
class ProdukDetailActivity: AppCompatActivity() {
    private var model: ProdukModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_produk_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bundle = intent.extras
        if(bundle != null){
            model = bundle.getParcelable("model")
            initFragment()
        }else{
            showalertInformation(this@ProdukDetailActivity, getString(R.string.terjadikesalahansaatmemuatdata)){
                finish()
            }
        }
    }

    private fun initFragment(){
        val args = Bundle()
        args.putParcelable("model", model)
        val frag = ProdukDetailFragment()
        frag.arguments = args
        supportFragmentManager.beginTransaction()
            .add(R.id.frame, frag)
            .commit()
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount > 0){
            supportFragmentManager.popBackStack()
        }else{
            super.onBackPressed()
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
}