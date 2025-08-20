package com.hastaprimasolusi.rana.ui.mitra

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.adapter.SliderAdapterExample
import com.hastaprimasolusi.rana.adapter.page.KategoriMenuAdapter
import com.hastaprimasolusi.rana.adapter.page.ProdukDiskonAdapter
import com.hastaprimasolusi.rana.adapter.page.ProdukMenuAdapter
import com.hastaprimasolusi.rana.data.local.DummyData
import com.hastaprimasolusi.rana.data.local.SliderItem
import com.hastaprimasolusi.rana.data.network.response.produk.CategoryModel
import com.hastaprimasolusi.rana.ui.produk.ProdukActivity
import com.hastaprimasolusi.rana.ui.produk.ProdukDetailActivity
import com.hastaprimasolusi.rana.ui.produk.kategori.KategoriActivity
import com.hastaprimasolusi.rana.utils.showAlert
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.support.v4.startActivity
import org.koin.android.ext.android.inject

/**
 * Created By maasrahman on 2020-04-26
 */
class HomeFragment: Fragment() {
    private val vieWModel: MitraViewModel by inject()
    private lateinit var adapterKategori: KategoriMenuAdapter
    private val listDiskon = mutableListOf<DummyData>()
    private lateinit var adapterDiskon: ProdukDiskonAdapter
    private lateinit var adapterProduk: ProdukMenuAdapter
    private var limit = 10
    private var offset = 0
    private var isWaitingData = false
    private var isLoadMore = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(ComponentName(activity!!, ResultProdukActivity::class.java)))
        searchView.queryHint = getString(R.string.cariproduk)
        val etSearch = searchView.findViewById(androidx.appcompat.R.id.search_src_text) as EditText
        etSearch.hint = getString(R.string.cariproduk)
        adapterKategori =
            KategoriMenuAdapter{
                if (it.cATEGORYNAME == "Semua") {
                    startActivity<KategoriActivity>()
                } else {
                    vieWModel.categorySelected = it
                    startActivity<ProdukActivity>()
                }
            }
        recyclerKategori.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recyclerKategori.itemAnimator = DefaultItemAnimator()
        recyclerKategori.adapter = adapterKategori
        adapterDiskon =
            ProdukDiskonAdapter(listDiskon) {
                startActivity<ProdukDetailActivity>()
            }
        recyclerDiskon.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recyclerDiskon.itemAnimator = DefaultItemAnimator()
        recyclerDiskon.adapter = adapterDiskon

        adapterProduk = ProdukMenuAdapter{ prod ->
            vieWModel.produkDetailModel.postValue(null)
            startActivity<ProdukDetailActivity>("model" to prod)
        }

        val gridLayoutManager = GridLayoutManager(activity, 2)
        gridLayoutManager.spanSizeLookup = object: GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when(adapterProduk.getItemViewType(position)){
                    0 -> 1
                    else -> 2
                }
            }
        }
        recyclerProduk.apply {
            layoutManager = gridLayoutManager
            itemAnimator = DefaultItemAnimator()
            adapter = adapterProduk
        }

        btnLainnya.setOnClickListener {
            startActivity<ProdukActivity>("type" to "diskon")
        }

        nestedScroll.setOnScrollChangeListener { v: NestedScrollView?, _: Int, scrollY: Int, _: Int, _: Int ->
            if (scrollY == (v?.getChildAt(0)?.measuredHeight?.minus(v.measuredHeight))) {
                if (!isWaitingData && isLoadMore) //check for scroll down
                {
                    adapterProduk.addProgress()
                    vieWModel.getProduk(limit.toString(), offset.toString())
                    isWaitingData = true
                }
            }
        }

        initView()
    }

    private fun initView(){
        vieWModel.loadingProduk.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            progressProduk.visibility = if(it) View.VISIBLE else View.GONE
            progressKategori.visibility = if(it) View.VISIBLE else View.GONE
        })

        vieWModel.listProduk.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            isLoadMore = it.size.rem(limit) == 0
            if(isWaitingData){
                offset += limit
                isWaitingData = false
                adapterProduk.removeProgress()
            }
            adapterProduk.updateData(it)
        })

        vieWModel.listPromo.observe(viewLifecycleOwner, Observer {
            if(it.isNullOrEmpty()){
                sliderView.visibility = View.GONE
            }else{
                if(it.size == 0){
                    sliderView.visibility = View.GONE
                }else{
                    sliderView.visibility = View.VISIBLE
                    var promoAdapter = SliderAdapterExample()
                    var listSlider = mutableListOf<SliderItem>()
                    it.forEach { row ->
                        listSlider.add(SliderItem(imageUrl = row.bANNERIMG, title = ""))
                    }
                    promoAdapter.updateData(listSlider)
                    sliderView.setSliderAdapter(promoAdapter)
                    sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM)
                    sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
                    sliderView.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
                    sliderView.indicatorSelectedColor = Color.WHITE
                    sliderView.indicatorUnselectedColor = Color.GRAY
                    sliderView.scrollTimeInSec = 4
                    sliderView.startAutoCycle()
                }
            }
        })

        vieWModel.showErrorProd.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            vieWModel.showErrorProd.postValue(null)
            adapterProduk.removeProgress()
            showAlert(activity!!, it)
        })

        vieWModel.listCategory.observe(viewLifecycleOwner, Observer {
            if(it.isNullOrEmpty()) return@Observer
            val isExist = it.filter { kat -> kat.cATEGORYNAME == "Semua" }
            if(isExist.isNullOrEmpty()){
                val addSemua = CategoryModel()
                addSemua.cATEGORYNAME = "Semua"
                addSemua.cATEGORYID = "Semua"
                it.add(addSemua)
            }
            adapterKategori.updateData(it)
            recyclerKategori.visibility = View.VISIBLE
        })

        if(vieWModel.listProduk.value.isNullOrEmpty()){
            isWaitingData = true
            vieWModel.homeRequest(limit.toString(), offset.toString())
        }
    }

}