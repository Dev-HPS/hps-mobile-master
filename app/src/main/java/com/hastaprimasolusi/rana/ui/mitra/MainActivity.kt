package com.hastaprimasolusi.rana.ui.mitra

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.data.network.response.UserModel
import com.hastaprimasolusi.rana.ui.akun.AkunFragment
import com.hastaprimasolusi.rana.ui.login.LoginActivity
import com.hastaprimasolusi.rana.ui.mitra.keranjang.KeranjangFragment
import com.hastaprimasolusi.rana.ui.mitra.keranjang.chekout.PembayaranActivity
import com.hastaprimasolusi.rana.ui.mitra.produk.ProdukGrosirFragment
import com.hastaprimasolusi.rana.ui.mitra.riwayat.DetailRiwayatActivity
import com.hastaprimasolusi.rana.ui.mitra.riwayat.RiwayatFragment
import com.hastaprimasolusi.rana.ui.notif.NotificationActivity
import com.hastaprimasolusi.rana.utils.UtilsPref
import com.hastaprimasolusi.rana.utils.showAlert
import com.hastaprimasolusi.rana.utils.showalertInformation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.txtCount
import org.jetbrains.anko.startActivity
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
    BottomNavigationView.OnNavigationItemReselectedListener {

    private val initialFragment = mutableMapOf<String, Fragment>()
    private val viewModel: MitraViewModel by inject()
    private val mReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val data = intent?.extras
            startActivity<PembayaranActivity>("id" to data?.getString("id"))
        }
    }
    private val mIntentFilter= IntentFilter("PAYMENTS")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNav.setOnNavigationItemSelectedListener(this)
        bottomNav.setOnNavigationItemReselectedListener(this)
        initFragment()
        initViewModel()
        imgNotif.setOnClickListener {
            startActivity<NotificationActivity>()
        }
    }

    override fun onResume() {
        super.onResume()
        cekBadge()
        registerReceiver(mReceiver, mIntentFilter)
    }

    override fun onPause() {
        unregisterReceiver(mReceiver)
        super.onPause()
    }

    private fun cekBadge(){
        val jmlPesanan = viewModel.listCart.value ?: mutableListOf()
        if(jmlPesanan.size == 0){
            bottomNav.removeBadge(R.id.navCart)
        }else{
            bottomNav.getOrCreateBadge(R.id.navCart).number = jmlPesanan.size
        }
    }

    private fun initViewModel(){
        viewModel.userModel = Gson().fromJson(UtilsPref.loadString(getString(R.string.userData)), UserModel::class.java)
        viewModel.showError.observe(this, Observer {
            if(it == null) return@Observer
            showAlert(this@MainActivity, it)
            viewModel.showError.postValue(null)
        })

        viewModel.isUnAuthorized.observe(this, Observer {
            if(it == null) return@Observer
            if(it){
                showalertInformation(this@MainActivity, getString(R.string.sessiontelahhabis)){
                    viewModel.isUnAuthorized.postValue(false)
                    viewModel.clearData()
                    startActivity<LoginActivity>()
                    UtilsPref.saveBoolean(getString(R.string.isLoggedIn), false)
                    finish()
                }
            }
        })

        viewModel.listCart.observe(this@MainActivity, Observer {
            if(it == null) return@Observer
            cekBadge()
        })

//        if(viewModel.userModel?.rOLENAME == "grosir"){
//            bottomNav.menu.clear()
//            bottomNav.inflateMenu(R.menu.grosir_nav)
//        }
    }


    private fun initNotif(){
        viewModel.getUnreadNotif().observe(this@MainActivity, Observer {
            if(it.isNullOrEmpty()){
                txtCount.visibility = View.GONE
                pointer.visibility = View.GONE
            }else{
                pointer.visibility = View.VISIBLE
                txtCount.visibility = View.VISIBLE
                if(it.size > 9){
                    txtCount.text = "9+"
                }else{
                    txtCount.text = it.size.toString()
                }
            }
        })
    }

    private fun initFragment() {
        initialFragment[getString(R.string.beranda)] =
            HomeFragment()
        initialFragment[getString(R.string.keranjang)] = KeranjangFragment()
        initialFragment[getString(R.string.riwayat)] = RiwayatFragment()
        initialFragment[getString(R.string.akun)] = AkunFragment()
        initialFragment[getString(R.string.produk)] = ProdukGrosirFragment()
        loadHome()

        val bundle = intent.extras
        if(bundle != null){
            startActivity<DetailRiwayatActivity>("id" to bundle.getString("id"))
        }
    }

    private fun loadHome(){
        supportFragmentManager.beginTransaction()
            .add(R.id.frame, initialFragment[getString(R.string.beranda)] ?: HomeFragment())
            .commit()
    }

    private fun replaceFragment(frag: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, frag)
            .addToBackStack(null)
            .commit()
    }

    fun changeNav(item: Int){
        bottomNav.selectedItemId = item
    }

    fun changeNavV2(item: Int){
        val view = bottomNav.findViewById<View>(item)
        view.performClick()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.navHome -> {
                replaceFragment(initialFragment[getString(R.string.beranda)] ?: HomeFragment())
            }
            R.id.navCart -> {
                replaceFragment(initialFragment[getString(R.string.keranjang)] ?: KeranjangFragment())
            }
            R.id.navRiwayat -> {
                replaceFragment(initialFragment[getString(R.string.riwayat)] ?: RiwayatFragment())
            }
            R.id.navAkun -> {
                replaceFragment(initialFragment[getString(R.string.akun)] ?: AkunFragment())
            }
            R.id.navProduk -> {
                replaceFragment(initialFragment[getString(R.string.produk)] ?: ProdukGrosirFragment())
            }
        }
        return true
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val bundle = intent?.extras
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.top_item_menu, menu);
//        val search = MenuItemCompat.getActionView(menu?.findItem(R.id.action_search)) as SearchView
//        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
//        search.setSearchableInfo(searchManager.getSearchableInfo( ComponentName(this, ResultProdukActivity::class.java)))
//        search.queryHint = getString(R.string.cariproduk);
        initNotif()
        return true
    }

    override fun onNavigationItemReselected(item: MenuItem) {

    }
}
