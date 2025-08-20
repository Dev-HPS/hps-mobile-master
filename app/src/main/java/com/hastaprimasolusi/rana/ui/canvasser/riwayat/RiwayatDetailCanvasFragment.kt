package com.hastaprimasolusi.rana.ui.canvasser.riwayat

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.adapter.page.DetailProdukCnvsAdapter
import com.hastaprimasolusi.rana.adapter.page.StatusPesananCnvsAdapter
import com.hastaprimasolusi.rana.data.network.response.canvas.RiwayatCnvsProdModel
import com.hastaprimasolusi.rana.data.network.response.order.PaymentModel
import com.hastaprimasolusi.rana.data.network.response.order.PembatalanResponse
import com.hastaprimasolusi.rana.ui.canvasser.CanvasViewModel
import com.hastaprimasolusi.rana.ui.common.ProgDialog
import com.hastaprimasolusi.rana.ui.mitra.keranjang.chekout.DetailPembayaranActivity
import com.hastaprimasolusi.rana.ui.mitra.keranjang.chekout.PembayaranCnvsActivity
import com.hastaprimasolusi.rana.ui.qr.QrScanActivity
import com.hastaprimasolusi.rana.utils.*
import kotlinx.android.synthetic.main.dialog_cart_sukses.*
import kotlinx.android.synthetic.main.dialog_detail_pembayaran.*
import kotlinx.android.synthetic.main.dialog_pembatalan.*
import kotlinx.android.synthetic.main.dialog_pembatalan.txtHeader
import kotlinx.android.synthetic.main.fragment_riwayat_detail_cnvs.*
import kotlinx.android.synthetic.main.fragment_riwayat_detail_cnvs.btnBatalkan
import kotlinx.android.synthetic.main.fragment_riwayat_detail_cnvs.btnCekStatus
import kotlinx.android.synthetic.main.fragment_riwayat_detail_cnvs.imgFrom
import kotlinx.android.synthetic.main.fragment_riwayat_detail_cnvs.layoutAksi
import kotlinx.android.synthetic.main.fragment_riwayat_detail_cnvs.layoutFrom
import kotlinx.android.synthetic.main.fragment_riwayat_detail_cnvs.layoutSelesai
import kotlinx.android.synthetic.main.fragment_riwayat_detail_cnvs.layoutTipeBayar
import kotlinx.android.synthetic.main.fragment_riwayat_detail_cnvs.lblJenisBayar
import kotlinx.android.synthetic.main.fragment_riwayat_detail_cnvs.progressProduk
import kotlinx.android.synthetic.main.fragment_riwayat_detail_cnvs.progressStatus
import kotlinx.android.synthetic.main.fragment_riwayat_detail_cnvs.recyclerProduk
import kotlinx.android.synthetic.main.fragment_riwayat_detail_cnvs.recyclerStatus
import kotlinx.android.synthetic.main.fragment_riwayat_detail_cnvs.txtBiaya
import kotlinx.android.synthetic.main.fragment_riwayat_detail_cnvs.txtFromAlamat
import kotlinx.android.synthetic.main.fragment_riwayat_detail_cnvs.txtFromNama
import kotlinx.android.synthetic.main.fragment_riwayat_detail_cnvs.txtHarga
import kotlinx.android.synthetic.main.fragment_riwayat_detail_cnvs.txtInvoice
import kotlinx.android.synthetic.main.fragment_riwayat_detail_cnvs.txtJenisBayar
import kotlinx.android.synthetic.main.fragment_riwayat_detail_cnvs.txtJmlBarang
import kotlinx.android.synthetic.main.fragment_riwayat_detail_cnvs.txtStatus
import kotlinx.android.synthetic.main.fragment_riwayat_detail_cnvs.txtStatusBayar
import kotlinx.android.synthetic.main.fragment_riwayat_detail_cnvs.txtTanggal
import kotlinx.android.synthetic.main.fragment_riwayat_detail_cnvs.txtTotalBayar
import org.jetbrains.anko.support.v4.startActivity
import org.koin.android.ext.android.inject

/**
 * Created By maasrahman on 5/30/20
 */
class RiwayatDetailCanvasFragment: Fragment() {
    private val viewModel: CanvasViewModel by inject()

    private var orderId: String? = null

