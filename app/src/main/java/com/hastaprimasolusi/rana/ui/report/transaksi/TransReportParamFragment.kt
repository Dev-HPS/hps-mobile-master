package com.hastaprimasolusi.rana.ui.report.transaksi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.*
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.data.network.response.order.OrderStatusModel
import com.hastaprimasolusi.rana.data.network.response.payment.PaymentStatusModel
import com.hastaprimasolusi.rana.ui.common.ProgDialog
import com.hastaprimasolusi.rana.ui.report.ReportViewModel
import kotlinx.android.synthetic.main.fragment_trans_report_param.*
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class TransReportParamFragment: Fragment() {
    private val viewModel: ReportViewModel by inject()
    private val progress = ProgDialog().getInstance()
    private var startDate = ""
    private var endDate = ""
    private var selectedStatus: OrderStatusModel? = null
    private var selectedType: OrderStatusModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "Filter Data"
        return inflater.inflate(R.layout.fragment_trans_report_param, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        etTanggal.setOnClickListener {
            buildDatePicker()
        }
        btnSubmit.setOnClickListener {
            if(etKodeTransaksi.text?.isNotEmpty() == true){
                viewModel.searchParam = etKodeTransaksi.text.toString()
            }
            if(spnStatus.text.isNotEmpty()){
                viewModel.statusParam = selectedStatus?.sTSCODE.toString()
            }
            if(spnTipe.text.isNotEmpty()){
                viewModel.typeParam = selectedType?.sTSCODE.toString()
            }
            if(etTanggal.text?.isNotEmpty() == true){
                var sdf = SimpleDateFormat("dd/MM/yyyy")
                val dt1 = sdf.parse(startDate)
                val dt2 = sdf.parse(endDate)
                sdf = SimpleDateFormat("yyyy-MM-dd")
                viewModel.dateStartParam = sdf.format(dt1)
                viewModel.dateEndParam = sdf.format(dt2)
            }
            viewModel.isReloading = true
            activity?.supportFragmentManager?.popBackStack()
        }

        if(viewModel.userModel?.rOLENAME == "canvasser"){
            layoutType.visibility = View.VISIBLE
            layoutStatus.visibility = View.GONE
        }

        initViewModel()
    }

    private fun initViewModel(){
        viewModel.loadingStatus.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(it) progress.show(activity!!) else progress.dismiss()
        })

        viewModel.listStatus.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(it == null) return@Observer
            val adapter = ArrayAdapter(activity!!, android.R.layout.simple_list_item_1, it)
            spnStatus.setAdapter(adapter)
            spnStatus.setOnItemClickListener { _, _, i, _ ->
                selectedStatus = it[i]
            }
        })

        viewModel.orderType.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(it == null) return@Observer
            val adapter = ArrayAdapter(activity!!, android.R.layout.simple_list_item_1, it)
            spnTipe.setAdapter(adapter)
            spnTipe.setOnItemClickListener { _, _, i, _ ->
                selectedType = it[i]
            }
        })

        if(viewModel.listStatus.value.isNullOrEmpty()){
            viewModel.getPaymentStatus()
        }
    }

    private fun buildDatePicker(){
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        val now = Calendar.getInstance()
        builder.setSelection(androidx.core.util.Pair(now.timeInMillis, now.timeInMillis))

        //limit max date
        val constraintsBuilderRange = CalendarConstraints.Builder()
        constraintsBuilderRange.setEnd(now.timeInMillis)
        val validators: ArrayList<CalendarConstraints.DateValidator> = ArrayList()
        validators.add(DateValidatorPointBackward.before(now.timeInMillis))
        constraintsBuilderRange.setValidator(CompositeDateValidator.allOf(validators))
        builder.setCalendarConstraints(constraintsBuilderRange.build())

        val picker = builder.build()
        picker.show(childFragmentManager, picker.toString())
        picker.addOnNegativeButtonClickListener {
            picker.dismiss()
        }
        picker.addOnPositiveButtonClickListener {
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            startDate = sdf.format(it.first)
            endDate = sdf.format(it.second)
            etTanggal.setText("${sdf.format(it.first)} - ${sdf.format(it.second)}")
        }
    }
}