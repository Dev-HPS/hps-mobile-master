package com.hastaprimasolusi.rana.ui.mitra.keranjang.chekout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.adapter.expand.MetodeBayarAdapter
import com.hastaprimasolusi.rana.data.network.response.PayMethodModel
import com.hastaprimasolusi.rana.ui.canvasser.CanvasViewModel
import com.hastaprimasolusi.rana.ui.canvasser.riwayat.RiwayatDetailCanvasFragment
import com.hastaprimasolusi.rana.utils.UtilsPref
import com.hastaprimasolusi.rana.utils.convertCurrency
import com.hastaprimasolusi.rana.utils.showalertConfirmation
import com.hastaprimasolusi.rana.utils.showalertInformation
import kotlinx.android.synthetic.main.fragment_pembayaran.*
import org.jetbrains.anko.support.v4.startActivity
import org.koin.android.ext.android.inject

/**
 * Created By maasrahman on 2020-05-01
 */
class PembayaranCnvsFragment: Fragment() {
    private val viewModel: CanvasViewModel by inject()
    private val listTipe = mutableListOf<String>()
    private val listTipeBayar = mutableMapOf<String, MutableList<PayMethodModel>>()
    private lateinit var adapter: MetodeBayarAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.pembayaran)
        return inflater.inflate(R.layout.fragment_pembayaran, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.paymethodSelected.postValue(null)
        adapter = MetodeBayarAdapter(activity!!, listTipe, listTipeBayar)
        recyclerMetode.setAdapter(adapter)
        recyclerMetode.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->
            val model = listTipeBayar[listTipe[groupPosition]]?.get(childPosition)
            viewModel.paymethodSelected.postValue(model)
            false
        }
        btnSubmit.setOnClickListener(listener)
        btnDetail.setOnClickListener(listener)
        initViewModel()
    }

    private val listener = View.OnClickListener { view ->
        when(view.id){
            R.id.btnSubmit -> {
                showalertConfirmation(activity!!, "Konfirmasi pilihan Pembayaran"){
                    viewModel.setPaymentMethod {
                        val model = viewModel.historyDetail.value
                        model?.sTSBAYAR = "2"
                        viewModel.historyDetail.postValue(model)
                        if(viewModel.paymethodSelected.value?.pAYMETHODNAME?.toLowerCase()?.contains("tunai") == true){
                            showalertInformation(activity!!, "Jenis Pembayaran telah disimpan, barang akan segera diterima"){
                                activity?.finish()
                            }
                        }else{
                            startActivity<DetailPembayaranActivity>("model" to it.dATA)
                            activity?.finish()
                        }
                    }
                }
            }
            R.id.btnDetail -> {
                val frag = RiwayatDetailCanvasFragment()
                val args = Bundle()
                args.putString("id", viewModel.historyDetail.value?.iD.toString())
                frag.arguments = args
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.frame, frag)
                    ?.addToBackStack(null)
                    ?.commit()
            }
        }
    }

    private fun initViewModel(){
        viewModel.paymethodSelected.observe(viewLifecycleOwner, Observer {
            if(it == null){
                txtMetode.visibility = View.VISIBLE
                layoutBayar.visibility = View.GONE
                line2.visibility = View.GONE
            }else{
                line2.visibility = View.VISIBLE
                txtMetode.visibility = View.GONE
                layoutBayar.visibility = View.VISIBLE
                txtJenisPembayaran.text = it.pAYMETHODNAME
                Glide.with(this)
                    .load(it.pAYMETHODLOGO)
                    .apply(RequestOptions().error(R.drawable.no_image))
                    .into(imgLogoPembayaran)
                if(it.pAYMETHODADMIN.isNullOrEmpty()){
                    layoutAdmin.visibility = View.GONE
                }else{
                    layoutAdmin.visibility = View.VISIBLE
                    txtAdmin.text = convertCurrency(it.pAYMETHODADMIN.toString(), 3, '.', UtilsPref.loadString(getString(R.string.currencySymbol)))
                }
                txtJenis.text = if(it.pAYMETHODGROUP.isNullOrEmpty()) "Lainnya" else it.pAYMETHODGROUP.toString()
            }
        })

        viewModel.historyDetail.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            var qtyInfo = 0
            it.iNFODETAIL?.forEach { row ->
                qtyInfo += row.qTY?.toInt() ?: 0
            }
            txtJumlahItem.text = "$qtyInfo Item"
            txtTotalTagihan.text = convertCurrency(it.tOTALAMT.toString(), 3, '.', UtilsPref.loadString(getString(R.string.currencySymbol)))
        })

        viewModel.loadingPayment.observe(viewLifecycleOwner, Observer {
            progbar.visibility = if(it) View.VISIBLE else View.GONE
        })

        println("YO GET PAY")
        viewModel.getPaymentMethod { resp ->
            println("GET PAY METHOD")
            when(resp.rC){
                "0000" -> {
                    resp.dATA?.forEach { method ->
                        if(!listTipe.contains(method.pAYMETHODGROUP)){
                            if(method.pAYMETHODGROUP.toString().isNotEmpty()){
                                listTipe.add(method.pAYMETHODGROUP.toString())
                            }
                        }
                        if(method.pAYMETHODGROUP.isNullOrEmpty()){
                            val list = listTipeBayar["Lainnya"] ?: mutableListOf()
                            list.add(method)
                            listTipeBayar["Lainnya"] = list
                        }else{
                            val list = listTipeBayar[method.pAYMETHODGROUP.toString()] ?: mutableListOf()
                            list.add(method)
                            listTipeBayar[method.pAYMETHODGROUP.toString()] = list
                        }
                    }
                    listTipe.add("Lainnya")
                    adapter.notifyDataSetChanged()
                }
                "0001" -> {
                    viewModel.isUnAuthorized.postValue(true)
                }
                else -> {
                    showalertInformation(activity!!, resp.rCM.toString()){
                        activity?.finish()
                    }
                }
            }
        }
    }
}