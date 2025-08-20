package com.hastaprimasolusi.rana.ui.canvasser.penjualan.toko

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.data.network.requesthelper.RegisterRequest
import com.hastaprimasolusi.rana.data.network.response.master.KabKotaModel
import com.hastaprimasolusi.rana.data.network.response.master.KecamatanModel
import com.hastaprimasolusi.rana.data.network.response.master.KelurahanModel
import com.hastaprimasolusi.rana.data.network.response.master.ProvinsiModel
import com.hastaprimasolusi.rana.helper.LocationHelper
import com.hastaprimasolusi.rana.ui.canvasser.CanvasViewModel
import com.hastaprimasolusi.rana.ui.common.ProgDialog
import com.hastaprimasolusi.rana.ui.daftarmitra.DaftarViewModel
import com.hastaprimasolusi.rana.utils.ImageFilePath
import com.hastaprimasolusi.rana.utils.showAlert
import com.hastaprimasolusi.rana.utils.showalertConfirmation
import com.hastaprimasolusi.rana.utils.startAnimationGoneDown
import com.hastaprimasolusi.rana.utils.startAnimationShowUp
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import kotlinx.android.synthetic.main.dialog_cart_sukses.*
import kotlinx.android.synthetic.main.dialog_imagechooser.*
import kotlinx.android.synthetic.main.fragment_daftar_mitra_canvas.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream
import java.io.File

/**
 * Created by maasrahman on 25/02/22.
 */
