package com.hastaprimasolusi.rana.ui.lp.laporan

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
import com.hastaprimasolusi.rana.adapter.page.ProdukCnvsAdapter
import com.hastaprimasolusi.rana.adapter.page.ProdukStokAdapter
import com.hastaprimasolusi.rana.data.ApiService
import com.hastaprimasolusi.rana.data.local.DummyData
import com.hastaprimasolusi.rana.data.network.response.produk.CategoryModel
import com.hastaprimasolusi.rana.data.network.response.produk.ProdukModel
import com.hastaprimasolusi.rana.ui.canvasser.CanvasViewModel
import com.hastaprimasolusi.rana.ui.common.ProgDialog
import com.hastaprimasolusi.rana.ui.lp.LpViewModel
import com.hastaprimasolusi.rana.utils.showAlert
import kotlinx.android.synthetic.main.fragment_canvas_stok.*
import org.koin.android.ext.android.inject

/**
 * Created By maasrahman on 2020-05-02
 */
class LpStokFragment: Fragment() {
    private val viewModel: LpViewModel by inject()
    private lateinit var adapter: ProdukStokAdapter
    private val prog = ProgDialog().getInstance()
    private var limit = 10
    private var offset = 0
    private var isWaitingData = false
    private var isLoadMore = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.stokbarang)
        return inflater.inflate(R.layout.fragment_canvas_stok, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btnKembali.visibility = View.GONE
        adapter = ProdukStokAdapter{

        }
        recyclerStok.layoutManager = LinearLayoutManager(activity)
        recyclerStok.itemAnimator = DefaultItemAnimator()
        recyclerStok.adapter = adapter
        recyclerStok.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (!isWaitingData && isLoadMore) {
                    if (linearLayoutManager != null &&
                        linearLayoutManager.findLastCompletelyVisibleItemPosition() == adapter.itemCount - 1) {
                        adapter.addProgress()
                        isWaitingData = true
                        viewModel.getProdukStok(limit.toString(), offset.toString(), false)
                    }
                }
            }
        })
        initViewModel()
    }

    private fun initViewModel(){
        viewModel.loadingStok.observe(viewLifecycleOwner, Observer {
            swipe.isRefreshing = it
        })

        viewModel.showErrorProd.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            viewModel.showErrorProd.postValue(null)
            adapter.removeProgress()
            showAlert(activity!!, it)
        })

        viewModel.listProdukStok.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            isLoadMore = it.size.rem(limit) == 0
            if(isWaitingData){
                isWaitingData = false
                offset += limit
                adapter.removeProgress()
            }
            adapter.updateData(it)
        })

        if(viewModel.listProdukStok.value.isNullOrEmpty()){
            reload()
        }

        swipe.setOnRefreshListener {
            reload()
        }
    }

    private fun reload(){
        isWaitingData = true
        offset = 0
        viewModel.getProdukStok(limit.toString(), offset.toString(), true)
    }

}