package com.hastaprimasolusi.rana.ui.akun

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.utils.showalertInformation
import kotlinx.android.synthetic.main.activity_profile.*

/**
 * Created By maasrahman on 2020-05-02
 */
class ProfileActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bundle = intent.extras
        if(bundle != null){
            when(bundle.getString("type")){
                "ubah" -> replaceFragment(UbahProfileFragment())
                else -> replaceFragment(UbahPasswordFragment())
            }
        }else{
            showalertInformation(this, getString(R.string.terjadikesalahansaatmemuatdata)){
                finish()
            }
        }
    }

    private fun replaceFragment(frag: Fragment){
        supportFragmentManager.beginTransaction()
            .add(R.id.frame, frag)
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}