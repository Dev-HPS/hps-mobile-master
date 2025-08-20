package com.hastaprimasolusi.rana.ui.lp.order

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.adapter.page.CourierAdapter
import com.hastaprimasolusi.rana.adapter.page.DetailProdukLpAdapter
import com.hastaprimasolusi.rana.adapter.page.StatusPesananAdapter
import com.hastaprimasolusi.rana.adapter.page.StatusPesananLpAdapter
import com.hastaprimasolusi.rana.data.network.requesthelper.ApproveProduct
import com.hastaprimasolusi.rana.data.network.requesthelper.ApproveRequest
import com.hastaprimasolusi.rana.data.network.requesthelper.DeliverRequest
import com.hastaprimasolusi.rana.data.network.response.DeliveryCourierModel
import com.hastaprimasolusi.rana.data.network.response.order.HistoryDetailProdModel
import com.hastaprimasolusi.rana.data.network.response.order.OrderProdModel
import com.hastaprimasolusi.rana.data.network.response.order.PaymentModel
import com.hastaprimasolusi.rana.ui.common.ProgDialog
import com.hastaprimasolusi.rana.ui.lp.LpViewModel
import com.hastaprimasolusi.rana.ui.mitra.keranjang.chekout.DetailPembayaranActivity
import com.hastaprimasolusi.rana.utils.*
import kotlinx.android.synthetic.main.dialog_detail_pembayaran.*
import kotlinx.android.synthetic.main.dialog_info.*
import kotlinx.android.synthetic.main.dialog_info.txtHeader
import kotlinx.android.synthetic.main.dialog_pembatalan.*
import kotlinx.android.synthetic.main.fragment_order_detail.*
import kotlinx.android.synthetic.main.fragment_order_detail.btnCekStatus
import kotlinx.android.synthetic.main.fragment_order_detail.imgClose
import kotlinx.android.synthetic.main.fragment_order_detail.layoutTipeBayar
import kotlinx.android.synthetic.main.fragment_order_detail.progressProduk
import kotlinx.android.synthetic.main.fragment_order_detail.progressStatus
import kotlinx.android.synthetic.main.fragment_order_detail.recyclerProduk
import kotlinx.android.synthetic.main.fragment_order_detail.recyclerStatus
import kotlinx.android.synthetic.main.fragment_order_detail.txtBiaya
import kotlinx.android.synthetic.main.fragment_order_detail.txtHarga
import kotlinx.android.synthetic.main.fragment_order_detail.txtInvoice
import kotlinx.android.synthetic.main.fragment_order_detail.txtJenisBayar
import kotlinx.android.synthetic.main.fragment_order_detail.txtJmlBarang
import kotlinx.android.synthetic.main.fragment_order_detail.txtStatus
import kotlinx.android.synthetic.main.fragment_order_detail.txtStatusBayar
import kotlinx.android.synthetic.main.fragment_order_detail.txtTanggal
import kotlinx.android.synthetic.main.fragment_order_detail.txtTotalBayar
import org.jetbrains.anko.support.v4.startActivity
import org.koin.android.ext.android.inject

/**
 * Created By maasrahman on 5/20/20
 */
class OrderDetailFragment : Fragment() {
    private val viewModel: LpViewModel by inject()
    private var orderId: String? = null

    private lateinit var adapter: DetailProdukLpAdapter
    private lateinit var adapterStatus: StatusPesananLpAdapter
    private lateinit var adapterCourier: CourierAdapter
    private var listProduk = mutableListOf<OrderProdModel>()
    private val progress = ProgDialog().getInstance()
    private var courierSelected: DeliveryCourierModel? = null
    private var isCanvasser = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_order_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.historyDetail.postValue(null)
        layoutAksi.visibility = View.GONE
        val args = arguments
        if (args != null) {
            orderId = args.getString("id")

            adapterStatus = StatusPesananLpAdapter()
            recyclerStatus.layoutManager = LinearLayoutManager(activity)
            recyclerStatus.itemAnimator = DefaultItemAnimator()
            recyclerStatus.adapter = adapterStatus

            adapterCourier = CourierAdapter {
                courierSelected = it
                etCourier.setText(courierSelected?.dELMETHODNAME)
                layoutDelivery.visibility = View.GONE
            }
            recyclerCourier.layoutManager = LinearLayoutManager(activity)
            recyclerCourier.itemAnimator = DefaultItemAnimator()
            recyclerCourier.adapter = adapterCourier

            initViewModel()
        }

