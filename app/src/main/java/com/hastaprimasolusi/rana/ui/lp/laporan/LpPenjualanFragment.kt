package com.hastaprimasolusi.rana.ui.lp.laporan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.adapter.page.LaporanCanvasAdapter
import com.hastaprimasolusi.rana.adapter.page.SummaryAdapter
import com.hastaprimasolusi.rana.data.local.DummyData
import com.hastaprimasolusi.rana.data.local.SummaryModel
import kotlinx.android.synthetic.main.fragment_lp_penjualan.*

/**
 * Created By maasrahman on 5/7/20
 */
class LpPenjualanFragment: Fragment() {
    private val listSummary = mutableListOf<SummaryModel>()
    private lateinit var adapterSum: SummaryAdapter
    private val listData = mutableListOf<DummyData>()
    private lateinit var adapter: LaporanCanvasAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.penjualan)
        return inflater.inflate(R.layout.fragment_lp_penjualan, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapterSum = SummaryAdapter(listSummary)
        adapter = LaporanCanvasAdapter(listData)
        recyclerSummary.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recyclerLaporan.layoutManager = LinearLayoutManager(activity)
        recyclerSummary.itemAnimator = DefaultItemAnimator()
        recyclerLaporan.itemAnimator = DefaultItemAnimator()
        recyclerSummary.adapter = adapterSum
        recyclerLaporan.adapter = adapter

        dummydata()
    }

    private fun dummydata(){
        listSummary.add(SummaryModel("Semua", 400))
        listSummary.add(SummaryModel("Pesanan Baru", 80))
        listSummary.add(SummaryModel("Diproses", 40))
        listSummary.add(SummaryModel("Selesai", 70))
        listSummary.add(SummaryModel("Retur", 10))
        for(i in 0 until 10){
            listData.add(DummyData(i.toString()))
        }
        adapter.notifyDataSetChanged()
        adapterSum.notifyDataSetChanged()
    }
}