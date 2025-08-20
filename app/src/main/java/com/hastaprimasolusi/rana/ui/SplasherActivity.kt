package com.hastaprimasolusi.rana.ui

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.data.network.response.UserModel
import com.hastaprimasolusi.rana.ui.canvasser.CanvasMainActivity
import com.hastaprimasolusi.rana.ui.login.LoginActivity
import com.hastaprimasolusi.rana.ui.lp.LpMainActivity
import com.hastaprimasolusi.rana.ui.mitra.MainActivity
import com.hastaprimasolusi.rana.utils.UtilsPref
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

/**
 * Created By maasrahman on 2020-04-25
 */
class SplasherActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splasher)

        var permission = arrayListOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CAMERA
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permission.add(Manifest.permission.READ_MEDIA_IMAGES)
            permission.add(Manifest.permission.READ_MEDIA_VIDEO)
            permission.add(Manifest.permission.POST_NOTIFICATIONS)
        }else{
            permission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            permission.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        Handler(Looper.getMainLooper()).postDelayed({
            Dexter.withActivity(this@SplasherActivity)
                .withPermissions(
                    permission
                )
                .withListener(object: MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {
                            if(report.areAllPermissionsGranted()){
                                launchApp()
                            }
                        }
                    }
                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        token?.continuePermissionRequest()
                    }
                })
                .withErrorListener {
                    toast(it.name)
                }
                .check()
        }, 2000)
    }

    private fun launchApp(){
        if(UtilsPref.loadBoolean(getString(R.string.isLoggedIn))){
            val userModel = Gson().fromJson(UtilsPref.loadString(getString(R.string.userData)), UserModel::class.java)
            when(userModel.rOLENAME){
                "local_partner" -> {
                    startActivity<LpMainActivity>()
                }
                "canvasser", "spg", "msr" -> {
                    startActivity<CanvasMainActivity>()
                }
                else -> {
                    startActivity<MainActivity>()
                }
            }
        }else{
            startActivity<LoginActivity>()
        }
        finish()
    }
}