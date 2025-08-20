package com.hastaprimasolusi.rana.ui.canvasser.laporan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.adapter.page.RiwayatCanvasAdapter
import com.hastaprimasolusi.rana.data.local.DummyData
import kotlinx.android.synthetic.main.fragment_canvas_riwayat.*

/**
 * Created By maasrahman on 2020-05-02
 */
class CanvasRiwayatFragment: Fragment() {

    private val listData = mutableListOf<DummyData>()
    private lateinit var adapter: RiwayatCanvasAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_canvas_riwayat, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = RiwayatCanvasAdapter(listData)
        recyclerRiwayat.layoutManager = LinearLayoutManager(activity)
        recyclerRiwayat.itemAnimator = DefaultItemAnimator()
        recyclerRiwayat.adapter = adapter

        dummydata()
    }

    private fun dummydata(){
        for(i in 0 until 10){
            listData.add(DummyData(i.toString()))
        }
        adapter.notifyDataSetChanged()
    }
}