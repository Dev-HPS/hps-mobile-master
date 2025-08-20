package com.hastaprimasolusi.rana.ui.canvasser.order

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.adapter.page.KeranjangAdapter
import com.hastaprimasolusi.rana.data.network.response.order.CartProdukModel
import com.hastaprimasolusi.rana.ui.canvasser.CanvasViewModel
import com.hastaprimasolusi.rana.ui.common.ProgDialog
import com.hastaprimasolusi.rana.utils.ImageFilePath
import com.hastaprimasolusi.rana.utils.UtilsPref
import com.hastaprimasolusi.rana.utils.convertCurrency
import com.hastaprimasolusi.rana.utils.convertCurrencyNo
import com.hastaprimasolusi.rana.utils.showalertInformation
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import kotlinx.android.synthetic.main.dialog_bukti_bayar.*
import kotlinx.android.synthetic.main.dialog_imagechooser.*
import kotlinx.android.synthetic.main.dialog_konfirmasi.*
import kotlinx.android.synthetic.main.dialog_konfirmasi.txtHeader
import kotlinx.android.synthetic.main.fragment_canvas_cart.*
import kotlinx.android.synthetic.main.fragment_daftar_mitra_canvas.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import java.io.ByteArrayOutputStream
import java.io.File

/**
 * Created By maasrahman on 6/7/20
 */
class DetailOrderFragment : Fragment() {
    private val viewModel: CanvasViewModel by inject()
    private lateinit var adapter: KeranjangAdapter
    private val progress = ProgDialog().getInstance()
    private var base64Image = ""
    private var isImageLoaded = MutableLiveData<Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_canvas_cart, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter =
            KeranjangAdapter({ model, jml ->
                viewModel.updateCart(model, jml)
            }, { model ->
                val dialog = Dialog(activity!!)
                dialog.setContentView(R.layout.dialog_konfirmasi)
                dialog.txtHeader.text = getString(R.string.yakinmenghapusproduk)
                dialog.btnYa.setOnClickListener {
                    viewModel.deleteCart(model)
                    dialog.dismiss()
                }
                dialog.btnTidak.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            })

        recyclerKeranjang.layoutManager = LinearLayoutManager(activity)
        recyclerKeranjang.itemAnimator = DefaultItemAnimator()
        recyclerKeranjang.adapter = adapter

