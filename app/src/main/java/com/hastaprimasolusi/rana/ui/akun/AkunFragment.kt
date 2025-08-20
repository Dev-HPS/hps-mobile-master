package com.hastaprimasolusi.rana.ui.akun

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.data.network.requesthelper.PhotoRequest
import com.hastaprimasolusi.rana.ui.CommonViewModel
import com.hastaprimasolusi.rana.ui.common.ProgDialog
import com.hastaprimasolusi.rana.ui.login.LoginActivity
import com.hastaprimasolusi.rana.ui.mitra.MitraViewModel
import com.hastaprimasolusi.rana.ui.report.transaksi.TransReportActivity
import com.hastaprimasolusi.rana.utils.*
import com.journeyapps.barcodescanner.BarcodeEncoder
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import kotlinx.android.synthetic.main.dialog_imagechooser.*
import kotlinx.android.synthetic.main.fragment_akun.*
import kotlinx.android.synthetic.main.fragment_akun.imgProfile
import kotlinx.android.synthetic.main.fragment_akun.txtAccountNo
import kotlinx.android.synthetic.main.fragment_akun.txtAlamat
import kotlinx.android.synthetic.main.fragment_akun.txtEmail
import kotlinx.android.synthetic.main.fragment_akun.txtNama
import kotlinx.android.synthetic.main.fragment_akun.txtPemilik
import kotlinx.android.synthetic.main.fragment_akun.txtTelepon
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.support.v4.startActivity
import org.koin.android.ext.android.inject
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.system.exitProcess

/**
 * Created By maasrahman on 2020-04-26
 */
class AkunFragment: Fragment() {
    private val viewModel: MitraViewModel by inject()
    private val common: CommonViewModel by inject()
    private var base64Image = ""
    private val progress = ProgDialog().getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_akun, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(viewModel.userModel?.rOLENAME == "grosir"){
            layoutPoin.visibility = View.GONE
        }

        showQR.setOnClickListener(listener)
        imgClose.setOnClickListener(listener)
        ubahProfile.setOnClickListener(listener)
        ubahPassword.setOnClickListener(listener)
        logout.setOnClickListener(listener)
        laporanTrans.setOnClickListener(listener)
        imgProfile.setOnClickListener(listener)
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    private fun initData(){
        common.loadingPhoto.observe(viewLifecycleOwner, Observer {
            if(it) progress.show(activity!!) else progress.dismiss()
        })

        viewModel.loadingProfile.observe(viewLifecycleOwner, Observer {
            if (it) {
                progressPoin.visibility = View.VISIBLE
            } else {
                progressPoin.visibility = View.GONE
            }
        })

        viewModel.getProfile({
            val poin = (it.uSERACCOUNTINFO?.aCCOUNTPOINT?.toIntOrNull() ?: 0).toString()
            txtAccountNo.text = "No. Akun ${it.uSERACCOUNTINFO?.aCCOUNTNUMBER}"
            txtPoin.text = converNumber(poin, 3, '.')
            txtNama.text = it.uSERDISPLAYNAME
            txtMitra.text = "Login terakhir ${
                convertDateTime(
                it.uSERLASTLOGINAPP.toString(),
                "dd MMM yyyy HH:mm"
            )
            }"
        }, {
            showAlert(activity!!, it)
        })

        viewModel.userModel?.let { user ->
            Glide.with(this)
                .load(user.pICTURE)
                .apply(RequestOptions().error(R.drawable.ic_store).circleCrop())
                .into(imgProfile)

            txtNama.text = user.nAME
            txtMitra.text = "Login terakhir ${convertDateTimeZone(
                user.lASTLOGIN.toString(),
                "dd MMM yyyy HH:mm"
            )}"
            txtPemilik.text = user.oWNERNAME
            txtTelepon.text = user.pHONE
            txtEmail.text = user.eMAIL
            txtAlamat.text = "${user.aLAMAT} ${user.dESANAME.toString().toLowerCase().capitalizeWords()} " +
                    "${user.kECNAME.toString().toLowerCase().capitalizeWords()} ${user.kOTAKABNAME.toString().toLowerCase().capitalizeWords()} " +
                    "${user.pROVNAME.toString().toLowerCase().capitalizeWords()}"
            generateQr(user.iD.toString())
        }
    }

    private val listener = View.OnClickListener { view ->
        when(view.id){
            R.id.imgClose -> {
                slideDown(layoutQR)
            }
            R.id.showQR -> {
                slideUp(layoutQR)
            }
            R.id.ubahProfile -> {
                startActivity<ProfileActivity>("type" to "ubah")
            }
            R.id.ubahPassword -> {
                startActivity<ProfileActivity>("type" to "password")
            }
            R.id.logout -> {
                showalertConfirmation(activity!!, getString(R.string.yakinkeluar)) {
                    viewModel.clearData()
                    UtilsPref.saveBoolean(getString(R.string.isLoggedIn), false)
                    startActivity<LoginActivity>()
                    activity?.finish()
                    exitProcess(0)
                }
            }
            R.id.laporanTrans -> {
                startActivity<TransReportActivity>()
            }
            R.id.imgProfile -> {
                showDialogImage()
            }
        }
    }

    private fun showDialogImage(){
        val dialog = Dialog(activity!!, R.style.DialogBounceAnim)
        dialog.setContentView(R.layout.dialog_imagechooser)
        dialog.setCanceledOnTouchOutside(true)
        dialog.btnCamera.setOnClickListener {
            var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, 88)
            dialog.dismiss()
        }
        dialog.btnGallery.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 8)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun generateQr(id: String){
        val multiFormat = MultiFormatWriter()
        try {
            val bitMatrix = multiFormat.encode(id, BarcodeFormat.QR_CODE, 400, 400)
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.createBitmap(bitMatrix)
            imgQR.setImageBitmap(bitmap)
        } catch (e: Exception) {

        }
    }

    private fun uploadImage(){
        val request = PhotoRequest(iMG = base64Image)
        common.updateProfile(requireContext(), request, {
            Glide.with(this)
                .load(Base64.decode(base64Image, Base64.DEFAULT))
                .apply(RequestOptions().error(R.drawable.no_image).circleCrop())
                .into(imgProfile)
            viewModel.userModel = it
            showAlert(activity!!, "Update Photo berhasil!")
        }, {
            showAlert(activity!!, it)
            Glide.with(this)
                .load(viewModel.userModel?.pICTURE)
                .apply(RequestOptions().error(R.drawable.ic_store).circleCrop())
                .into(imgProfile)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 8 && resultCode == Activity.RESULT_OK) {
            val path = ImageFilePath.getPath(activity!!, data?.data)
            GlobalScope.launch {
                val compressedImageFile = Compressor.compress(activity!!, File(path)) {
                    default(width = 640, format = Bitmap.CompressFormat.JPEG, quality = 75)
                }
                val bytes = compressedImageFile.readBytes()
                base64Image = Base64.encodeToString(bytes, Base64.DEFAULT)
                uploadImage()
//                withContext(Dispatchers.Main){
//                    Glide.with(activity!!)
//                        .load(compressedImageFile)
//                        .apply(RequestOptions().error(R.drawable.no_image).circleCrop())
//                        .into(imgProfile)
//                }
            }
        } else if (requestCode == 88 && resultCode == Activity.RESULT_OK) {
            var baos = ByteArrayOutputStream()
            val bm = data?.extras?.get("data") as Bitmap
            try {
                bm.compress(Bitmap.CompressFormat.JPEG, 75, baos)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    val bytes = baos.toByteArray()
                    base64Image = Base64.encodeToString(bytes, Base64.DEFAULT)
                    uploadImage()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}