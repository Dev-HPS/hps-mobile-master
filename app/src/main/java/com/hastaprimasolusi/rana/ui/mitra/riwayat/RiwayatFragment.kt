package com.hastaprimasolusi.rana.ui.mitra.riwayat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.hastaprimasolusi.rana.ui.mitra.MitraViewModel
import com.hastaprimasolusi.rana.utils.millisToDate
import com.hastaprimasolusi.rana.utils.showAlert
import kotlinx.android.synthetic.main.fragment_riwayat.*
import kotlinx.android.synthetic.main.fragment_riwayat.btnFilter
import kotlinx.android.synthetic.main.fragment_riwayat.chipGroup
import kotlinx.android.synthetic.main.fragment_riwayat.chipSemua
import kotlinx.android.synthetic.main.fragment_riwayat.layoutNoData
import kotlinx.android.synthetic.main.fragment_riwayat.swipe
import org.jetbrains.anko.support.v4.startActivity
import org.koin.android.ext.android.inject

/**
 * Created By maasrahman on 2020-04-26
 */
class RiwayatFragment: Fragment() {

    private val viewModel: MitraViewModel by inject()
    private lateinit var adapter: RiwayatAdapter
    private var statusSelected = MutableLiveData<String>()
    private var limit = 10
    private var offset = 0
    private var isWaitingData = false
    private var isLoadMore = false
    private var dateFirst: Long? = null
    private var dateEnd: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_riwayat, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.riwayat14.postValue(false)
        adapter = RiwayatAdapter {
            viewModel.historyDetail.postValue(null)
            startActivity<DetailRiwayatActivity>("id" to it?.oRDERID)
        }
        recyclerRiwayat.layoutManager = LinearLayoutManager(activity)
        recyclerRiwayat.itemAnimator = DefaultItemAnimator()
        recyclerRiwayat.adapter = adapter
        recyclerRiwayat.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (!isWaitingData && isLoadMore) {
                    if (linearLayoutManager != null &&
                        linearLayoutManager.findLastCompletelyVisibleItemPosition() == adapter.itemCount - 1) {
                        adapter.addProgress()
                        isWaitingData = true
                        viewModel.getHistory(limit.toString(), offset.toString(), getStatus(), false)
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
                loadDataWithLoading(getStatus())
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
            loadDataWithLoading(getStatus())
        }
    }

    private fun initViewModel(){
        viewModel.listHistory.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            if(it.size > 0) layoutNoData.visibility = View.GONE
            isLoadMore = it.size.rem(limit) == 0
            if(isWaitingData){
                adapter.removeProgress()
                isWaitingData = false
                offset += limit
            }
            adapter.updateData(it)
        })

        viewModel.loadingHistory.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            swipe.isRefreshing = it
        })

        viewModel.riwayat14.observe(viewLifecycleOwner, Observer {
            layoutNoData.visibility = if(it) View.VISIBLE else View.GONE
        })

        viewModel.listStatus.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
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

        viewModel.showErrorRiwayat.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            viewModel.showErrorRiwayat.postValue(null)
            adapter.removeProgress()
            showAlert(activity!!, it)
        })

        statusSelected.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            adapter.updateData(emptyList())
            loadDataWithLoading(it)
        })

//        if(viewModel.listHistory.value.isNullOrEmpty()){
            loadDataWithLoading(getStatus())
//        }

        swipe.setOnRefreshListener {
            loadDataWithLoading(getStatus())
        }
    }

    private fun loadDataWithLoading(status: String){
        adapter.updateData(emptyList())
        offset = 0
        isWaitingData = true
        viewModel.getHistory(limit.toString(), offset.toString(), status, true)
    }

    private fun getStatus(): String{
        return statusSelected.value ?: ""
    }
}