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
import com.hastaprimasolusi.rana.adapter.page.CanvasserAdapter
import com.hastaprimasolusi.rana.data.local.DummyData
import kotlinx.android.synthetic.main.fragment_lp_canvasser.*

/**
 * Created By maasrahman on 5/7/20
 */
class LpCanvasserFragment: Fragment() {

    private val listData = mutableListOf<DummyData>()
    private lateinit var adapter: CanvasserAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.canvasser)
        return inflater.inflate(R.layout.fragment_lp_canvasser, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = CanvasserAdapter(listData)
        recyclerCanvas.layoutManager = LinearLayoutManager(activity)
        recyclerCanvas.itemAnimator = DefaultItemAnimator()
        recyclerCanvas.adapter = adapter
        dummydata()
    }

    private fun dummydata(){
        for(i in 0 until 10){
            listData.add(DummyData(i.toString()))
        }
        adapter.notifyDataSetChanged()
    }
}