package com.hastaprimasolusi.rana.ui.akun

import android.app.Dialog
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.data.network.requesthelper.PasswordRequest
import com.hastaprimasolusi.rana.ui.common.ProgDialog
import com.hastaprimasolusi.rana.utils.showAlert
import kotlinx.android.synthetic.main.dialog_cart_sukses.*
import kotlinx.android.synthetic.main.fragment_ubah_password.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created By maasrahman on 2020-05-02
 */
class UbahPasswordFragment: Fragment() {
    private val viewModel: ProfileViewModel by viewModel()
    private val progress = ProgDialog().getInstance()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.ubahpassword)
        return inflater.inflate(R.layout.fragment_ubah_password, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        checkShow.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                etPassword.transformationMethod = null
                etPasswordBaru.transformationMethod = null
                etPasswordRetype.transformationMethod = null
            }else{
                etPassword.transformationMethod = PasswordTransformationMethod()
                etPasswordBaru.transformationMethod = PasswordTransformationMethod()
                etPasswordRetype.transformationMethod = PasswordTransformationMethod()
            }
        }
        btnUbah.setOnClickListener {
            when{
//                etPassword.text.isNullOrEmpty() -> etPassword.error = getString(R.string.wajibdiisi)
                etPasswordBaru.text.isNullOrEmpty() -> etPasswordBaru.error = getString(R.string.wajibdiisi)
                etPasswordRetype.text.isNullOrEmpty() -> etPasswordRetype.error = getString(R.string.wajibdiisi)
                etPasswordBaru.text.toString() != etPasswordRetype.text.toString() -> {
                    showAlert(activity!!, "Password tidak sama")
                }
                else -> {
                    changePassword()
                }
            }
        }

        initViewModel()
    }

    private fun changePassword(){
        val request = PasswordRequest()
        request.pASSWORDBARU = etPasswordBaru.text.toString()
        request.rEPASSWORDBARU = etPasswordRetype.text.toString()
        viewModel.updatePassword(request){
            val dialog = Dialog(activity!!)
            dialog.setContentView(R.layout.dialog_cart_sukses)
            dialog.txtHeader.text = "Perubahan password berhasil"
            dialog.btnOk.setOnClickListener {
                activity?.finish()
            }
            dialog.show()
        }
    }

    private fun initViewModel(){
        viewModel.loadingProgress.observe(viewLifecycleOwner, Observer {
            if(it) progress.show(activity!!)
            else progress.dismiss()
        })

        viewModel.showError.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            showAlert(activity!!, it)
            viewModel.showError.postValue(null)
        })
    }
}