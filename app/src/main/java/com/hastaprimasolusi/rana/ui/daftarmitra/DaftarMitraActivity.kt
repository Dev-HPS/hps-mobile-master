package com.hastaprimasolusi.rana.ui.daftarmitra

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.utils.showalertConfirmation
import kotlinx.android.synthetic.main.activity_daftar_mitra.*

/**
 * Created By maasrahman on 2020-04-26
 */
class DaftarMitraActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_mitra)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.daftar)
        addDaftarFragment()
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount > 0){
            supportFragmentManager.popBackStack()
        }else{
            showalertConfirmation(this, "Batalkan pendaftaran, kembali ke halaman Login?") {
                super.onBackPressed()
            }
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

    private fun addDaftarFragment(){
        supportFragmentManager.beginTransaction()
            .add(R.id.frame, DaftarMitraFragment())
            .commit()
    }
}