        btnProses.setOnClickListener(listener)
        btnKirim.setOnClickListener(listener)
        btnBatalkan.setOnClickListener(listener)
        btnSelesaikan.setOnClickListener(listener)
        etCourier.setOnClickListener(listener)
        imgClose.setOnClickListener(listener)
        btnCekStatus.setOnClickListener(listener)
        btnDetailBayar.setOnClickListener(listener)
        imgBuktiBayar.setOnClickListener(listener)
        imgBayarClose.setOnClickListener(listener)
//        btnPilihSemua.setOnClickListener(listener)
        etCari.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    private val listener = View.OnClickListener { view ->
        val history = viewModel.historyDetail.value
        when (view.id) {
            R.id.btnProses -> {
                val listProd = mutableListOf<ApproveProduct>()
                listProduk.forEach {
                    if (it.isChecked) {
                        listProd.add(ApproveProduct(it.pRODID, it.oRDERPRODUNIT))
                    }
                }

                if (listProd.size == 0) {
                    showAlert(activity!!, getString(R.string.belumadaprodukterpilih))
                    return@OnClickListener
                }
                showalertConfirmation(activity!!, getString(R.string.pesanandiapprove)) {
                    val request = ApproveRequest()
                    request.tRXPAYMENT = viewModel.buktiBayarImg.value
                    request.oRDERID = history?.oRDERID
                    request.oRDERPRODUCT = listProd
                    history?.let { _ ->
                        viewModel.approvePesanan(request) {
                            if (!isCanvasser) {
                                layoutKirim.visibility = View.VISIBLE
                            }
                            layoutProses.visibility = View.GONE
                        }
                    }
                }
            }

            R.id.etCourier -> {
                dialogSetDelivery()
            }

            R.id.btnKirim -> {
                if (etCourier.text.isNullOrEmpty()) {
                    showAlert(activity!!, getString(R.string.isianbelumlengkap))
                    return@OnClickListener
                }
                showalertConfirmation(
                    activity!!,
                    "Jenis Pengiriman terpilih ${courierSelected?.dELMETHODNAME}, Lanjutkan?"
                ) {
                    val request = DeliverRequest(history?.oRDERNO, courierSelected?.dELMETHODID)
                    viewModel.setCourier(request) {
                        val dialog = Dialog(activity!!)
                        dialog.setContentView(R.layout.dialog_info)
                        dialog.txtHeader.text = "Pilihan pengiriman telah disimpan"
                        dialog.btnOk.setOnClickListener {
                            activity?.supportFragmentManager?.popBackStack()
                            dialog.dismiss()
                        }
                        dialog.show()
                    }
                }
            }

            R.id.imgClose -> {
                layoutDelivery.visibility = View.GONE
            }
//            R.id.btnPilihSemua -> {
//                listProduk.forEach {
//                    it.isChecked = true
//                }
//                adapter.updateData(listProduk)
//            }
            R.id.btnBatalkan -> {
                val dialog = Dialog(activity!!)
                dialog.setContentView(R.layout.dialog_pembatalan)
                dialog.btnSubmitBatal.setOnClickListener {
                    if (dialog.etKeterangan.text.isNullOrEmpty()) {
                        dialog.etKeterangan.error = "Harap diisi"
                        return@setOnClickListener
                    }
                    viewModel.actionBatalkan(history?.oRDERNO.toString()) {
                        if (it.rC == "0000") {
                            actionBatal("Transaksi telah dibatalkan")
                        } else {
                            showAlert(activity!!, it.rCM.toString())
                        }
                    }
                    dialog.dismiss()
                }
                dialog.show()
            }

            R.id.btnSelesaikan -> {
                showalertConfirmation(
                    activity!!,
                    "Untuk menyelesaikan pesanan pastikan barang telah diterima, Lanjutkan?"
                ) {
                    viewModel.confirmPos(history?.oRDERNO.toString()) {
                        actionBatal("Transaksi telah selesai")
                    }
                }
            }

            R.id.btnCekStatus -> {
                viewModel.cekStatusBayar {
                    toDialogStatus(it)
                }
            }

            R.id.btnDetailBayar -> {
                viewModel.cekDetailBayar {
                    startActivity<DetailPembayaranActivity>("model" to it)
                }
            }

            R.id.imgBuktiBayar -> {
                layoutImageZoom.visibility = View.VISIBLE
            }

            R.id.imgBayarClose -> {
                layoutImageZoom.visibility = View.GONE

            }
        }
    }

