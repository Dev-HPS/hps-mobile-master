package com.hastaprimasolusi.rana.ui.canvasser.penjualan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.adapter.page.CheckoutCanvasAdapter
import com.hastaprimasolusi.rana.data.local.DummyData
import kotlinx.android.synthetic.main.fragment_canvas_pelanggan.*

/**
 * Created By maasrahman on 5/6/20
 */
class CanvasPelangganFragment: Fragment() {

    private val listData = mutableListOf<DummyData>()
    private lateinit var adapter: CheckoutCanvasAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_canvas_pelanggan, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = CheckoutCanvasAdapter(listData)
        recyclerItem.layoutManager = LinearLayoutManager(activity)
        recyclerItem.itemAnimator = DefaultItemAnimator()
        recyclerItem.adapter = adapter

        btnBayar.setOnClickListener {

        }

        dummydata()
    }

    private fun dummydata(){
        for(i in 0 until 2){
            listData.add(DummyData(i.toString()))
        }
        adapter.notifyDataSetChanged()
    }
}