package com.hastaprimasolusi.rana.ui.canvasser.akun

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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.data.network.requesthelper.PhotoRequest
import com.hastaprimasolusi.rana.ui.CommonViewModel
import com.hastaprimasolusi.rana.ui.akun.ProfileActivity
import com.hastaprimasolusi.rana.ui.canvasser.CanvasViewModel
import com.hastaprimasolusi.rana.ui.common.ProgDialog
import com.hastaprimasolusi.rana.ui.report.ReportViewModel
import com.hastaprimasolusi.rana.utils.*
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import kotlinx.android.synthetic.main.dialog_imagechooser.*
import kotlinx.android.synthetic.main.fragment_canvas_profile.*
import kotlinx.android.synthetic.main.fragment_canvas_profile.imgProfile
import kotlinx.android.synthetic.main.fragment_canvas_profile.txtAccountNo
import kotlinx.android.synthetic.main.fragment_canvas_profile.txtAlamat
import kotlinx.android.synthetic.main.fragment_canvas_profile.txtEmail
import kotlinx.android.synthetic.main.fragment_canvas_profile.txtNama
import kotlinx.android.synthetic.main.fragment_canvas_profile.txtPemilik
import kotlinx.android.synthetic.main.fragment_canvas_profile.txtTelepon
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.support.v4.startActivity
import org.koin.android.ext.android.inject
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.math.roundToInt

/**
 * Created By maasrahman on 2020-05-02
 */
class CanvasProfileFragment: Fragment() {
    private val viewModel: CanvasViewModel by inject()
    private val reportModel: ReportViewModel by inject()
    private val common: CommonViewModel by inject()
    private var base64Image = ""
    private val progress = ProgDialog().getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.profile)
        return inflater.inflate(R.layout.fragment_canvas_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btnUbah.setOnClickListener(listener)
        btnUbahPass.setOnClickListener(listener)
        btnTarikSaldo.setOnClickListener(listener)
        imgProfile.setOnClickListener(listener)
    }

    private val listener = View.OnClickListener {view ->
        when(view.id){
            R.id.btnUbah -> {
                startActivity<ProfileActivity>("type" to "ubah")
            }
            R.id.btnUbahPass -> {
                startActivity<ProfileActivity>("type" to "pass")
            }
            R.id.btnTarikSaldo -> {
                showalertConfirmation(activity!!, getString(R.string.cairkandanapadaaplikasi)){
                    cairkan()
                }
            }
            R.id.imgProfile -> {
                showDialogImage()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        initViewModel()
    }

    private fun cairkan(){
        reportModel.cairkanDana({
            showalertInformation(activity!!, it){}
        }, {
            showAlert(activity!!, it)
        })
    }

    private fun initViewModel(){
        common.loadingPhoto.observe(viewLifecycleOwner, Observer {
            if(it) progress.show(activity!!) else progress.dismiss()
        })

        viewModel.loadingProfile.observe(viewLifecycleOwner, Observer {
            if(it){
                progressSaldo.visibility = View.VISIBLE
            }else{
                progressSaldo.visibility = View.GONE
            }
        })

        viewModel.getProfile({
            if(it.uSERACCOUNTINFO?.aCCOUNTBALANCE.isNullOrEmpty()){
                txtSaldo.text = "${UtilsPref.loadString(getString(R.string.currencySymbol))} 0"
            }else{
                val saldo = it.uSERACCOUNTINFO?.aCCOUNTBALANCE?.toDouble()?.roundToInt() ?: 0
                txtSaldo.text = convertCurrency(saldo.toString(), 3, '.',
                    UtilsPref.loadString(getString(R.string.currencySymbol)))
            }
            txtAccountNo.text = "No. Akun ${it.uSERACCOUNTINFO?.aCCOUNTNUMBER}"
            txtNama.text = it.uSERDISPLAYNAME
        }, {
            showAlert(activity!!, it)
        })

        viewModel.userModel?.let { user ->
            Glide.with(this)
                .load(user.pICTURE)
                .apply(RequestOptions().error(R.drawable.ava_profile).circleCrop())
                .into(imgProfile)
            txtNama.text = user.nAME
            txtPemilik.text = user.oWNERNAME
            txtTelepon.text = user.pHONE
            txtEmail.text = user.eMAIL
            txtAlamat.text = "${user.aLAMAT} ${user.dESANAME.toString().toLowerCase().capitalizeWords()} " +
                    "${user.kECNAME.toString().toLowerCase().capitalizeWords()} ${user.kOTAKABNAME.toString().toLowerCase().capitalizeWords()} " +
                    "${user.pROVNAME.toString().toLowerCase().capitalizeWords()}"
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

    private fun uploadImage(){
        val request = PhotoRequest(iMG = base64Image)
        common.updateProfile(requireContext(), request, {
            Glide.with(this)
                .load(Base64.decode(base64Image, Base64.DEFAULT))
                .apply(RequestOptions().error(R.drawable.no_image).circleCrop())
                .into(imgProfile)
            viewModel.userModel = it
            viewModel.updateImage.postValue(true)
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