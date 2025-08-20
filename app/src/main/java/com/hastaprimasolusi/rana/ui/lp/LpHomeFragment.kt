package com.hastaprimasolusi.rana.ui.lp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.adapter.page.RiwayatAdapter
import com.hastaprimasolusi.rana.ui.lp.order.OrderDetailFragment
import com.hastaprimasolusi.rana.ui.report.ReportViewModel
import com.hastaprimasolusi.rana.utils.millisToDate
import com.hastaprimasolusi.rana.utils.showAlert
import kotlinx.android.synthetic.main.fragment_canvas_home.*
import org.koin.android.ext.android.inject

/**
 * Created By maasrahman on 5/7/20
 */
class LpHomeFragment: Fragment() {
    private val viewModel: LpViewModel by inject()
    private val reportModel: ReportViewModel by inject()
    private lateinit var adapter: RiwayatAdapter
    private var statusSelected = MutableLiveData<String>()
    private var limit = 20
    private var offset = 0
    private var isWaitingData = false
    private var isLoadMore = false
    private var isFirstLoading = true
    private var dateFirst: Long? = null
    private var dateEnd: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.app_name)
        return inflater.inflate(R.layout.fragment_canvas_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = RiwayatAdapter {
            viewModel.historyDetail.postValue(null)
            val frag = OrderDetailFragment()
            val args = Bundle()
            args.putString("id", it?.oRDERID)
            frag.arguments = args
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.frame, frag)
                ?.addToBackStack(null)
                ?.commit()
        }
        recyclerPesanan.layoutManager = LinearLayoutManager(activity)
        recyclerPesanan.itemAnimator = DefaultItemAnimator()
        recyclerPesanan.adapter = adapter
        recyclerPesanan.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (!isWaitingData && isLoadMore) {
                    if (linearLayoutManager != null &&
                        linearLayoutManager.findLastCompletelyVisibleItemPosition() == adapter.itemCount - 1) {
                        adapter.addProgress()
                        isWaitingData = true
                        viewModel.getPesanan(limit.toString(), offset.toString(), getStatus(), false)
                    }
                }
                if (dy > 0 && btnFilter.visibility == View.VISIBLE) {
                    btnFilter.hide()
                } else if (dy < 0 && btnFilter.visibility != View.VISIBLE) {
                    btnFilter.show()
                }
            }
        })
        btnFilter.setOnClickListener(listener)
        btnClose.setOnClickListener(listener)
        btnReset.setOnClickListener(listener)
        btnSubmit.setOnClickListener(listener)
        etFilterTanggal.setOnClickListener(listener)
        initViewModel()
    }

    private val listener = View.OnClickListener { view ->
        when(view.id){
            R.id.btnFilter -> {
                layoutFilter.visibility = View.VISIBLE
                btnFilter.hide()
            }
            R.id.btnClose -> {
                resetFilter(false)
            }
            R.id.btnReset -> {
                resetFilter(true)
            }
            R.id.etFilterTanggal -> {
                val builder = MaterialDatePicker.Builder.dateRangePicker().setTheme(R.style.ThemeOverlay_MaterialComponents_MaterialCalendar)
                val picker = builder.build()
                picker.show(childFragmentManager, picker.toString())
                picker.addOnPositiveButtonClickListener { date ->
                    dateFirst = date.first
                    dateEnd = date.second
                    etFilterTanggal.setText("${date.first?.let { millisToDate(it,"dd/MM/yyyy") }} - " +
                            "${date.second?.let { millisToDate(it,"dd/MM/yyyy") }}")
                }
            }
            R.id.btnSubmit -> {
                if(etFilterTanggal.text.isNullOrEmpty()){
                    etFilterTanggal.error = getString(R.string.wajibdiisi)
                    return@OnClickListener
                }
                viewModel.dtStart = dateFirst?.let { millisToDate(it, "yyyy-MM-dd") } ?: ""
                viewModel.dtEnd = dateEnd?.let { millisToDate(it, "yyyy-MM-dd") } ?: ""
                layoutFilter.visibility = View.GONE
                btnFilter.show()
                loadDatawithLoading(getStatus())
            }
        }
    }

    private fun resetFilter(isReload: Boolean){
        viewModel.dtStart = ""
        viewModel.dtEnd = ""
        layoutFilter.visibility = View.GONE
        btnFilter.show()
        if(isReload){
            etFilterTanggal.text?.clear()
            loadDatawithLoading(getStatus())
        }
    }

    private fun initViewModel(){
        viewModel.listHistory.observe(viewLifecycleOwner, Observer {
            layoutNoData.visibility = if(it.isNullOrEmpty()) View.VISIBLE else View.GONE
            isLoadMore = it.size.rem(limit) == 0
            if(isWaitingData){
                isWaitingData = false
                offset += limit
                adapter.removeProgress()
            }
            adapter.updateData(it)
        })

        viewModel.loadingHistory.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            swipe.isRefreshing = it
        })

        viewModel.showErrorPesanan.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            viewModel.showErrorPesanan.postValue(null)
            adapter.removeProgress()
            showAlert(activity!!, it)
        })

        viewModel.listStatus.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            reportModel.listStatus.postValue(it.toMutableList())
            if(chipGroup.childCount <= 1){
                it.forEach { row ->
                    val chip = activity?.layoutInflater?.inflate(R.layout.item_chip, null, false) as Chip
                    chip.text = row.sTSTEXT
                    chip.setOnCheckedChangeListener { _, isChecked ->
                        if(isChecked){
                            statusSelected.postValue(row.sTSCODE.toString())
                        }
                    }
                    chipGroup.addView(chip)
                }
                chipSemua.setOnCheckedChangeListener { _, isChecked ->
                    if(isChecked){
                        statusSelected.postValue("")
                    }
                }
            }
        })

        statusSelected.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            adapter.updateData(emptyList())
            loadDatawithLoading(it)
        })

//        if(viewModel.listHistory.value.isNullOrEmpty()){
        if(isFirstLoading){
            offset = 0
            isFirstLoading = false
            isWaitingData = false
            viewModel.homeRequest(limit.toString(), offset.toString(), getStatus())
        }else{
            loadDatawithLoading(getStatus())
        }

        swipe.setOnRefreshListener {
            loadDatawithLoading(getStatus())
        }
    }

    private fun loadDatawithLoading(status: String){
        adapter.updateData(emptyList())
        isWaitingData = true
        offset = 0
        viewModel.getPesanan(limit.toString(), offset.toString(), status, true)
    }

    private fun getStatus(): String{
        return statusSelected.value ?: ""
    }
}