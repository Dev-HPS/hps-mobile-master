package com.hastaprimasolusi.rana.ui.canvasser

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.adapter.page.RiwayatCnvsAdapter
import com.hastaprimasolusi.rana.helper.AttendanceHelper
import com.hastaprimasolusi.rana.helper.LocationHelper
import com.hastaprimasolusi.rana.ui.CustomCameraActivity
import com.hastaprimasolusi.rana.ui.canvasser.riwayat.RiwayatDetailCanvasFragment
import com.hastaprimasolusi.rana.ui.common.ProgDialog
import com.hastaprimasolusi.rana.ui.report.ReportViewModel
import com.hastaprimasolusi.rana.utils.millisToDate
import com.hastaprimasolusi.rana.utils.showAlert
import kotlinx.android.synthetic.main.dialog_attendance.*
import kotlinx.android.synthetic.main.fragment_canvas_home.*
import org.koin.android.ext.android.inject
import java.io.ByteArrayOutputStream
import java.io.File

/**
 * Created By maasrahman on 2020-05-02
 */
class CanvasHomeFragment : Fragment() {
    private val viewModel: CanvasViewModel by inject()
    private val reportModel: ReportViewModel by inject()
    private lateinit var adapter: RiwayatCnvsAdapter
    private var statusSelected = MutableLiveData<String>()
    private var offset = 0
    private var limit = 10
    private var isWaitingData = false
    private var isLoadMore = false
    private var isFirstLoading = true
    private var dateFirst: Long? = null
    private var dateEnd: Long? = null

    private lateinit var attendanceHelper: AttendanceHelper
    private lateinit var locationHelper: LocationHelper
    private val progress = ProgDialog().getInstance()
    private lateinit var dialog: Dialog
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.app_name)
        return inflater.inflate(R.layout.fragment_canvas_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        attendanceHelper = AttendanceHelper(requireActivity())
        locationHelper = LocationHelper(requireActivity())
        adapter = RiwayatCnvsAdapter {
            viewModel.historyDetail.postValue(null)
            val frag = RiwayatDetailCanvasFragment()
            val args = Bundle()
            args.putString("id", it?.iD)
            frag.arguments = args
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.frame, frag)
                ?.addToBackStack(null)
                ?.commit()
        }
        recyclerPesanan.layoutManager = LinearLayoutManager(activity)
        recyclerPesanan.itemAnimator = DefaultItemAnimator()
        recyclerPesanan.adapter = adapter
        recyclerPesanan.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (!isWaitingData && isLoadMore) {
                    if (linearLayoutManager != null &&
                        linearLayoutManager.findLastCompletelyVisibleItemPosition() == adapter.itemCount - 1
                    ) {
                        adapter.addProgress()
                        isWaitingData = true
                        viewModel.getPesanan(
                            limit.toString(),
                            offset.toString(),
                            getStatus(),
                            false
                        )
                    }
                }
                if (dy > 0 && btnFilter.visibility == View.VISIBLE) {
                    btnFilter.hide()
                } else if (dy < 0 && btnFilter.visibility != View.VISIBLE) {
                    btnFilter.show()
                }
            }
        })
        btnFilter.setOnClickListener(listener)
        btnClose.setOnClickListener(listener)
        btnReset.setOnClickListener(listener)
        btnSubmit.setOnClickListener(listener)
        etFilterTanggal.setOnClickListener(listener)
        initViewModel()
    }

    private val listener = View.OnClickListener { view ->
        when (view.id) {
            R.id.btnFilter -> {
                layoutFilter.visibility = View.VISIBLE
                btnFilter.hide()


            }

            R.id.btnClose -> {
                resetFilter(false)
            }

            R.id.btnReset -> {
                resetFilter(true)
            }

            R.id.etFilterTanggal -> {
                val builder = MaterialDatePicker.Builder.dateRangePicker()
                    .setTheme(R.style.ThemeOverlay_MaterialComponents_MaterialCalendar)
                val picker = builder.build()
                picker.show(childFragmentManager, picker.toString())
                picker.addOnPositiveButtonClickListener { date ->
                    dateFirst = date.first
                    dateEnd = date.second
                    etFilterTanggal.setText("${
                        date.first?.let {
                            millisToDate(
                                it,
                                "dd/MM/yyyy"
                            )
                        }
                    } - " +
                            "${date.second?.let { millisToDate(it, "dd/MM/yyyy") }}"
                    )
                }
            }

            R.id.btnSubmit -> {
                if (etFilterTanggal.text.isNullOrEmpty()) {
                    etFilterTanggal.error = getString(R.string.wajibdiisi)
                    return@OnClickListener
                }
                viewModel.dtStart = dateFirst?.let { millisToDate(it, "yyyy-MM-dd") } ?: ""
                viewModel.dtEnd = dateEnd?.let { millisToDate(it, "yyyy-MM-dd") } ?: ""
                layoutFilter.visibility = View.GONE
                btnFilter.show()
                loadDataWithLoading(getStatus())
            }
        }
    }

    private fun resetFilter(isReload: Boolean) {
        viewModel.dtStart = ""
        viewModel.dtEnd = ""
        layoutFilter.visibility = View.GONE
        btnFilter.show()
        if (isReload) {
            etFilterTanggal.text?.clear()
            loadDataWithLoading(getStatus())
        }
    }

    private fun initViewModel() {
        viewModel.listHistory.observe(viewLifecycleOwner, Observer {
            layoutNoData.visibility = if (it.isNullOrEmpty()) View.VISIBLE else View.GONE
            if (it == null) return@Observer
            isLoadMore = it.size.rem(limit) == 0
            if (isWaitingData) {
                adapter.removeProgress()
                isWaitingData = false
                offset += limit
            }
            adapter.updateData(it)
        })

        viewModel.loadingHistory.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            swipe.isRefreshing = it
        })

        viewModel.listStatus.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            reportModel.listStatus.postValue(it.toMutableList())
            if (chipGroup.childCount <= 1) {
                it.forEach { row ->
                    val chip =
                        activity?.layoutInflater?.inflate(R.layout.item_chip, null, false) as Chip
                    chip.text = row.sTSTEXT
                    chip.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            statusSelected.postValue(row.sTSCODE.toString())
                        }
                    }
                    chipGroup.addView(chip)
                }
                chipSemua.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        statusSelected.postValue("")
                    }
                }
            }
        })

        viewModel.showErrorPesanan.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            viewModel.showErrorPesanan.postValue(null)
            adapter.removeProgress()
            showAlert(activity!!, it)
        })

        statusSelected.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            adapter.updateData(emptyList())
            loadDataWithLoading(it)
        })

        swipe.setOnRefreshListener {
            loadDataWithLoading(getStatus())
        }