class RegisterTokoFragment : Fragment(), OnMapReadyCallback {
    private val viewModel: DaftarViewModel by viewModel()
    private val progress = ProgDialog().getInstance()
    private var googleMaps: GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latLng = MutableLiveData<LatLng>()
    private var base64Image = ""
    private var isImageLoaded = MutableLiveData<Boolean>()
    private lateinit var locationHelper: LocationHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_daftar_mitra_canvas, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        locationHelper = LocationHelper(activity!!)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
        btnDaftar.setOnClickListener(listener)
        btnManual.setOnClickListener(listener)
        btnFab.setOnClickListener(listener)
        addImage.setOnClickListener(listener)
        imgRefresh.setOnClickListener(listener)
        cbSetuju.visibility = View.GONE
        initViewModel()
    }

    private val listener = View.OnClickListener { view ->
        when (view?.id) {
            R.id.btnManual -> {
                activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
                layoutMaps.visibility = View.VISIBLE
                startAnimationShowUp(layoutMaps)
            }

            R.id.btnFab -> {
                layoutMaps.visibility = View.GONE
                startAnimationGoneDown(layoutMaps)
            }

            R.id.btnDaftar -> {
                when {
                    etNama.text.isNullOrEmpty() -> etNama.error = "Required"
                    etNoTelp.text.isNullOrEmpty() -> etNoTelp.error = "Required"
                    etNamaPemilik.text.isNullOrEmpty() -> etNamaPemilik.error = "Required"
                    etProvinsi.text.isNullOrEmpty() -> etProvinsi.error = "Required"
                    etKotaKab.text.isNullOrEmpty() -> etKotaKab.error = "Required"
                    etKecamatan.text.isNullOrEmpty() -> etKecamatan.error = "Required"
                    etDesaKel.text.isNullOrEmpty() -> etDesaKel.error = "Required"
//                    etAlamat.text.isNullOrEmpty() -> etAlamat.error = "Required"
                    base64Image.isNullOrEmpty() -> showAlert(
                        activity!!,
                        "Foto Lokasi belum ditambahkan"
                    )

                    etLokasi.text.isNullOrEmpty() -> etLokasi.error = "Required"
//                    etKodePos.text.isNullOrEmpty() -> etKodePos.error = "Required"
                    etPassword.text.isNullOrEmpty() -> etPassword.error = "Required"
                    else -> {
                        actionRegister()
                    }
                }
            }

            R.id.addImage -> {
                showDialogImage()
            }

            R.id.imgRefresh -> {
                isImageLoaded.postValue(false)
            }
        }
    }

    private fun showDialogImage() {
        val dialog = Dialog(activity!!, R.style.DialogBounceAnim)
        dialog.setContentView(R.layout.dialog_imagechooser)
        dialog.btnCamera.setOnClickListener {
            var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, 88)
            dialog.dismiss()
        }
        dialog.btnGallery.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 8)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun actionRegister() {
        val request = RegisterRequest()
        request.nAMAOUTLET = etNama.text.toString()
        request.nOTELPON = etNoTelp.text.toString()
        request.eMAIL = etEmail.text.toString()
        request.dESAKELID = viewModel.kelSelected.value?.kELDESAID.toString()
        request.aLAMAT = etAlamat.text.toString()
        request.nAMAPEMILIK = etNamaPemilik.text.toString()
        request.zIPCODE = etKodePos.text.toString()
        request.oUTLETIMG = base64Image
        request.oUTLETLATITUDE = latLng.value?.latitude.toString()
        request.oUTLETLONGITUDE = latLng.value?.longitude.toString()
        request.nIK = if (etNIK.text.isNullOrEmpty()) "0000000000000000" else etNIK.text.toString()
        request.password = etPassword.text.toString()
        request.rePassword = etPassword.text.toString()
        viewModel.registerToko(request) {
            val dialog = Dialog(activity!!)
            dialog.setContentView(R.layout.dialog_cart_sukses)
            dialog.txtHeader.text = "Pendaftaran berhasil"
            dialog.btnOk.setOnClickListener {
                dialog.dismiss()
                val canvasModel: CanvasViewModel by inject()
                locationHelper.getLastKnownLocation { location ->
                    if (location != null) {
                        val latitude = location.latitude
                        val longitude = location.longitude
                        val address = locationHelper.getAddressFromLocation(latitude, longitude)
                        canvasModel.setLocationAddress(location, address)
                        canvasModel.getListToko()
//                    Toast.makeText(requireContext(), "Latitude: $latitude, Longitude: $longitude, Address: $address", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Location not found", Toast.LENGTH_SHORT)
                            .show()
                        canvasModel.getListToko()

                    }

                }
                requireActivity().supportFragmentManager.popBackStack()
            }
            dialog.show()
        }
    }

    private fun initViewModel() {
        viewModel.listProv.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            var listData = mutableListOf<Any>()
            it.forEach { prov ->
                listData.add(prov)
            }
            etProvinsi.setAdapter(listData) { selected ->
                val model = selected as ProvinsiModel
                viewModel.provSelected.postValue(model)
                etProvinsi.setText(model.pROVNAME)
            }
        })

        viewModel.listKota.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            var listData = mutableListOf<Any>()
            it.forEach { kota ->
                listData.add(kota)
            }
            etKotaKab.setAdapter(listData) { selected ->
                val model = selected as KabKotaModel
                viewModel.kotaSelected.postValue(model)
                etKotaKab.setText(model.kOTAKABNAME)
            }

            etKotaKab.text?.clear()
            viewModel.kotaSelected.postValue(null)
        })

        viewModel.listKec.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            var listData = mutableListOf<Any>()
            it.forEach { kec ->
                listData.add(kec)
            }
            etKecamatan.setAdapter(listData) { selected ->
                val model = selected as KecamatanModel
                viewModel.kecSelected.postValue(model)
                etKecamatan.setText(model.kECNAME)
            }

            etKecamatan.text?.clear()
            viewModel.kecSelected.postValue(null)
        })

        viewModel.listKel.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            var listData = mutableListOf<Any>()
            it.forEach { kel ->
                listData.add(kel)
            }
            etDesaKel.setAdapter(listData) { selected ->
                val model = selected as KelurahanModel
                viewModel.kelSelected.postValue(model)
                etDesaKel.setText(model.kELDESANAME)
            }

            etDesaKel.text?.clear()
            viewModel.kelSelected.postValue(null)
        })

        viewModel.provSelected.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            viewModel.getKabKota(it.pROVID.toString())
        })

        viewModel.kotaSelected.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            viewModel.getKec(it.kOTAKABID.toString())
        })

        viewModel.kecSelected.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            viewModel.getKel(it.kECID.toString())
        })

        viewModel.loadingProses.observe(viewLifecycleOwner, Observer {
            if (it) progress.show(activity!!)
            else progress.dismiss()
        })

        viewModel.showError.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            showAlert(activity!!, it)
            viewModel.showError.postValue(null)
        })

        isImageLoaded.observe(viewLifecycleOwner, Observer {
            if (it) {
                addImage.visibility = View.GONE
                layoutLokasi.visibility = View.VISIBLE
            } else {
                base64Image = ""
                addImage.visibility = View.VISIBLE
                layoutLokasi.visibility = View.GONE
            }
        })

        latLng.observe(viewLifecycleOwner, Observer { lok ->
            if (lok == null) return@Observer
            etLokasi.setText("${lok?.latitude},${lok?.longitude}")
        })

        viewModel.getProvinsi()

        activity?.runOnUiThread {
            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }
    }

    override fun onMapReady(gMaps: GoogleMap?) {
        googleMaps = gMaps
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    activity!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                googleMaps?.isMyLocationEnabled = true
            }
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            val mapLocation = LatLng(location?.latitude?.let { it }
                ?: run { -6.175110 }, location?.longitude?.let { it } ?: run { 106.865036 })
            val cameraPosition = CameraPosition.Builder().target(mapLocation).zoom(15f).build()
            googleMaps?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

            googleMaps?.addMarker(
                MarkerOptions()
                    .position(mapLocation)
                    .draggable(true)
            )
            latLng.postValue(mapLocation)
            googleMaps?.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
                @SuppressLint("SetTextI18n")
                override fun onMarkerDragEnd(markers: Marker?) {
                    markers?.position?.let {
                        latLng.postValue(it)
                    }
                }

                override fun onMarkerDragStart(markers: Marker?) {

                }

                override fun onMarkerDrag(markers: Marker?) {

                }

            })

            googleMaps?.setOnMapClickListener { data ->
                googleMaps?.clear()
                googleMaps?.addMarker(
                    MarkerOptions()
                        .position(data)
                        .draggable(true)
                )
                latLng.postValue(data)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 8 && resultCode == Activity.RESULT_OK) {
            val path = ImageFilePath.getPath(activity!!, data?.data)
            GlobalScope.launch {
                val compressedImageFile = Compressor.compress(activity!!, File(path)) {
                    default(width = 640, format = Bitmap.CompressFormat.JPEG, quality = 75)
                }
                val bytes = compressedImageFile.readBytes()
                base64Image = Base64.encodeToString(bytes, Base64.DEFAULT)
                isImageLoaded.postValue(true)
                withContext(Dispatchers.Main) {
                    Glide.with(activity!!)
                        .load(compressedImageFile)
                        .apply(RequestOptions().error(R.drawable.no_image))
                        .into(imgLokasi)
                }
            }
        } else if (requestCode == 88 && resultCode == Activity.RESULT_OK) {
            var baos = ByteArrayOutputStream()
            val bm = data?.extras?.get("data") as Bitmap
            Glide.with(this)
                .load(bm)
                .apply(RequestOptions().error(R.drawable.no_image))
                .into(imgLokasi)
            try {
                bm.compress(Bitmap.CompressFormat.JPEG, 75, baos)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    val bytes = baos.toByteArray()
                    base64Image = Base64.encodeToString(bytes, Base64.DEFAULT)
                    isImageLoaded.postValue(true)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}