package com.hastaprimasolusi.rana.ui.akun

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.data.network.requesthelper.RegisterRequest
import com.hastaprimasolusi.rana.ui.canvasser.CanvasViewModel
import com.hastaprimasolusi.rana.ui.common.ProgDialog
import com.hastaprimasolusi.rana.ui.lp.LpViewModel
import com.hastaprimasolusi.rana.ui.mitra.MitraViewModel
import com.hastaprimasolusi.rana.utils.UtilsPref
import com.hastaprimasolusi.rana.utils.showAlert
import kotlinx.android.synthetic.main.dialog_cart_sukses.*
import kotlinx.android.synthetic.main.fragment_ubah_profile.*
import org.koin.android.ext.android.inject

/**
 * Created By maasrahman on 7/22/20
 */
class UbahProfileFragment: Fragment() {
    private val profileModel: ProfileViewModel by inject()
    private val mitraModel: MitraViewModel by inject()
    private val canvasModel: CanvasViewModel by inject()
    private val lpModel: LpViewModel by inject()
    private var isCanvas = false
    private val progress = ProgDialog().getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.ubahprofile)
        return inflater.inflate(R.layout.fragment_ubah_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mitraModel.userModel?.let {
            isCanvas = false
            etNama.setText(it.nAME)
            etNamaPemilik.setText(it.oWNERNAME)
            etAlamat.setText(it.aLAMAT)
        }
        canvasModel.userModel?.let {
            isCanvas = true
            layoutNama.visibility = View.GONE
            layoutNama.hint = "Nama"
            etNamaPemilik.setText(it.nAME)
            etAlamat.setText(it.aLAMAT)
        }
        lpModel.userModel?.let {
            isCanvas = false
            etNama.setText(it.nAME)
            etNamaPemilik.setText(it.oWNERNAME)
            etAlamat.setText(it.aLAMAT)
        }

        btnUbah.setOnClickListener {
            when{
                etNama.text.isNullOrEmpty() && !isCanvas -> etNama.error = getString(R.string.wajibdiisi)
                etNamaPemilik.text.isNullOrEmpty() -> etNamaPemilik.error = getString(R.string.wajibdiisi)
                etAlamat.text.isNullOrEmpty() -> etAlamat.error = getString(R.string.wajibdiisi)
                else -> {
                    updateProfile()
                }
            }
        }

        profileModel.loadingProgress.observe(viewLifecycleOwner, Observer {
            if(it) progress.show(activity!!) else progress.dismiss()
        })

        profileModel.showError.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            showAlert(activity!!, it)
            profileModel.showError.postValue(null)
        })
    }

    private fun updateProfile(){
        val request = RegisterRequest(nAMA = etNamaPemilik.text.toString(), nAMATOKO = etNama.text.toString(),
            aLAMAT = etAlamat.text.toString())
        profileModel.updateProfile(request){
            val dialog = Dialog(activity!!)
            dialog.setContentView(R.layout.dialog_cart_sukses)
            dialog.txtHeader.text = "Perubahan profile berhasil"
            dialog.btnOk.setOnClickListener {
                when {
                    lpModel.userModel != null -> {
                        lpModel.userModel?.nAME = request.nAMA
                        lpModel.userModel?.oWNERNAME = request.nAMATOKO
                        lpModel.userModel?.aLAMAT = request.aLAMAT
                        UtilsPref.saveString(getString(R.string.userData), Gson().toJson(lpModel.userModel))
                    }
                    canvasModel.userModel != null -> {
                        canvasModel.userModel?.nAME = request.nAMA
                        canvasModel.userModel?.oWNERNAME = request.nAMATOKO
                        canvasModel.userModel?.aLAMAT = request.aLAMAT
                        UtilsPref.saveString(getString(R.string.userData), Gson().toJson(canvasModel.userModel))
                    }
                    else -> {
                        mitraModel.userModel?.nAME = request.nAMA
                        mitraModel.userModel?.oWNERNAME = request.nAMATOKO
                        mitraModel.userModel?.aLAMAT = request.aLAMAT
                        UtilsPref.saveString(getString(R.string.userData), Gson().toJson(mitraModel.userModel))
                    }
                }
                activity?.finish()
                dialog.dismiss()
            }
            dialog.show()
        }
    }
}