        initViewModel()
        btnBayar.setOnClickListener {
//            view ->
//            viewModel.actionCheckOut {
//                layoutSummary.visibility = View.GONE
//                showalertInformation(activity!!, "Pesanan berhasil"){
//                    activity?.supportFragmentManager?.popBackStack()
//                }
//            }
            val dialog = Dialog(activity!!)
            dialog.setContentView(R.layout.dialog_bukti_bayar)
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            dialog.btnImage.setOnClickListener {
                dialog.dismiss()
                showDialogImage()
            }
            dialog.btnSkip.setOnClickListener {
                dialog.dismiss()
                viewModel.actionCheckOut {
                    layoutSummary.visibility = View.GONE
                    showalertInformation(activity!!, "Pesanan berhasil") {
                        activity?.supportFragmentManager?.popBackStack()
                    }
                }
            }
            dialog.show()
            val window = dialog.window
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 8 && resultCode == Activity.RESULT_OK) {
//            val path = ImageFilePath.getPath(activity!!, data?.data)
//            GlobalScope.launch {
//                val compressedImageFile = Compressor.compress(activity!!, File(path)) {
//                    default(width = 640, format = Bitmap.CompressFormat.JPEG, quality = 75)
//                }
//                val bytes = compressedImageFile.readBytes()
//                base64Image = Base64.encodeToString(bytes, Base64.DEFAULT)
//                viewModel.buktiBayarImg.postValue(base64Image)
//                isImageLoaded.postValue(true)
//            }
//        } else if (requestCode == 88 && resultCode == Activity.RESULT_OK) {
//            var baos = ByteArrayOutputStream()
//            val bm = data?.extras?.get("data") as Bitmap
//            try {
//                bm.compress(Bitmap.CompressFormat.JPEG, 75, baos)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            } finally {
//                try {
//                    val bytes = baos.toByteArray()
//                    base64Image = Base64.encodeToString(bytes, Base64.DEFAULT)
//                    viewModel.buktiBayarImg.postValue(base64Image)
//                    isImageLoaded.postValue(true)
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
//        }
//        viewModel.actionCheckOut {
//            layoutSummary.visibility = View.GONE
//            showalertInformation(activity!!, "Pesanan berhasil") {
//                activity?.supportFragmentManager?.popBackStack()
//            }
//        }
//    }
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if (requestCode == 8 && resultCode == Activity.RESULT_OK && data?.data != null) {
        val path = ImageFilePath.getPath(activity!!, data.data)
        if (path != null) {
            GlobalScope.launch {
                val compressedImageFile = Compressor.compress(activity!!, File(path)) {
                    default(width = 640, format = Bitmap.CompressFormat.JPEG, quality = 75)
                }
                val bytes = compressedImageFile.readBytes()
                base64Image = Base64.encodeToString(bytes, Base64.DEFAULT)
                viewModel.buktiBayarImg.postValue(base64Image)
                isImageLoaded.postValue(true)

                // Hanya panggil actionCheckOut setelah gambar berhasil diproses
                withContext(Dispatchers.Main) {
                    viewModel.actionCheckOut {
                        layoutSummary.visibility = View.GONE
                        showalertInformation(activity!!, "Pesanan berhasil") {
                            activity?.supportFragmentManager?.popBackStack()
                        }
                    }
                }
            }
        }
    } else if (requestCode == 88 && resultCode == Activity.RESULT_OK) {
        val bm = data?.extras?.get("data") as? Bitmap
        if (bm != null) {
            val baos = ByteArrayOutputStream()
            try {
                bm.compress(Bitmap.CompressFormat.JPEG, 75, baos)
                val bytes = baos.toByteArray()
                base64Image = Base64.encodeToString(bytes, Base64.DEFAULT)
                viewModel.buktiBayarImg.postValue(base64Image)
                isImageLoaded.postValue(true)

                viewModel.actionCheckOut {
                    layoutSummary.visibility = View.GONE
                    showalertInformation(activity!!, "Pesanan berhasil") {
                        activity?.supportFragmentManager?.popBackStack()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                baos.close()
            }
        }
    }
}


    private fun updateSummary(data: List<CartProdukModel>) {
        UtilsPref.saveString(
            getString(R.string.currencySymbol),
            viewModel.cartData?.cARTCURRENCY.toString()
        )
        layoutSummary.visibility = View.VISIBLE
        var jml = 0
        var jmlItem = 0
        data.forEach {
            val totalAmount = it.tOTALAMT.toString().toIntOrNull()
            val totalQty = it.tOTALQTY.toString().toIntOrNull()
            val prodPrice = it.pRODPRICE?.first()?.pRODPRICE.toString().toIntOrNull() ?: 0
            val prodQty = it.pRODPRICE?.first()?.pRODQTY.toString().toIntOrNull() ?: 0

            jml = totalAmount ?: jml + (prodQty * prodPrice)
            jmlItem = totalQty ?: jmlItem + prodQty
        }
        txtJmlItem.text = convertCurrencyNo(jmlItem.toString(), 3, '.')
        txtTotal.text = convertCurrency(
            jml.toString(),
            3,
            '.',
            UtilsPref.loadString(getString(R.string.currencySymbol))
        )
    }

    private fun showDialogImage(){
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

    private fun initViewModel() {
        viewModel.loadingCart.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            swipe.isRefreshing = it
        })

        viewModel.listCart.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            updateSummary(it)
            adapter.updateData(it)
        })

        viewModel.loadingCheckOut.observe(viewLifecycleOwner, Observer {
            if (it) progress.show(activity!!) else progress.dismiss()
        })

        if (adapter.itemCount == 0) {
            viewModel.getCart()
        }

        swipe.setOnRefreshListener {
            viewModel.getCart()
        }
    }
}