package com.hastaprimasolusi.rana.ui.login

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.data.network.requesthelper.LoginRequest
import com.hastaprimasolusi.rana.ui.canvasser.CanvasMainActivity
import com.hastaprimasolusi.rana.ui.common.ProgDialog
import com.hastaprimasolusi.rana.ui.daftarmitra.DaftarMitraActivity
import com.hastaprimasolusi.rana.ui.lp.LpMainActivity
import com.hastaprimasolusi.rana.ui.mitra.MainActivity
import com.hastaprimasolusi.rana.ui.settingurl.SettingUrlActivity
import com.hastaprimasolusi.rana.utils.UtilsPref
import com.hastaprimasolusi.rana.utils.showAlert
import com.hastaprimasolusi.rana.utils.showalertInformation
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created By maasrahman on 2020-04-26
 */
class LoginActivity: AppCompatActivity(){
    private val viewModel: LoginViewModel by viewModel()
    private var progress = ProgDialog().getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnLogin.setOnClickListener(listener)
        btnDaftar.setOnClickListener(listener)
        txtLupaPassword.setOnClickListener(listener)
        btnsettingUrl.setOnClickListener (listener)
        initView()
//        initDataTest();
    }

    private val listener = View.OnClickListener { view ->
        when(view.id){
            R.id.btnLogin -> { authentication() }
            R.id.btnDaftar -> { startActivity<DaftarMitraActivity>() }
            R.id.btnsettingUrl -> {  startActivity<SettingUrlActivity>() }
            R.id.txtLupaPassword -> {
                showAlert(this@LoginActivity, "Hubungi Admin untuk melakukan Reset Password")
            }
        }
    }

    private fun authentication(){
        if(etUsername.text.isNullOrEmpty() || etPassword.text.isNullOrEmpty()){
            showAlert(this@LoginActivity, getString(R.string.isianbelumlengkap))
            return
        }
        if(UtilsPref.loadString("firebaseToken") == ""){
            progress.show(this@LoginActivity)
            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    progress.dismiss()
                    if (!task.isSuccessful) {
                        showAlert(this@LoginActivity, task.exception?.message.toString())
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    val token = task.result?.token
                    token?.let { fToken ->
                        UtilsPref.saveString("firebaseToken", fToken)
                    }
                    auth()
                })
        }else{
            auth()
        }
    }

    private fun auth(){
        val request = LoginRequest(etUsername.text.toString(), etPassword.text.toString(), UtilsPref.loadString("firebaseToken"))
        viewModel.auth(request)
    }

    private fun initView(){
        viewModel.showLoading.observe(this, Observer {
            if(it == null) return@Observer
            if(it){
                progress.show(this@LoginActivity)
            }else{
                progress.dismiss()
            }
        })

        viewModel.loginResponse.observe(this, Observer {
            if(it == null) return@Observer
            when (it.rC) {
                "0000" -> {
                    toMainMenu(it.dATA?.rOLENAME.toString())
                }
                "9999" -> {
                    startActivity<KonfirmasiAkunActivity>("data" to it.rCM.toString())
                }
                else -> {
                    showAlert(this@LoginActivity, it.rCM.toString())
                }
            }
        })

        viewModel.showError.observe(this, Observer {
            if(it == null) return@Observer
            showalertInformation(this@LoginActivity, it){
                viewModel.showError.postValue(null)
            }
        })
    }

    private fun toMainMenu(role: String){
        when(role) {
            "local_partner" -> {
                startActivity<LpMainActivity>()
            }
            "mitra", "grosir" -> {
                startActivity<MainActivity>()
            }
            "canvasser", "spg", "msr" -> {
                startActivity<CanvasMainActivity>()
            }
            else -> {
                showAlert(this@LoginActivity, getString(R.string.usertidaktersedia))
                return
            }
        }
        viewModel.savePref(this@LoginActivity)
        finish()
    }

    private fun initDataTest(){
        findViewById<EditText>(R.id.etUsername).setText("085714862128")
    }
}