package com.hastaprimasolusi.rana.ui.login

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.utils.showalertInformation
import kotlinx.android.synthetic.main.activity_konfirmasi_akun.*

/**
 * Created By maasrahman on 7/3/20
 */
class KonfirmasiAkunActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_konfirmasi_akun)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Informasi"

        val bundle = intent.extras
        if(bundle != null){
            loadWeb(bundle.getString("data", ""))
        }else{
            showalertInformation(this@KonfirmasiAkunActivity, getString(R.string.terjadikesalahansaatmemuatdata)){
                finish()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        finish()
    }

    private fun loadWeb(htmlString: String){
        webView.loadData(htmlString, "text/html", "base64")
    }
}