package com.hastaprimasolusi.rana.ui.report.mutasi

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
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CompositeDateValidator
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.adapter.page.MutasiAdapter
import com.hastaprimasolusi.rana.ui.common.ProgDialog
import com.hastaprimasolusi.rana.ui.report.ReportViewModel
import kotlinx.android.synthetic.main.fragment_mutasi.*
import kotlinx.android.synthetic.main.fragment_trans_report_param.etTanggal
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by maasrahman on 16/09/20.
 */
class MutasiFragment: Fragment() {
    private val viewModel: ReportViewModel by inject()
    private val progress = ProgDialog().getInstance()
    private lateinit var adapter: MutasiAdapter
    private var startDate = ""
    private var endDate = ""
    private var limit = 10
    private var offset = 0
    private var isWaitingData = false
    private var isLoadMore = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "Mutasi Saldo"
        return inflater.inflate(R.layout.fragment_mutasi, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = MutasiAdapter()
        recyclerMutasi.layoutManager = LinearLayoutManager(activity)
        recyclerMutasi.itemAnimator = DefaultItemAnimator()
        recyclerMutasi.adapter = adapter
        recyclerMutasi.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (!isWaitingData && isLoadMore) {
                    if (linearLayoutManager != null &&
                        linearLayoutManager.findLastCompletelyVisibleItemPosition() == adapter.itemCount - 1) {
                        adapter.addProgress()
                        isWaitingData = true
                        viewModel.getMutasi(limit.toString(), offset.toString(), startDate, endDate, false)
                    }
                }
            }
        })
        etTanggal.setOnClickListener(listener)
        initViewModel()
    }

    private val listener = View.OnClickListener { view ->
        when(view.id){
            R.id.etTanggal -> {
                buildDatePicker()
            }
        }
    }

    private fun initViewModel(){
        viewModel.loadingProgress.observe(viewLifecycleOwner, Observer {
            if(it) progress.show(activity!!) else progress.dismiss()
        })

        viewModel.mutasiData.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            layoutNoData.visibility = View.GONE
            adapter.removeProgress()
            isLoadMore = it.size.rem(limit) == 0
            isWaitingData = false
            adapter.updateData(it)
        })

        viewModel.getMutasi(limit.toString(), offset.toString(), startDate, endDate, true)
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
            adapter.updateData(emptyList())
            layoutNoData.visibility = View.VISIBLE
            var sdf = SimpleDateFormat("yyyy-MM-dd")
            startDate = sdf.format(it.first)
            endDate = sdf.format(it.second)
            sdf = SimpleDateFormat("dd/MM/yyyy")
            etTanggal.setText("${sdf.format(it.first)} - ${sdf.format(it.second)}")
            offset = 0
            viewModel.getMutasi(limit.toString(), offset.toString(), startDate, endDate, true)
        }
    }
}