package com.hastaprimasolusi.rana.ui.mitra.riwayat

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.adapter.page.DetailProdukAdapter
import com.hastaprimasolusi.rana.adapter.page.StatusPesananAdapter
import com.hastaprimasolusi.rana.data.network.requesthelper.ConfirmRequest
import com.hastaprimasolusi.rana.data.network.response.PaymentDetailModel
import com.hastaprimasolusi.rana.data.network.response.order.PaymentModel
import com.hastaprimasolusi.rana.ui.common.ProgDialog
import com.hastaprimasolusi.rana.ui.mitra.MitraViewModel
import com.hastaprimasolusi.rana.ui.mitra.keranjang.chekout.DetailPembayaranActivity
import com.hastaprimasolusi.rana.ui.mitra.keranjang.chekout.PembayaranActivity
import com.hastaprimasolusi.rana.utils.*
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.dialog_detail_pembayaran.*
import kotlinx.android.synthetic.main.fragment_detail_riwayat.*
import kotlinx.android.synthetic.main.fragment_detail_riwayat.btnCekStatus
import kotlinx.android.synthetic.main.fragment_detail_riwayat.imgClose
import kotlinx.android.synthetic.main.fragment_detail_riwayat.layoutTipeBayar
import kotlinx.android.synthetic.main.fragment_detail_riwayat.progressProduk
import kotlinx.android.synthetic.main.fragment_detail_riwayat.progressStatus
import kotlinx.android.synthetic.main.fragment_detail_riwayat.recyclerProduk
import kotlinx.android.synthetic.main.fragment_detail_riwayat.recyclerStatus
import kotlinx.android.synthetic.main.fragment_detail_riwayat.txtBiaya
import kotlinx.android.synthetic.main.fragment_detail_riwayat.txtHarga
import kotlinx.android.synthetic.main.fragment_detail_riwayat.txtInvoice
import kotlinx.android.synthetic.main.fragment_detail_riwayat.txtJenisBayar
import kotlinx.android.synthetic.main.fragment_detail_riwayat.txtJmlBarang
import kotlinx.android.synthetic.main.fragment_detail_riwayat.txtStatus
import kotlinx.android.synthetic.main.fragment_detail_riwayat.txtStatusBayar
import kotlinx.android.synthetic.main.fragment_detail_riwayat.txtTanggal
import kotlinx.android.synthetic.main.fragment_detail_riwayat.txtTotalBayar
import org.jetbrains.anko.support.v4.startActivity
import org.koin.android.ext.android.inject

/**
 * Created By maasrahman on 5/20/20
 */
class DetailRiwayatFragment: Fragment() {

    private val viewModel: MitraViewModel by inject()
    private var orderId: String? = null

    private lateinit var adapter: DetailProdukAdapter
    private lateinit var adapterStatus: StatusPesananAdapter
    private val progress = ProgDialog().getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_riwayat, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val args = arguments
        if(args != null){
            orderId = args.getString("id")

            adapter = DetailProdukAdapter()
            recyclerProduk.layoutManager = LinearLayoutManager(activity)
            recyclerProduk.itemAnimator = DefaultItemAnimator()
            recyclerProduk.adapter = adapter

            adapterStatus = StatusPesananAdapter()
            recyclerStatus.layoutManager = LinearLayoutManager(activity)
            recyclerStatus.itemAnimator = DefaultItemAnimator()
            recyclerStatus.adapter = adapterStatus

            initViewModel()
        }

