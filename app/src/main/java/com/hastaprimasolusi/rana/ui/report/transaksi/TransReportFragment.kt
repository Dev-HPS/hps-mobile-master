package com.hastaprimasolusi.rana.ui.report.transaksi

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.adapter.page.ReportAdapter
import com.hastaprimasolusi.rana.ui.common.ProgDialog
import com.hastaprimasolusi.rana.ui.report.ReportViewModel
import com.hastaprimasolusi.rana.utils.UtilsPref
import com.hastaprimasolusi.rana.utils.convertCurrency
import kotlinx.android.synthetic.main.fragment_trans_report.*
import org.koin.android.ext.android.inject

class TransReportFragment: Fragment() {
    private val viewModel: ReportViewModel by inject()
    private val progress = ProgDialog().getInstance()
    private lateinit var adapter: ReportAdapter
    private var isWaitingData = false
    private var isLoadMore = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "Laporan Transaksi"
        return inflater.inflate(R.layout.fragment_trans_report, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = ReportAdapter(viewModel.userModel?.rOLENAME.toString())
        recyclerTrans.layoutManager = LinearLayoutManager(activity)
        recyclerTrans.itemAnimator = DefaultItemAnimator()
        recyclerTrans.adapter = adapter
        recyclerTrans.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (!isWaitingData && isLoadMore) {
                    if (linearLayoutManager != null &&
                        linearLayoutManager.findLastCompletelyVisibleItemPosition() == adapter.itemCount - 1) {
                        adapter.addProgress()
                        isWaitingData = true
                        viewModel.getReport(isLoading = false, isOffsetReset = false)
                    }
                }
                if (dy > 0 && btnFilter.visibility == View.VISIBLE) {
                    btnFilter.hide()
                } else if (dy < 0 && btnFilter.visibility != View.VISIBLE) {
                    btnFilter.show()
                }
            }
        })
        btnFilter.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.frame,
                    TransReportParamFragment()
                )
                ?.addToBackStack(null)
                ?.commit()
        }
        initViewModel()
    }

    private fun initViewModel(){
        viewModel.reportResume.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            txtJmlTrans.text = "${it.tRXCOUNT} Transaksi (${it.tRXTOTALITEMS} Item)"
            txtJumlah.text = convertCurrency(it.tRXTOTALAMOUNT.toString(), 3, '.', UtilsPref.loadString(getString(R.string.currencySymbol)))
            txtTotal.text = convertCurrency(it.tRXTOTALTOTAL.toString(), 3, '.', UtilsPref.loadString(getString(R.string.currencySymbol)))
            txtAdmin.text = convertCurrency(it.tRXTOTALADMIN.toString(), 3, '.', UtilsPref.loadString(getString(R.string.currencySymbol)))
        })

        viewModel.reportData.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            isLoadMore = it.size.rem(viewModel.limit) == 0
            if(isWaitingData){
                isWaitingData = false
                adapter.removeProgress()
            }
            adapter.updateData(it)
        })

        viewModel.disableLoadMore.observe(viewLifecycleOwner, Observer {
            if(it){
                isLoadMore = false
                viewModel.disableLoadMore.postValue(false)
            }
        })

        viewModel.loadingProgress.observe(viewLifecycleOwner, Observer {
            if(it) progress.show(activity!!) else progress.dismiss()
        })

        if(!viewModel.isReloading && !isWaitingData){
            isWaitingData = true
            viewModel.getReport(isLoading = true, isOffsetReset = true)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if(resultCode == RESULT_OK && requestCode == 14){
//            isWaitingData = false
//            adapter.updateData(emptyList())
//            viewModel.isReloading = false
//            viewModel.getReport(isLoading = true, isOffsetReset = true)
//        }
    }

    override fun onResume() {
        super.onResume()
        if(viewModel.isReloading){
            isWaitingData = false
            adapter.updateData(emptyList())
            viewModel.isReloading = false
            viewModel.getReport(isLoading = true, isOffsetReset = true)
        }
    }
}