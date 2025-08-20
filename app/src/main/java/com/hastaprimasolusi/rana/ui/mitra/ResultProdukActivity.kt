package com.hastaprimasolusi.rana.ui.mitra

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.adapter.page.ProdukMenuAdapter
import com.hastaprimasolusi.rana.ui.produk.ProdukDetailActivity
import com.hastaprimasolusi.rana.utils.showAlert
import com.hastaprimasolusi.rana.utils.showalertInformation
import kotlinx.android.synthetic.main.activity_list_kategori.toolbar
import kotlinx.android.synthetic.main.activity_result_produk.*
import org.jetbrains.anko.startActivity
import org.koin.android.ext.android.inject

/**
 * Created By maasrahman on 5/27/20
 */
class ResultProdukActivity: AppCompatActivity() {
    private val viewModel: MitraViewModel by inject()
    private lateinit var adapter: ProdukMenuAdapter
    private var limit = 10
    private var offset = 0
    private var isWaitingData = false
    private var isLoadMore = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_produk)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = getString(R.string.pencarianproduk)

        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            query?.let { initViewModel(it) }
        }else{
            showalertInformation(this, getString(R.string.terjadikesalahansaatmemuatdata)){
                finish()
            }
        }
    }

    private fun initViewModel(paramString: String) {
        adapter = ProdukMenuAdapter {
            viewModel.produkDetailModel.postValue(null)
            startActivity<ProdukDetailActivity>("model" to it)
        }
        recyclerProduk.layoutManager = GridLayoutManager(this, 2)
        recyclerProduk.itemAnimator = DefaultItemAnimator()
        recyclerProduk.adapter = adapter
        recyclerProduk.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (!isWaitingData && isLoadMore) {
                    if (linearLayoutManager != null &&
                        linearLayoutManager.findLastCompletelyVisibleItemPosition() == adapter.itemCount - 1) {
                        adapter.addProgress()
                        isWaitingData = true
                        viewModel.getProdukBySearch(paramString, limit.toString(), offset.toString(), false)
                    }
                }
            }
        })

        viewModel.showErrorProd.observe(this, Observer {
            if (it == null) return@Observer
            viewModel.showErrorProd.postValue(null)
            adapter.removeProgress()
            showAlert(this@ResultProdukActivity, it)
        })

        viewModel.loadingProduk.observe(this, Observer {
            progress.visibility = if(it) View.VISIBLE else View.GONE
        })

        viewModel.listProdukSearch.observe(this, Observer {
            if(it == null) return@Observer
            isLoadMore = it.size.rem(limit) == 0
            if(isWaitingData){
                adapter.removeProgress()
                isWaitingData = false
                offset += limit
            }
            adapter.updateData(it)
        })

        viewModel.getProdukBySearch(paramString, limit.toString(), offset.toString(), true)
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}