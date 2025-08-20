package com.hastaprimasolusi.rana.ui.mitra.produk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.adapter.page.ProdukStokAdapter
import com.hastaprimasolusi.rana.ui.common.ProgDialog
import com.hastaprimasolusi.rana.ui.mitra.MitraViewModel
import com.hastaprimasolusi.rana.utils.showAlert
import kotlinx.android.synthetic.main.fragment_produk_grosir.*
import org.koin.android.ext.android.inject

/**
 * Created by maasrahman on 24/09/20.
 */

class ProdukGrosirFragment: Fragment() {
    private val viewModel: MitraViewModel by inject()
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
        return inflater.inflate(R.layout.fragment_produk_grosir, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
                        isWaitingData = true
                        recyclerStok.post {
                            adapter.addProgress()
                            viewModel.getProdukStok(limit.toString(), offset.toString(), false)
                        }
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

        swipe.setOnRefreshListener {
            reload()
        }

        if(viewModel.listProdukStok.value.isNullOrEmpty()){
            reload()
        }
    }

    private fun reload(){
        isWaitingData = true
        offset = 0
        viewModel.getProdukStok(limit.toString(), offset.toString(), true)
    }
}