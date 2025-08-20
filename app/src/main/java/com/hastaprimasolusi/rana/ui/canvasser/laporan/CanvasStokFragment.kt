package com.hastaprimasolusi.rana.ui.canvasser.laporan

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
import com.hastaprimasolusi.rana.adapter.page.ProdukStokAdapter
import com.hastaprimasolusi.rana.ui.canvasser.CanvasViewModel
import com.hastaprimasolusi.rana.ui.canvasser.order.DaftarLpFragment
import com.hastaprimasolusi.rana.utils.showAlert
import kotlinx.android.synthetic.main.fragment_canvas_stok.*
import org.koin.android.ext.android.inject

/**
 * Created By maasrahman on 2020-05-02
 */
class CanvasStokFragment: Fragment() {
    private val viewModel: CanvasViewModel by inject()
    private lateinit var adapter: ProdukStokAdapter
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

                if (dy > 0 && btnKembali.visibility == View.VISIBLE) {
                    btnKembali.hide()
                } else if (dy < 0 && btnKembali.visibility != View.VISIBLE) {
                    btnKembali.show()
                }
            }
        })
        btnKembali.setOnClickListener {
            val args = Bundle()
            val frag = DaftarLpFragment()
            args.putString("test", "stokopname")
            frag.arguments = args
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.frame, frag)
                ?.addToBackStack(null)
                ?.commit()
        }
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
            isWaitingData = true
            offset = 0
            viewModel.getProdukStok(limit.toString(), offset.toString(), true)
        }

        adapter.updateData(emptyList())
        isWaitingData = true
        offset = 0
        viewModel.getProdukStok(limit.toString(), offset.toString(), true)
    }

}