    private fun toDialogStatus(model: PaymentModel) {
        val tagihan = (model.tRXAMOUNT?.toInt() ?: 0) + (model.tRXADMIN?.toInt() ?: 0)
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_detail_pembayaran)
        dialog.txtNoOrder.text = ": ${model.tRXCODE}"
        dialog.txtTanggal.text =
            ": ${convertDateTime(model.tRXDATE.toString(), "dd MMM yyyy HH:mm")}"
        dialog.txtJumlah.text =
            ": ${convertCurrency(model.tRXAMOUNT.toString(), 3, '.', model.tRXCURRENCY.toString())}"
        dialog.txtBiayaAdm.text =
            ": ${convertCurrency(model.tRXADMIN.toString(), 3, '.', model.tRXCURRENCY.toString())}"
        dialog.txtTotal.text =
            ": ${convertCurrency(tagihan.toString(), 3, '.', model.tRXCURRENCY.toString())}"
        dialog.txtNoBayar.text = ": ${model.tRXPAYMENTNO}"
        dialog.txtStatus.text = ": ${model.tRXPAYSTATUSTEXT}"
        dialog.txtExpired.text =
            "${convertDateTime(model.tRXPAYMENTEXPIRED.toString(), "dd MMM yyyy HH:mm")}"
        dialog.btnTutup.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun actionBatal(txt: String) {
        showalertInformation(activity!!, txt) {
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    private fun dialogSetDelivery() {
        if (adapterCourier.itemCount == 0) {
            viewModel.getCourier()
        }
        layoutDelivery.visibility = View.VISIBLE
    }

    private fun initViewModel() {
        viewModel.listCourier.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            adapterCourier.updateData(it)
        })

        viewModel.loadingHistoryDetail.observe(viewLifecycleOwner, Observer {
            progressProduk.visibility = if (it) View.VISIBLE else View.GONE
            progressStatus.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.historyDetail.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer

            when (it.oRDERSTATUS.toString()) {
                "1", "10" -> {
                    layoutAksi.visibility = View.VISIBLE
                    layoutProses.visibility = View.VISIBLE
                    layoutSelesai.visibility = View.GONE
                    layoutKirim.visibility = View.GONE
                    if (it.oRDERSTATUS == "10" && it.oRDERTYPE == "2") {
                        isCanvasser = true
                        layoutSelesai.visibility = View.VISIBLE
                        btnBatalkan.visibility = View.VISIBLE
                        btnSelesaikan.visibility = View.GONE
                    }
                }

                "3", "2" -> {
                    layoutAksi.visibility = View.VISIBLE
                    layoutProses.visibility = View.GONE
                    layoutSelesai.visibility = View.GONE
                    layoutKirim.visibility = View.VISIBLE
                }

                "11", "12", "18" -> {
                    layoutAksi.visibility = View.GONE
                }

                "6" -> {
                    layoutAksi.visibility = View.VISIBLE
                    layoutSelesai.visibility = View.VISIBLE
                }

                else -> {
                    layoutAksi.visibility = View.VISIBLE
                    layoutProses.visibility = View.GONE
                    layoutKirim.visibility = View.GONE
                    layoutSelesai.visibility = View.GONE
                    if (it.oRDERSTATUSPAY != "1") {
                        btnBatalkan.visibility = View.GONE
                    }
                }
            }

            if (it.oRDERSTATUSPAY == "4") {
                btnCekStatus.visibility = View.VISIBLE
                btnDetailBayar.visibility = View.VISIBLE
            } else {
                btnCekStatus.visibility = View.GONE
                btnDetailBayar.visibility = View.GONE
                line5.visibility = View.VISIBLE
            }

            Glide.with(this)
                .load(it.oRDERFROM?.pROFILE)
                .apply(RequestOptions().error(R.drawable.ic_store).circleCrop())
                .into(imgFrom)
            //imgbukti bayar
            Glide.with(this)
                .load(it.tRXPAYMENT)
                .apply(RequestOptions().error(R.drawable.no_image).centerCrop())
                .into(imgBuktiBayar)
            Glide.with(this)
                .load(it.tRXPAYMENT)
                .apply(RequestOptions().error(R.drawable.no_image).centerCrop())
                .into(imgBuktiBayarZoom)
            viewModel.buktiBayarImg.postValue(it.tRXPAYMENT)
            if (it.tRXPAYMENT.isNullOrEmpty())
                imgBuktiBayar.visibility = View.GONE
            else imgBuktiBayar.visibility = View.VISIBLE

            txtFromNama.text = "${it.oRDERFROM?.oUTLETNAME} (${it.oRDERFROM?.oWNERNAME})"
            txtFromAlamat.text = it.oRDERFROM?.aDDRESS
            txtFromPhone.text = it.oRDERFROM?.pHONE
            txtStatus.text = it.oRDERSTATUSTEXT
            txtTanggal.text = convertDateTime(it.oRDERDATE.toString(), "dd MMM yyyy")
            txtInvoice.text = it.oRDERNO
            txtJmlBarang.text = "Total Harga (${
                if (it.oRDERTOTALPAYQTY.toString() == "0") it.oRDERTOTALQTY else
                    it.oRDERTOTALPAYQTY
            } Item)"
            var tagihan = if (it.oRDERAMOUNT.toString() == "0") {
                (it.oRDERTOTALAMT?.toIntOrNull() ?: 0)
            } else {
                it.oRDERTOTALPAYAMT?.toIntOrNull() ?: 0
            }
            tagihan += if (it.oRDERAMOUNT.toString() == "0") {
                0
            } else {
                (it.oRDERMETHODPAYADMIN?.toIntOrNull() ?: 0)
            }
            tagihan += if (it.oRDERAMOUNT.toString() == "0") {
                0
            } else {
                (it.oRDERONGKIR?.toIntOrNull() ?: 0)
            }
            txtHarga.text = convertCurrency(
                it.oRDERAMOUNT.toString(),
                3,
                '.',
                UtilsPref.loadString("symbolCur")
            )
            txtBiaya.text = convertCurrency(
                it.oRDERMETHODPAYADMIN.toString(),
                3,
                '.',
                UtilsPref.loadString("symbolCur")
            )
            txtOngkir.text = convertCurrency(
                it.oRDERONGKIR.toString(),
                3,
                '.',
                UtilsPref.loadString("symbolCur")
            )
            txtTotalBayar.text =
                convertCurrency(tagihan.toString(), 3, '.', UtilsPref.loadString("symbolCur"))
            if (it.oRDERMETHODPAYSTATUS.isNullOrEmpty()) {
                txtStatusBayar.visibility = View.GONE
            } else {
                txtStatusBayar.text = it.oRDERMETHODPAYSTATUS
                if (it.oRDERMETHODPAYSTATUS?.toLowerCase()?.contains("sukses") == true) {
                    txtStatusBayar.setTextColor(ContextCompat.getColor(activity!!, R.color.teal))
                } else {
                    txtStatusBayar.setTextColor(ContextCompat.getColor(activity!!, R.color.dark))
                }
            }
            if (it.oRDERMETHODPAYNAME.isNullOrEmpty()) {
                layoutTipeBayar.visibility = View.GONE
            } else {
                txtJenisBayar.text = it.oRDERMETHODPAYNAME
            }

            adapter = DetailProdukLpAdapter(it.oRDERSTATUS.toString().toIntOrNull() ?: 0)
            recyclerProduk.layoutManager = LinearLayoutManager(activity)
            recyclerProduk.itemAnimator = DefaultItemAnimator()
            recyclerProduk.adapter = adapter

            it.oRDERPRODDETAIL?.let { product ->
                listProduk = product.toMutableList()
                adapter.updateData(product)
            }

            it.oRDERHISTORIES?.let { status ->
                adapterStatus.updateData(status)
            }
        })

        viewModel.loadingProses.observe(viewLifecycleOwner, Observer {
            if (it) progress.show(activity!!) else progress.dismiss()
        })

        viewModel.getPesananDetail(orderId.toString())
        viewModel.updateNotif("1", orderId.toString())
    }
}