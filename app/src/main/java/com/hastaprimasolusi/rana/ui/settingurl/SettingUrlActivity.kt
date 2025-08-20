package com.hastaprimasolusi.rana.ui.settingurl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import com.hastaprimasolusi.rana.BuildConfig
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.helper.Helpers
import com.hastaprimasolusi.rana.ui.daftarmitra.DaftarMitraActivity
import com.hastaprimasolusi.rana.utils.UtilsPref
import com.hastaprimasolusi.rana.utils.showAlert
import kotlinx.android.synthetic.main.activity_produk_detail.*
import kotlinx.android.synthetic.main.activity_setting_url.*
import kotlinx.android.synthetic.main.activity_setting_url.toolbar
import org.jetbrains.anko.startActivity
import kotlin.system.exitProcess

class SettingUrlActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_url)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "SETTING URL"
        btnSaveUrl.setOnClickListener(listener)
        txtResetUrl.setOnClickListener(listener)
        initData()
    }

    private val listener = View.OnClickListener { view ->
        when (view.id) {
            R.id.btnSaveUrl -> {
                saveUrl()
            }

            R.id.txtResetUrl -> {
                resetUrl()
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun resetUrl() {
        UtilsPref.saveString(this@SettingUrlActivity.getString(R.string.url), BuildConfig.BASE_URL)
        exitProcess(0)
    }

    private fun saveUrl() {
        if (!Helpers.isEmptyOrNull(etUrl.text.toString())) {
            UtilsPref.saveString(
                this@SettingUrlActivity.getString(R.string.url),
                etUrl.text.toString()
            )
            exitProcess(0)

        } else {
            showAlert(this@SettingUrlActivity, "Harap isi URL")
        }
    }

    private fun initData() {
        findViewById<EditText>(R.id.etUrl).setText("085714862128")
        if (!Helpers.isEmptyOrNull(UtilsPref.loadString(this@SettingUrlActivity.getString(R.string.url)))) {
            findViewById<EditText>(R.id.etUrl).setText(
                UtilsPref.loadString(
                    this@SettingUrlActivity.getString(
                        R.string.url
                    )
                )
            )
        } else {
            findViewById<EditText>(R.id.etUrl).setText(BuildConfig.BASE_URL)
        }

    }
}