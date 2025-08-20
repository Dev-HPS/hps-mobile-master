package com.hastaprimasolusi.rana.ui.produk.kategori

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.ui.mitra.MitraViewModel
import com.hastaprimasolusi.rana.utils.showAlert
import kotlinx.android.synthetic.main.activity_list_kategori.*
import org.koin.android.ext.android.inject

/**
 * Created By maasrahman on 2020-04-26
 */
class KategoriActivity: AppCompatActivity() {
    private val viewModel: MitraViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_kategori)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        initViewModel()
    }

    private fun initViewModel(){
        viewModel.listProdukCategory.value?.clear()
        viewModel.showErrorProd.observe(this, Observer {
            if(it == null) return@Observer
            showAlert(this@KategoriActivity, it)
            viewModel.showErrorProd.postValue(null)
        })

        supportFragmentManager.beginTransaction()
            .add(R.id.frame, KategoriFragment())
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