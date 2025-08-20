package com.hastaprimasolusi.rana.ui.mitra.keranjang.chekout

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.data.network.response.order.PaymentModel
import com.hastaprimasolusi.rana.utils.UtilsPref
import com.hastaprimasolusi.rana.utils.convertCurrency
import com.hastaprimasolusi.rana.utils.showalertInformation
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_detail_pembayaran.*
import org.jetbrains.anko.toast
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created By maasrahman on 6/19/20
 */
class DetailPembayaranActivity: AppCompatActivity() {
    var cTimer: CountDownTimer? = null
    var modelPembayaran: PaymentModel? = null
    var imageBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_pembayaran)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Detail Pembayaran"
        initData()
    }

    private fun initData(){
        val bundle = intent.extras
        if(bundle != null){
            modelPembayaran = bundle.getParcelable("model")
            var sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            try{
                val dateExpired = sdf.parse(modelPembayaran?.tRXPAYMENTEXPIRED.toString())
                sdf = SimpleDateFormat("dd MMM yyyy HH:mm")
                txtCountDown.text = sdf.format(dateExpired)
//                if(dateExpired != null){
//                    val diff = dateExpired.time - Date().time
//                    countDownTimer(diff)
//                }else{
//                    countDownTimer(3600000)
//                }
            }catch (e: Exception){
                txtCountDown.text = "Undefined"
            }
            btnCopy.setOnClickListener(listener)
            btnSimpan.setOnClickListener(listener)

            val tagihan = (modelPembayaran?.tRXAMOUNT?.toInt() ?: 0) + (modelPembayaran?.tRXADMIN?.toInt() ?: 0)
            txtTagihan.text = convertCurrency(tagihan.toString(), 3, '.', UtilsPref.loadString(getString(R.string.currencySymbol)))

            if(modelPembayaran?.tRXPAYMETHOD == "8"){
                layoutKode.visibility = View.GONE
                layoutQris.visibility = View.VISIBLE
                generateQr(modelPembayaran?.tRXPAYMENTNO.toString())
            }else{
                layoutKode.visibility = View.VISIBLE
                layoutQris.visibility = View.GONE
                txtKode.text = modelPembayaran?.tRXPAYMENTNO
            }

        }else{
            showalertInformation(this, getString(R.string.terjadikesalahansaatmemuatdata)){
                finish()
            }
        }
    }

    private fun countDownTimer(millis: Long){
        cTimer = object : CountDownTimer(millis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                var valueMillis = millisUntilFinished
                val secondsInMilli: Long = 1000
                val minutesInMilli = secondsInMilli * 60
                val hoursInMilli = minutesInMilli * 60

                val elapsedHours = valueMillis / hoursInMilli
                valueMillis %= hoursInMilli

                val elapsedMinutes = valueMillis / minutesInMilli
                valueMillis %= minutesInMilli

                val elapsedSeconds = valueMillis / secondsInMilli

                val yy = String.format("%02d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds)
                txtCountDown.text = yy
            }

            override fun onFinish() {

            }
        }.start()
    }

    private val listener = View.OnClickListener { view ->
        when(view.id){
            R.id.btnCopy -> {
                val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Text Copy", txtKode.text)
                clipboard.setPrimaryClip(clip)
            }
            R.id.btnSimpan -> {
                checkPermission()
            }
        }
    }

    private fun generateQr(id: String){
        val multiFormat = MultiFormatWriter()
        try {
            val bitMatrix = multiFormat.encode(id, BarcodeFormat.QR_CODE,400,400)
            val barcodeEncoder = BarcodeEncoder()
            imageBitmap = barcodeEncoder.createBitmap(bitMatrix)
            imgQR.setImageBitmap(imageBitmap)
        } catch (e: Exception) {

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkPermission(){
        Dexter.withActivity(this)
            .withPermission(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    captureScreen()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    showSettingsDialog()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }).check()
    }

    private fun showSettingsDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Permission")
        builder.setMessage("Permission Penyimpanan dibutuhkan agar aplikasi berjalan baik, aktifkan di setting aplikasi.")
        builder.setPositiveButton("SETTING"
        ) { dialog, _ ->
            dialog.cancel()
            openSettings()
        }
        builder.setNegativeButton("Batal"
        ) { dialog, which -> dialog.cancel() }
        builder.show()
    }
    private fun openSettings() {
        val intent: Intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", "com.hastaprimasolusi.rana", null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    private fun captureScreen() {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "DMLT_" + timeStamp + "_"
        val storageDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS)
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }
        val file = File(storageDir, "$imageFileName.jpg")
        try {
            val fOut = FileOutputStream(file)
            imageBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
            fOut.flush()
            fOut.close()
            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, "Transaksi QRIS")
            values.put(MediaStore.Images.Media.DESCRIPTION, imageFileName)
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            values.put(MediaStore.MediaColumns.DATA, file.path)
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            toast("QR Code telah disimpan")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        cTimer?.cancel()
        super.onDestroy()
    }
}