package com.hastaprimasolusi.rana.ui.lp

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.data.network.response.UserModel
import com.hastaprimasolusi.rana.ui.login.LoginActivity
import com.hastaprimasolusi.rana.ui.lp.akun.LpProfileFragment
import com.hastaprimasolusi.rana.ui.lp.kembalibarang.DaftarPengembalianFragment
import com.hastaprimasolusi.rana.ui.lp.laporan.LpStokFragment
import com.hastaprimasolusi.rana.ui.lp.order.OrderDetailFragment
import com.hastaprimasolusi.rana.ui.notif.ListNotifFragment
import com.hastaprimasolusi.rana.ui.report.ReportViewModel
import com.hastaprimasolusi.rana.ui.report.mutasi.MutasiActivity
import com.hastaprimasolusi.rana.ui.report.transaksi.TransReportActivity
import com.hastaprimasolusi.rana.utils.UtilsPref
import com.hastaprimasolusi.rana.utils.showalertConfirmation
import com.hastaprimasolusi.rana.utils.showalertInformation
import kotlinx.android.synthetic.main.activity_lp_main.*
import kotlinx.android.synthetic.main.nav_header.view.*
import org.jetbrains.anko.startActivity
import org.koin.android.ext.android.inject

/**
 * Created By maasrahman on 5/7/20
 */
class LpMainActivity: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val viewModel: LpViewModel by inject()
    private val reportModel: ReportViewModel by inject()
    private var txtCount: TextView? = null
    private var imgPointer: ImageView? = null

    //attendance
    var clockIn: TextView? = null
    var clockOut: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lp_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.title = getString(R.string.app_name)
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        //attendance
        clockIn = MenuItemCompat.getActionView(navigationView.menu.findItem(R.id.absenMasuk)) as TextView
        clockIn?.gravity = Gravity.CENTER_VERTICAL
        clockIn?.setTypeface(null, Typeface.BOLD)
        clockIn?.setTextColor(ContextCompat.getColor(this, R.color.green))
        clockOut = MenuItemCompat.getActionView(navigationView.menu.findItem(R.id.absenKeluar)) as TextView
        clockOut?.gravity = Gravity.CENTER_VERTICAL
        clockOut?.setTypeface(null, Typeface.BOLD)
        clockOut?.setTextColor(ContextCompat.getColor(this, R.color.red))

        clockIn?.text = "--:--"
        clockOut?.text = "--:--"
        navigationView.setNavigationItemSelectedListener(this)
        initViewModel()
    }

    private fun initViewModel(){
        viewModel.userModel = Gson().fromJson(UtilsPref.loadString(getString(R.string.userData)), UserModel::class.java)
        reportModel.userModel = viewModel.userModel
        initFragment()
        viewModel.showError.observe(this, Observer {
            if(it == null) return@Observer
            showalertInformation(this@LpMainActivity, it){
                viewModel.showError.postValue(null)
            }
        })

        viewModel.isUnAuthorized.observe(this, Observer {
            if(it == null) return@Observer
            if(it){
                showalertInformation(this@LpMainActivity, getString(R.string.sessiontelahhabis)){
                    viewModel.isUnAuthorized.postValue(false)
                    startActivity<LoginActivity>()
                    UtilsPref.saveBoolean(getString(R.string.isLoggedIn), false)
                    finish()
                }
            }
        })
    }

    private fun initFragment(){
        val headerView = navigationView.getHeaderView(0)
        Glide.with(this)
            .load(viewModel.userModel?.pICTURE.toString())
            .apply(RequestOptions().error(R.drawable.ic_account).circleCrop())
            .into(headerView.imgProfile)
        headerView.txtNama.text = viewModel.userModel?.nAME
        headerView.txtPrivilage.text = viewModel.userModel?.rOLEDISPLAYNAME

        val bundle = intent.extras
        if(bundle != null){
            toDetail(bundle)
        }else{
            supportFragmentManager.beginTransaction()
                .add(R.id.frame, LpHomeFragment())
                .commit()
        }
        viewModel.updateImage.observe(this, Observer {
            Glide.with(this)
                .load(viewModel.userModel?.pICTURE.toString())
                .apply(RequestOptions().error(R.drawable.ic_account).circleCrop())
                .into(headerView.imgProfile)
        })
    }

    private fun toDetail(bundle: Bundle){
        val args = Bundle()
        val frag = OrderDetailFragment()
        args.putString("id", bundle.getString("id"))
        frag.arguments = args
        supportFragmentManager.beginTransaction()
            .add(R.id.frame, frag)
            .commit()
    }

    private fun replaceFragment(frag: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, frag)
            .addToBackStack(null)
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawer_layout.closeDrawer(GravityCompat.START)
        when(item.itemId){
            R.id.menuDashboard -> {
                replaceFragment(LpHomeFragment())
            }
            R.id.navPenjualan -> {
//                replaceFragment(LpPenjualanFragment())
            }
            R.id.navStok -> {
                replaceFragment(LpStokFragment())
            }
            R.id.navTransaksi -> {
                startActivity<TransReportActivity>()
            }
            R.id.navMutasi -> {
                startActivity<MutasiActivity>()
            }
            R.id.navPengembalian -> {
                replaceFragment(DaftarPengembalianFragment())
            }
//            R.id.navCanvaser -> {
//                replaceFragment(LpCanvasserFragment())
//            }
//            R.id.navWarung -> {
//                replaceFragment(LpMitraFragment())
//            }
            R.id.navProfile -> {
                replaceFragment(LpProfileFragment())
            }
            R.id.navUbahPassword -> {
//                startActivity<ProfileActivity>("type" to "pass")
            }
            R.id.navLogout -> {
                showalertConfirmation(this@LpMainActivity, getString(R.string.yakinkeluar)){
                    UtilsPref.saveBoolean(getString(R.string.isLoggedIn), false)
                    startActivity<LoginActivity>()
                    finish()
                }
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun initNotif(){
        viewModel.getUnreadNotif().observe(this, Observer {
            if(it.isNullOrEmpty()){
                imgPointer?.visibility = View.GONE
                txtCount?.visibility = View.GONE
            }else{
                imgPointer?.visibility = View.VISIBLE
                txtCount?.visibility = View.VISIBLE
                if(it.size > 9){
                    txtCount?.text = "9+"
                }else{
                    txtCount?.text = it.size.toString()
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_item_menu, menu)
        val notificaitons: View? = menu?.findItem(R.id.menuNotif)?.actionView
        txtCount = notificaitons?.findViewById(R.id.txtCount) as TextView
        imgPointer = notificaitons.findViewById(R.id.pointer) as ImageView
        notificaitons.setOnClickListener {
            supportActionBar?.title = "Notification"
            replaceFragment(ListNotifFragment())
        }
        initNotif()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> { onBackPressed() }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val bundle = intent?.extras
        bundle?.let {
            if(bundle.containsKey("id")){
                toDetail(bundle)
            }
        }
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount > 0){
            supportFragmentManager.popBackStack()
        }else{
            showalertConfirmation(this@LpMainActivity, "Keluar Aplikasi?"){
                super.onBackPressed()
            }
        }
    }
}