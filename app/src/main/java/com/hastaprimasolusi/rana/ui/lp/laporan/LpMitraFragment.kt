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
import com.hastaprimasolusi.rana.adapter.page.MitraAdapter
import com.hastaprimasolusi.rana.data.local.DummyData
import kotlinx.android.synthetic.main.fragment_lp_mitra.*

/**
 * Created By maasrahman on 5/8/20
 */
class LpMitraFragment: Fragment() {

    private val listData = mutableListOf<DummyData>()
    private lateinit var adapter: MitraAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.mitra)
        return inflater.inflate(R.layout.fragment_lp_mitra, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = MitraAdapter(listData)
        recyclerMitra.layoutManager = LinearLayoutManager(activity)
        recyclerMitra.itemAnimator = DefaultItemAnimator()
        recyclerMitra.adapter = adapter
        dummydata()
    }

    private fun dummydata(){
        for(i in 0 until 10){
            listData.add(DummyData(i.toString()))
        }
        adapter.notifyDataSetChanged()
    }
}