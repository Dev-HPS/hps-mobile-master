package com.hastaprimasolusi.rana.ui.qr

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.ResultPoint
import com.google.zxing.integration.android.IntentIntegrator
import com.hastaprimasolusi.rana.R
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import kotlinx.android.synthetic.main.activity_qr_scan.*


/**
 * Created By maasrahman on 6/20/20
 */
class QrScanActivity: AppCompatActivity() {
    var isFlash = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_scan)

        btnCancel.setOnClickListener(listener)
        flashLight.setOnClickListener(listener)
        if (packageManager?.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH) == false) {
            flashLight.visibility = View.GONE
        }
        initScan()
    }

    private fun initScan(){
        val integrator = IntentIntegrator(this)
        integrator.setOrientationLocked(false)
        integrator.setPrompt("")
        integrator.captureActivity = QrScanActivity::class.java
        barcodeview.decodeContinuous(callback)
        barcodeview.setStatusText("")
        barcodeview.initializeFromIntent(integrator.createScanIntent())
    }

    private val listener = View.OnClickListener { v ->
        when (v.id) {
            R.id.btnCancel -> {
                onBackPressed()
            }
            R.id.flashLight -> setFlash()
        }
    }

    private fun setFlash() {
        isFlash = !isFlash
        if (isFlash) {
            barcodeview.setTorchOn()
        } else {
            barcodeview.setTorchOff()
        }
    }

    override fun onResume() {
        super.onResume()
        barcodeview.resume()
    }

    override fun onPause() {
        super.onPause()
        barcodeview.pause()
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    private val callback: BarcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            if (result.text != null) {
                val intent = Intent()
                intent.putExtra("id", result.text)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            val intent = Intent()
            intent.putExtra("id", result.contents)
            setResult(Activity.RESULT_OK, intent)
            finish()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}