    private lateinit var adapter: DetailProdukCnvsAdapter
    private lateinit var adapterStatus: StatusPesananCnvsAdapter
    private var listProduk = mutableListOf<RiwayatCnvsProdModel>()
    private val progress = ProgDialog().getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_riwayat_detail_cnvs, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.historyDetail.postValue(null)
        val args = arguments
        if(args != null){
            orderId = args.getString("id")

            adapter = DetailProdukCnvsAdapter()
            recyclerProduk.layoutManager = LinearLayoutManager(activity)
            recyclerProduk.itemAnimator = DefaultItemAnimator()
            recyclerProduk.adapter = adapter

            adapterStatus = StatusPesananCnvsAdapter()
            recyclerStatus.layoutManager = LinearLayoutManager(activity)
            recyclerStatus.itemAnimator = DefaultItemAnimator()
            recyclerStatus.adapter = adapterStatus

            initViewModel()
        }
        btnBatalkan.setOnClickListener(listener)
        btnKonfirmasi.setOnClickListener(listener)
        btnSelesai.setOnClickListener(listener)
        btnPembayaran.setOnClickListener(listener)
        btnTolak.setOnClickListener(listener)
        btnCekStatus.setOnClickListener(listener)
        btnDetailBayar.setOnClickListener(listener)
    }

    private val listener = View.OnClickListener { view ->
        val history = viewModel.historyDetail.value
        when(view.id){
            R.id.btnKonfirmasi -> {
                showalertInformation(activity!!, getString(R.string.konfirmasipengirimanbarang)){
                    viewModel.actionKonfirmLp(history?.cODE.toString(), "1", "")
                }
            }
            R.id.btnBatalkan -> {
                val dialog = Dialog(activity!!)
                dialog.setContentView(R.layout.dialog_pembatalan)
                dialog.btnSubmitBatal.setOnClickListener {
                    if(dialog.etKeterangan.text.isNullOrEmpty()){
                        dialog.etKeterangan.error = "Harap diisi"
                        return@setOnClickListener
                    }
                    viewModel.actionBatalkan(history?.cODE.toString()){
                        actionBatal(it)
                    }
                    dialog.dismiss()
                }
                dialog.show()
            }
            R.id.btnSelesai -> {
                showalertConfirmation(activity!!, getString(R.string.konfirmasipengirimanbarang)){
                    startActivityForResult(Intent(activity!!, QrScanActivity::class.java), 888)
                }
            }
            R.id.btnPembayaran -> {
                startActivity<PembayaranCnvsActivity>("id" to history?.iD.toString())
            }
            R.id.btnTolak -> {
                dialogTolak(history?.cODE.toString())
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

    private fun dialogTolak(orderNo: String){
        val dialog = Dialog(activity!!)
        dialog.setContentView(R.layout.dialog_pembatalan)
        dialog.txtHeader.text = "Alasan penolakan"
        dialog.btnSubmitBatal.text = "Submit"
        dialog.btnSubmitBatal.setOnClickListener {
            viewModel.actionKonfirmLp(orderNo, "0", dialog.etKeterangan.text.toString())
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 888 && resultCode == Activity.RESULT_OK){
            val bundle = data?.extras
            if(bundle != null){
                val id = bundle.getString("id")
                viewModel.selesaiKirim(id.toString()){
                    val dialog = Dialog(activity!!)
                    dialog.setContentView(R.layout.dialog_cart_sukses)
                    dialog.txtHeader.text = "Pengiriman berhasil"
                    dialog.btnOk.setOnClickListener {
                        activity?.supportFragmentManager?.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                        dialog.dismiss()
                    }
                    dialog.show()
                }
            }else{
                showAlert(activity!!, "Ulangi QR Scanner")
            }
        }
    }

    private fun actionBatal(resp: PembatalanResponse){
        if(resp.rC == "0000"){
            showalertInformation(activity!!, "Pembatalan pesanan berhasil"){
                activity?.supportFragmentManager?.popBackStack()
            }
        }else{
            showAlert(activity!!, resp.rCM.toString())
        }
    }

    private fun initViewModel(){
        viewModel.loadingHistoryDetail.observe(viewLifecycleOwner, Observer {
            progressProduk.visibility = if(it) View.VISIBLE else View.GONE
            progressStatus.visibility = if(it) View.VISIBLE else View.GONE
        })

        viewModel.historyDetail.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer

            btnDetailBayar.visibility = View.GONE
            btnCekStatus.visibility = View.GONE
            layoutBatal.visibility = View.GONE
            layoutKonfirmasi.visibility = View.GONE
            layoutCheckout.visibility = View.GONE
            layoutSelesai.visibility = View.GONE
            layoutTipeBayar.visibility = View.GONE
            layoutAksi.visibility = View.VISIBLE

            it.iNFOOUTLET?.let { from ->
                layoutFrom.visibility = View.VISIBLE
                Glide.with(this)
                    .load(from.pROFILE)
                    .apply(RequestOptions().error(R.drawable.ic_store).circleCrop())
                    .into(imgFrom)
                txtFromNama.text = "${from.nAMAOUTLET} (${from.nAMAPEMILIK})"
                txtFromAlamat.text = from.aLAMAT
            }

            it.iNFOSTATUSPEMBAYARAN?.let { pemb ->
                layoutTipeBayar.visibility = View.VISIBLE
                lblJenisBayar.text = pemb.cARABAYARLABEL
                txtJenisBayar.text = pemb.cARABAYARTEXT
                lblStatusBayar.text = pemb.sTATUSLABEL
                if(pemb.sTATUSTEXT?.isEmpty() == true){
                    txtStatusBayar.visibility = View.GONE
                }else{
                    txtStatusBayar.text = pemb.sTATUSTEXT
                    if(pemb.sTATUSTEXT?.toLowerCase()?.contains("sukses") == true){
                        txtStatusBayar.setTextColor(ContextCompat.getColor(activity!!, R.color.teal))
                    }else{
                        txtStatusBayar.setTextColor(ContextCompat.getColor(activity!!, R.color.dark))
                    }
                }
            }

            it.iNFODETAIL?.let { product ->
                listProduk = product.toMutableList()
                adapter.updateData(product)
            }

            it.iNFORIWAYAT?.let { status ->
                adapterStatus.updateData(status)
            }

            var qty = 0
            listProduk.forEach { prod ->
                prod.qTY?.let { q ->
                    qty += q.toInt()
                }
            }

            if(it.sTSBAYAR == "4"){
                btnCekStatus.visibility = View.VISIBLE
                btnDetailBayar.visibility = View.VISIBLE
            }else{
                btnCekStatus.visibility = View.GONE
                btnDetailBayar.visibility = View.GONE
                line5.visibility = View.VISIBLE
            }
            val tagihan = (it.aDMINFEE?.toIntOrNull() ?: 0) + (it.tOTALAMT?.toIntOrNull() ?: 0) + (it.oNGKIR?.toIntOrNull() ?: 0)
            txtBiaya.text = convertCurrency(it.aDMINFEE.toString(), 3, '.', UtilsPref.loadString("symbolCur"))
            txtJmlBarang.text = "Total Harga (${qty} Item)"
            txtHarga.text = convertCurrency(it.tOTALORDERAMT.toString(), 3, '.', UtilsPref.loadString("symbolCur"))
            txtTotalBayar.text = convertCurrency(tagihan.toString(), 3, '.', UtilsPref.loadString("symbolCur"))
            txtOngkir.text = convertCurrency(it.oNGKIR.toString(), 3, '.', UtilsPref.loadString("symbolCur"))
            if(it.sTSBAYAR == "1" && it.tYPE == "3") {
//                pemilihan tipe bayar dihilangkan karena jarang mitra menggunakan aplikasi
//                layoutBatal.visibility = View.VISIBLE
            }else if(it.tYPE == "1" && (it.sTATUS == "8" || it.sTATUS == "7")) {
                layoutSelesai.visibility = View.VISIBLE
            }else if(it.tYPE == "1" && it.sTATUS == "9") {
                layoutKonfirmasi.visibility = View.VISIBLE
            }else if(it.tYPE == "2" && it.sTSBAYAR == "1"){
                layoutCheckout.visibility = View.VISIBLE
            }else{
                layoutAksi.visibility = View.GONE
            }

            txtStatus.text = it.sTATUSTEXT
            txtTanggal.text = convertDateTime(it.dATETIME.toString(), "dd MMM yyyy")
            txtInvoice.text = it.cODE
        })

        viewModel.loadingProses.observe(viewLifecycleOwner, Observer {
            if(it) progress.show(activity!!) else progress.dismiss()
        })

        viewModel.getPesananDetail(orderId.toString())
        viewModel.updateNotif("1", orderId.toString())
    }

}