//        if(viewModel.listHistory.value.isNullOrEmpty()){
        if (isFirstLoading) {
            isWaitingData = true
            offset = 0
            isFirstLoading = false
            viewModel.homeRequest(limit.toString(), offset.toString(), getStatus())
        } else {
            loadDataWithLoading(getStatus())
        }

        viewModel.showAttendance.observe(viewLifecycleOwner, Observer {
            if (it == null || it == true) return@Observer
//            viewModel.showErrorPesanan.postValue(null)
//            dialog = Dialog(activity!!)
//            dialog.setContentView(R.layout.dialog_attendance)
//            dialog.setCancelable(false)
//            dialog.setCanceledOnTouchOutside(false)
//            dialog.btnCLockIn.setOnClickListener {
//                activity?.supportFragmentManager?.popBackStack()
//                showDialogImage()
//            }
//            dialog.show()
//            val window = dialog.window
//            window?.setLayout(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//            )
        })


    }

    private fun loadDataWithLoading(status: String) {
        adapter.updateData(emptyList())
        isWaitingData = true
        offset = 0
        viewModel.getPesanan(limit.toString(), offset.toString(), status, true)
    }

    private fun getStatus(): String {
        return statusSelected.value ?: ""
    }

    private fun showDialogImage() {
//
        val intent = Intent(requireContext(), CustomCameraActivity::class.java)
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

                    progress.show(requireContext())
                    dialog.dismiss()
                    locationHelper.startLocationUpdates()
                    locationHelper.getLastKnownLocation { location ->
                        progress.dismiss()
                        location?.let {
                            val lat = it.latitude
                            val lng = it.longitude
                            val address = locationHelper.getAddressFromLocation(lat, lng)
                            Toast.makeText(
                                requireContext(),
                                "Last known location: $lat, $lng, $address",
                                Toast.LENGTH_SHORT
                            ).show()
                            viewModel.attendance(
                                "IN",
                                "$address",
                                "$base64Image",
                                "$lat",
                                "$lng"
                            )
                        } ?: run {
                            Toast.makeText(
                                requireContext(),
                                "Last known location is not available",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    locationHelper.stopLocationUpdates()
                } else {
                    Toast.makeText(requireContext(), "Failed to decode image", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "No photo returned", Toast.LENGTH_SHORT).show()
            }
        }
    }


}