        btnBayar.setOnClickListener(listener)
        btnShowQR.setOnClickListener(listener)
        imgClose.setOnClickListener(listener)
        btnDetailBayar.setOnClickListener(listener)
        btnCekStatus.setOnClickListener(listener)
    }

    private var listener = View.OnClickListener { view ->
        val history = viewModel.historyDetail.value
        when(view.id){
            R.id.btnBayar -> {
                when(history?.oRDERSTATUS){
                    "2" -> {
                        showalertConfirmation(activity!!, "Terdapat beberapa PRODUK yang stoknya tidak sesuai " +
                                "dengan jumlah pesanan akan disesuaikan secara otomatis oleh sistem, Lanjutkan?"){
                            val request = ConfirmRequest(history.oRDERNO.toString(), "1", "")
                            viewModel.confirmPesanan(request){
                                toPayment()
                            }
                        }
                    }
                    else -> {
                        toPayment()
                    }
                }
            }
            R.id.btnShowQR -> {
                slideUp(layoutQR)
            }
            R.id.imgClose -> {
                slideDown(layoutQR)
            }
            R.id.btnDetailBayar -> {
                viewModel.cekDetailBayar {
                    startActivity<DetailPembayaranActivity>("model" to it)
                }
            }
            R.id.btnCekStatus -> {
                viewModel.cekStatusBayar{
                    toDialogStatus(it)
                }
            }
        }
    }

    private fun toDialogStatus(model: PaymentModel){
        val tagihan = (model.tRXAMOUNT?.toInt() ?: 0) + (model.tRXADMIN?.toInt() ?: 0)
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_detail_pembayaran)
        dialog.txtNoOrder.text = ": ${model.tRXCODE}"
        dialog.txtTanggal.text = ": ${convertDateTime(model.tRXDATE.toString(), "dd MMM yyyy HH:mm")}"
        dialog.txtJumlah.text = ": ${convertCurrency(model.tRXAMOUNT.toString(), 3, '.', model.tRXCURRENCY.toString())}"
        dialog.txtBiayaAdm.text = ": ${convertCurrency(model.tRXADMIN.toString(), 3, '.', model.tRXCURRENCY.toString())}"
        dialog.txtTotal.text = ": ${convertCurrency(tagihan.toString(), 3, '.', model.tRXCURRENCY.toString())}"
        dialog.txtNoBayar.text = ": ${model.tRXPAYMENTNO}"
        dialog.txtStatus.text = ": ${model.tRXPAYSTATUSTEXT}"
        dialog.txtExpired.text = "${convertDateTime(model.tRXPAYMENTEXPIRED.toString(), "dd MMM yyyy HH:mm")}"
        dialog.btnTutup.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun toPayment(){
        startActivity<PembayaranActivity>()
    }

    private fun initViewModel(){
        viewModel.loadingHistoryDetail.observe(viewLifecycleOwner, Observer {
            progressProduk.visibility = if(it) View.VISIBLE else View.GONE
            progressStatus.visibility = if(it) View.VISIBLE else View.GONE
        })

        viewModel.historyDetail.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer

            it.oRDERDETAIL?.let { product ->
                adapter.updateData(product)
            }

            it.oRDERHISTORIES?.let { status ->
                adapterStatus.updateData(status)
            }

            txtStatus.text = it.oRDERSTATUSTEXT
            txtTanggal.text = convertDateTime(it.oRDERDATE.toString(), "dd MMM yyyy")
            txtInvoice.text = it.oRDERNO
            var tagihan = if(it.oRDERTOTALPAYAMT.toString() == "0"){ (it.oRDERTOTALAMT?.toIntOrNull() ?: 0) }else{ it.oRDERTOTALPAYAMT?.toIntOrNull() ?: 0 }
            tagihan += (it.oRDERMETHODPAYADMIN?.toIntOrNull() ?: 0)
            tagihan += (it.oRDERONGKIR?.toIntOrNull() ?: 0)
            txtJmlBarang.text = "Total Harga (${if(it.oRDERTOTALPAYQTY.toString() == "0") it.oRDERTOTALQTY else
                it.oRDERTOTALPAYQTY} Item)"
            txtHarga.text = convertCurrency(it.oRDERAMOUNT.toString(), 3, '.', UtilsPref.loadString("symbolCur"))
            txtTotalBayar.text = convertCurrency(tagihan.toString(), 3, '.', UtilsPref.loadString("symbolCur"))
            txtOngkir.text = convertCurrency(it.oRDERONGKIR.toString(), 3, '.', UtilsPref.loadString("symbolCur"))
            txtBiaya.text = convertCurrency(it.oRDERMETHODPAYADMIN.toString(), 3, '.', UtilsPref.loadString("symbolCur"))
            if(it.oRDERMETHODPAYNAME.isNullOrEmpty()){
                layoutTipeBayar.visibility = View.GONE
            }else{
                txtJenisBayar.text = it.oRDERMETHODPAYNAME
            }

            if(it.oRDERMETHODPAYSTATUS.isNullOrEmpty()){
                txtStatusBayar.visibility = View.GONE
            }else{
                txtStatusBayar.visibility = View.VISIBLE
                txtStatusBayar.text = it.oRDERMETHODPAYSTATUS
                if(it.oRDERMETHODPAYSTATUS?.toLowerCase()?.contains("sukses") == true){
                    txtStatusBayar.setTextColor(ContextCompat.getColor(activity!!, R.color.teal))
                }else{
                    txtStatusBayar.setTextColor(ContextCompat.getColor(activity!!, R.color.dark))
                }
            }

            when(it.oRDERSTATUS){
                "2", "3", "7" -> {
                    layoutCheckout.visibility = View.VISIBLE
                }
            }
            when (it.oRDERSTATUSPAY) {
                "1" -> {
                    layoutCheckout.visibility = View.VISIBLE
                }
                "4" -> {
                    txtStatusBayar.visibility = View.GONE
                    btnCekStatus.visibility = View.VISIBLE
                    btnDetailBayar.visibility = View.VISIBLE
                }
            }

            generateQr(it.oRDERNO.toString())
        })

        viewModel.loadingCheckOut.observe(viewLifecycleOwner, Observer {
            if(it) progress.show(activity!!) else progress.dismiss()
        })

        viewModel.getHistoryDetail(orderId.toString())
        viewModel.updateNotif("1", orderId.toString())
    }

    private fun generateQr(id: String){
        val multiFormat = MultiFormatWriter()
        try {
            val bitMatrix = multiFormat.encode(id, BarcodeFormat.QR_CODE,400,400)
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.createBitmap(bitMatrix)
            imgQR.setImageBitmap(bitmap)
        } catch (e: Exception) {

        }
    }
}