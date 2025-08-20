package com.hastaprimasolusi.rana.ui.canvasser.penjualan

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.adapter.page.KeranjangAdapter
import com.hastaprimasolusi.rana.data.network.response.order.CartProdukModel
import com.hastaprimasolusi.rana.helper.LocationHelper
import com.hastaprimasolusi.rana.ui.canvasser.CanvasViewModel
import com.hastaprimasolusi.rana.ui.common.ProgDialog
import com.hastaprimasolusi.rana.ui.qr.QrScanActivity
import com.hastaprimasolusi.rana.utils.*
import kotlinx.android.synthetic.main.dialog_cart_sukses.*
import kotlinx.android.synthetic.main.dialog_konfirmasi.*
import kotlinx.android.synthetic.main.dialog_konfirmasi.txtHeader
import kotlinx.android.synthetic.main.fragment_canvas_cart.*
import kotlinx.android.synthetic.main.fragment_daftar_mitra.*
import org.koin.android.ext.android.inject

/**
 * Created By maasrahman on 6/7/20
 */
class PosDetailFragment : Fragment() {
    private val viewModel: CanvasViewModel by inject()
    private lateinit var adapter: KeranjangAdapter
    private val progress = ProgDialog().getInstance()
    private lateinit var locationHelper: LocationHelper
    private var selectedOption: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_canvas_cart, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        locationHelper = LocationHelper(requireActivity())
        adapter =
            KeranjangAdapter({ model, jml ->
                viewModel.updatePos(model, jml)
            }, { model ->
                val dialog = Dialog(activity!!)
                dialog.setContentView(R.layout.dialog_konfirmasi)
                dialog.txtHeader.text = getString(R.string.yakinmenghapusproduk)
                dialog.btnYa.setOnClickListener {
                    viewModel.deletePos(model)
                    dialog.dismiss()
                }
                dialog.btnTidak.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            })

        recyclerKeranjang.layoutManager = LinearLayoutManager(activity)
        recyclerKeranjang.itemAnimator = DefaultItemAnimator()
        recyclerKeranjang.adapter = adapter

        initViewModel()
        btnProses.setOnClickListener {
            if (selectedOption != "") {
                viewModel.confirmPosSpg(
                    name = etNameSpg.text.toString(),
                    owner = etOwnerSpg.text.toString(),
                    phone = etPhoneSpg.text.toString(),
                    option = selectedOption
                ) {
                    jualBerhasil()
                }
                formSpg.visibility = View.GONE
            }
        }

        radioGroupActivity.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.radioP) {
                selectedOption = "1"
                radioP.isChecked = true
            }
            if (checkedId == R.id.radioD) {
                selectedOption = "2"
                radioD.isChecked = true
            }
            if (checkedId == R.id.radioS) {
                selectedOption = "3"
                radioS.isChecked = true
            }
        }
        btnClose.setOnClickListener {
            formSpg.visibility = View.GONE
        }
        btnBayar.text = "Konfirmasi"
        lblMitra.visibility = View.VISIBLE
        txtNamaMitra.visibility = View.VISIBLE
        lblMitra.text = viewModel.selectedTokoJual?.nAME
        txtNamaMitra.text = viewModel.selectedTokoJual?.aDDRESS
        btnBayar.setOnClickListener {
            locationHelper.getLastKnownLocation { location ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    val address = locationHelper.getAddressFromLocation(latitude, longitude)
                    viewModel.setLocationAddress(location, address)
//                    Toast.makeText(requireContext(), "Latitude: $latitude, Longitude: $longitude, Address: $address", Toast.LENGTH_SHORT).show()

                } else {
//                    Toast.makeText(requireContext(), "Location not found", Toast.LENGTH_SHORT).show()
                }

                showalertConfirmation(activity!!, getString(R.string.yakinmelanjutkan)) {
//                startActivityForResult(Intent(activity!!, QrScanActivity::class.java), 888)

                    if (viewModel.userModel?.rOLENAME == "spg" || viewModel.userModel?.rOLENAME == "msr") {
                        formSpg.visibility = View.VISIBLE
                    } else {
                        viewModel.confirmPos(viewModel.selectedTokoJual?.iD.toString()) {
                            jualBerhasil()
                        }
                    }
                }

            }

        }
    }

    private fun jualBerhasil() {
        val listPost = viewModel.listPos.value
        listPost?.clear()
        viewModel.listPos.postValue(listPost)

        val dialog = Dialog(activity!!)
        dialog.setContentView(R.layout.dialog_cart_sukses)
        dialog.txtHeader.text = "Penjualan berhasil"
        dialog.btnOk.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack(
                null,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 888 && resultCode == RESULT_OK) {
            val bundle = data?.extras
            if (bundle != null) {
                val id = bundle.getString("id")
                viewModel.confirmPos(id.toString()) {
                    jualBerhasil()
                }
            } else {
                showAlert(activity!!, "Ulangi QR Scanner")
            }
        }
    }

    private fun updateSummary(data: List<CartProdukModel>) {
        layoutSummary.visibility = View.VISIBLE
        var jml = 0
        var jmlItem = 0
        data.forEach {
            it.pRODPRICE?.let { price ->
                jmlItem += price.first().pRODQTY.toString().toIntOrNull() ?: 0
                jml += (price.first().pRODQTY.toString().toIntOrNull()
                    ?: 0) * (price.first().pRODPRICE.toString().toIntOrNull() ?: 0)
            }
        }
        txtJmlItem.text = convertCurrencyNo(jmlItem.toString(), 3, '.')
        txtTotal.text = convertCurrency(
            jml.toString(),
            3,
            '.',
            UtilsPref.loadString(getString(R.string.currencySymbol))
        )
    }

    private fun initViewModel() {
        if (adapter.itemCount == 0) {
            viewModel.getPos()
        }

        viewModel.loadingPos.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            swipe.isRefreshing = it
        })

        viewModel.listPos.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
//            println("Summary")
            updateSummary(it)
            adapter.updateData(it)
        })

        viewModel.loadingCheckOut.observe(viewLifecycleOwner, Observer {
            if (it) progress.show(activity!!) else progress.dismiss()
        })

        swipe.setOnRefreshListener {
            viewModel.getPos()
        }
    }
}