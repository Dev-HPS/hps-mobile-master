package com.hastaprimasolusi.rana.ui.canvasser.penjualan

import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.gson.Gson
import com.google.zxing.ResultPoint
import com.google.zxing.integration.android.IntentIntegrator
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.ui.canvasser.CanvasViewModel
import com.hastaprimasolusi.rana.ui.common.ProgDialog
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import kotlinx.android.synthetic.main.dialog_cart_sukses.*
import kotlinx.android.synthetic.main.fragment_pos_scan.*
import org.koin.android.ext.android.inject

/**
 * Created By maasrahman on 6/7/20
 */
class PosScanFragment: Fragment() {
    private val viewModel: CanvasViewModel by inject()
    var isFlash = false
    private val progress = ProgDialog().getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pos_scan, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val intentIntegrator = IntentIntegrator.forSupportFragment(this)
        barcodeview.initializeFromIntent(intentIntegrator.createScanIntent())
        barcodeview.decodeContinuous(callback)
        btnCancel.setOnClickListener(listener)
        flashLight.setOnClickListener(listener)
        if (activity?.packageManager?.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH) == false) {
            flashLight.visibility = View.GONE
        }
//        initViewModel()
    }

    private val listener = View.OnClickListener { v ->
        when (v.id) {
            R.id.btnCancel -> activity?.supportFragmentManager?.popBackStack()
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

    private val callback: BarcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            if (result.text != null) {
                barcodeview.pause()

            }
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }

}