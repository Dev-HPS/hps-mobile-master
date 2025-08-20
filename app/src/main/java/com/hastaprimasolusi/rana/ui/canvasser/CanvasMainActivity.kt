package com.hastaprimasolusi.rana.ui.canvasser

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.os.Bundle
import android.util.Base64
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
import com.hastaprimasolusi.rana.helper.AttendanceHelper
import com.hastaprimasolusi.rana.helper.LocationHelper
import com.hastaprimasolusi.rana.ui.CustomCameraActivity
import com.hastaprimasolusi.rana.ui.akun.ProfileActivity
import com.hastaprimasolusi.rana.ui.canvasser.akun.CanvasProfileFragment
import com.hastaprimasolusi.rana.ui.canvasser.laporan.CanvasStokFragment
import com.hastaprimasolusi.rana.ui.canvasser.order.DaftarLpFragment
import com.hastaprimasolusi.rana.ui.canvasser.penjualan.CanvasPenjualanFragment
import com.hastaprimasolusi.rana.ui.canvasser.penjualan.toko.ListTokoFragment
import com.hastaprimasolusi.rana.ui.canvasser.riwayat.RiwayatDetailCanvasFragment
import com.hastaprimasolusi.rana.ui.common.ProgDialog
import com.hastaprimasolusi.rana.ui.login.LoginActivity
import com.hastaprimasolusi.rana.ui.notif.ListNotifFragment
import com.hastaprimasolusi.rana.ui.report.ReportViewModel
import com.hastaprimasolusi.rana.ui.report.mutasi.MutasiActivity
import com.hastaprimasolusi.rana.ui.report.transaksi.TransReportActivity
import com.hastaprimasolusi.rana.utils.UtilsPref
import com.hastaprimasolusi.rana.utils.showalertConfirmation
import com.hastaprimasolusi.rana.utils.showalertInformation
import kotlinx.android.synthetic.main.activity_canvas_main.*
import kotlinx.android.synthetic.main.nav_header.view.*
import org.jetbrains.anko.startActivity
import org.koin.android.ext.android.inject
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.system.exitProcess

/**
 * Created By maasrahman on 2020-05-02
 */
class CanvasMainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val viewModel: CanvasViewModel by inject()
    private val reportModel: ReportViewModel by inject()
    var itemOrder: TextView? = null
    var itemSell: TextView? = null
    var txtCount: TextView? = null
    var imgPointer: ImageView? = null

    //attendance
    var clockIn: TextView? = null
    var clockOut: TextView? = null
    private var statusAttendance = "IN"

    private lateinit var attendanceHelper: AttendanceHelper
    private lateinit var locationHelper: LocationHelper
    private val progress = ProgDialog().getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canvas_main)
        //drawer layout
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.title = getString(R.string.app_name)
        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)

        itemOrder =
            MenuItemCompat.getActionView(navigationView.menu.findItem(R.id.navOrder)) as TextView
        itemOrder?.gravity = Gravity.CENTER_VERTICAL
        itemOrder?.setTypeface(null, Typeface.BOLD)
        itemOrder?.setTextColor(ContextCompat.getColor(this, R.color.deep_orange))
        itemSell =
            MenuItemCompat.getActionView(navigationView.menu.findItem(R.id.navPenjualan)) as TextView
        itemSell?.gravity = Gravity.CENTER_VERTICAL
        itemSell?.setTypeface(null, Typeface.BOLD)
        itemSell?.setTextColor(ContextCompat.getColor(this, R.color.teal))

        itemOrder?.text = "0 Barang"
        itemSell?.text = "0 Barang"

        //attendance
        clockIn =
            MenuItemCompat.getActionView(navigationView.menu.findItem(R.id.absenMasuk)) as TextView
        clockIn?.gravity = Gravity.CENTER_VERTICAL
        clockIn?.setTypeface(null, Typeface.BOLD)
        clockIn?.setTextColor(ContextCompat.getColor(this, R.color.green))
        clockOut =
            MenuItemCompat.getActionView(navigationView.menu.findItem(R.id.absenKeluar)) as TextView
        clockOut?.gravity = Gravity.CENTER_VERTICAL
        clockOut?.setTypeface(null, Typeface.BOLD)
        clockOut?.setTextColor(ContextCompat.getColor(this, R.color.red))

        clockIn?.text = "--:--"
        clockOut?.text = "--:--"
        attendanceHelper = AttendanceHelper(this)
        locationHelper = LocationHelper(this)
        initViewModel()
    }

    private fun initViewModel() {
        println("INIT VIEW MODEL")
        viewModel.userModel = Gson().fromJson(
            UtilsPref.loadString(getString(R.string.userData)), UserModel::class.java
        )
        reportModel.userModel = viewModel.userModel
        viewModel.showError.observe(this@CanvasMainActivity, Observer {
            if (it == null) return@Observer
            showalertInformation(this@CanvasMainActivity, it) {
                viewModel.showError.postValue(null)
            }
        })

        viewModel.isUnAuthorized.observe(this@CanvasMainActivity, Observer {
            if (it == null) return@Observer
            if (it) {
                showalertInformation(
                    this@CanvasMainActivity, getString(R.string.sessiontelahhabis)
                ) {
                    viewModel.isUnAuthorized.postValue(false)
                    startActivity<LoginActivity>()
                    UtilsPref.saveBoolean(getString(R.string.isLoggedIn), false)
                    finish()
                }
            }
        })

        viewModel.listCart.observe(this@CanvasMainActivity, Observer {
            if (it == null) {
                itemOrder?.text = "0 Barang"
            } else {
                println("MASUK JUMLAH CART BARANG")
                itemOrder?.text = "${it.size} Barang"
            }
        })

        viewModel.showClockIn.observe(this@CanvasMainActivity, Observer {
            if (it == null) return@Observer
            clockIn?.text = it
        })
        viewModel.showClockOut.observe(this@CanvasMainActivity, Observer {
            if (it == null) return@Observer
            clockOut?.text = it
        })

//        viewModel.listPos.observe(this@CanvasMainActivity, Observer {
//            if(it == null){
//                itemSell?.text = "0 Barang"
//            }else{
//                itemSell?.text = "${it.size} Barang"
//            }
//        })

        initFragment()
    }

    private fun initNotif() {
        viewModel.getUnreadNotif().observe(this@CanvasMainActivity, Observer {
            if (it.isNullOrEmpty()) {
                txtCount?.visibility = View.GONE
                imgPointer?.visibility = View.GONE
            } else {
                imgPointer?.visibility = View.VISIBLE
                txtCount?.visibility = View.VISIBLE
                if (it.size > 9) {
                    txtCount?.text = "9+"
                } else {
                    txtCount?.text = it.size.toString()
                }
            }
        })
    }

    private fun initFragment() {
        val headerView = navigationView.getHeaderView(0)
        Glide.with(this).load(viewModel.userModel?.pICTURE.toString())
            .apply(RequestOptions().error(R.drawable.ic_account).circleCrop())
            .into(headerView.imgProfile)
        headerView.txtNama.text = viewModel.userModel?.nAME
        headerView.txtPrivilage.text = viewModel.userModel?.rOLEDISPLAYNAME

        val bundle = intent.extras
        if (bundle != null) {
            toDetail(bundle)
        } else {
            supportFragmentManager.beginTransaction().add(R.id.frame, CanvasHomeFragment()).commit()
        }
        viewModel.updateImage.observe(this, Observer {
            Glide.with(this).load(viewModel.userModel?.pICTURE.toString())
                .apply(RequestOptions().error(R.drawable.ic_account).circleCrop())
                .into(headerView.imgProfile)
        })
    }

    private fun toDetail(bundle: Bundle) {
        val args = Bundle()
        val frag = RiwayatDetailCanvasFragment()
        args.putString("id", bundle.getString("id"))
        frag.arguments = args
        supportFragmentManager.beginTransaction().add(R.id.frame, frag).commitAllowingStateLoss()
    }

    private fun replaceFragment(frag: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frame, frag).addToBackStack(null)
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawer_layout.closeDrawer(GravityCompat.START)
        when (item.itemId) {
//            R.id.menuDashboard -> {
//                replaceFragment(CanvasHomeFragment())
//            }
            R.id.navPenjualan -> {
                if (viewModel.userModel?.rOLENAME == "spg" || viewModel.userModel?.rOLENAME == "msr") {
                    replaceFragment(CanvasPenjualanFragment())
                } else {
                    replaceFragment(ListTokoFragment())
                }
            }

            R.id.navRiwayat -> {
                replaceFragment(CanvasHomeFragment())
            }

            R.id.navTransaksi -> {
                startActivity<TransReportActivity>()
            }

            R.id.navMutasi -> {
                startActivity<MutasiActivity>()
            }
//            R.id.navLaporan -> {
//                replaceFragment(CanvasLaporanFragment())
//            }
            R.id.navOrder -> {
//                replaceFragment(OrderCanvasFragment())
                replaceFragment(DaftarLpFragment())
            }

            R.id.navStok -> {
                replaceFragment(CanvasStokFragment())
            }

            R.id.navProfile -> {
                replaceFragment(CanvasProfileFragment())
            }

            R.id.navUbahPassword -> {
                startActivity<ProfileActivity>("type" to "profile")
            }

            R.id.navLogout -> {
                showalertConfirmation(this@CanvasMainActivity, getString(R.string.yakinkeluar)) {
                    UtilsPref.saveBoolean(getString(R.string.isLoggedIn), false)
                    startActivity<LoginActivity>()
                    finish()
                    exitProcess(0)
                }
            }

            R.id.absenMasuk -> {
                showDialogImage()
                statusAttendance = "IN"
            }

            R.id.absenKeluar -> {
                showDialogImage()
                statusAttendance = "OUT"

            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_item_menu, menu)
        val notificaitons: View? = menu?.findItem(R.id.menuNotif)?.actionView
        txtCount = notificaitons?.findViewById(R.id.txtCount) as TextView
        imgPointer = notificaitons.findViewById(R.id.pointer) as ImageView
        notificaitons.setOnClickListener {
            replaceFragment(ListNotifFragment())
        }
        initNotif()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val bundle = intent?.extras
        bundle?.let {
            if (bundle.containsKey("id")) {
                toDetail(bundle)
            }
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            showalertConfirmation(this@CanvasMainActivity, "Keluar Aplikasi?") {
                super.onBackPressed()
            }
        }
    }

    private fun showDialogImage() {
        val intent = Intent(this, CustomCameraActivity::class.java)
        startActivityForResult(intent, 88)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 88 && resultCode == RESULT_OK) {
            val photoPath = data?.getStringExtra("photo_path")
            if (!photoPath.isNullOrEmpty()) {
                val photoFile = File(photoPath)
                val imageBitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                if (imageBitmap != null) {
                    val baos = ByteArrayOutputStream()
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos)
                    val bytes = baos.toByteArray()
                    val base64Image = Base64.encodeToString(bytes, Base64.DEFAULT)

                    progress.show(this)
                    locationHelper.startLocationUpdates()
                    locationHelper.getLastKnownLocation { location ->
                        progress.dismiss()
                        location?.let {
                            val lat = it.latitude
                            val lng = it.longitude
                            val address = locationHelper.getAddressFromLocation(lat, lng)
                            Toast.makeText(
                                this,
                                "Last known location: $lat, $lng, $address",
                                Toast.LENGTH_SHORT
                            ).show()
                            viewModel.attendance(
                                statusAttendance,
                                address,
                                base64Image,
                                "$lat",
                                "$lng"
                            )
                        } ?: run {
                            Toast.makeText(
                                this,
                                "Last known location is not available",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    locationHelper.stopLocationUpdates()
                } else {
                    Toast.makeText(this, "Failed to decode image", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "No photo returned", Toast.LENGTH_SHORT).show()
            }
        }
    }

}