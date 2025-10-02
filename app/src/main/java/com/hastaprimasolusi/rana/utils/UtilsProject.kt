package com.hastaprimasolusi.rana.utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Base64
import android.util.Base64OutputStream
import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.animation.PathInterpolatorCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hastaprimasolusi.rana.R
import kotlinx.coroutines.CoroutineExceptionHandler
import org.jetbrains.anko.alert
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.textColor
import java.io.*
import java.nio.charset.Charset
import java.security.MessageDigest
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.experimental.and

val handler = CoroutineExceptionHandler { _, throwable ->

}

fun ImageView.loadImage(imageUrl: String) {
    Glide.with(this)
        .load(imageUrl)
        .apply(RequestOptions().error(R.drawable.no_image))
        .into(this)
}

fun String.capitalizeWords(): String = split(" ").map { it.capitalize() }.joinToString(" ")

fun TextView.changeBackground(status: String){
    when (status) {
        "1", "2", "10" -> {
            this.background = resources.getDrawable(R.drawable.bg_rounded_lime)
        }
        "3" -> {
            this.background = resources.getDrawable(R.drawable.bg_rounded_amber)
        }
        "6", "7", "8", "9" -> {
            this.background = resources.getDrawable(R.drawable.bg_rounded_cyan)
        }
        "4" -> {
            this.background = resources.getDrawable(R.drawable.bg_rounded_green)
        }
        else -> {
            this.background = resources.getDrawable(R.drawable.bg_rounded_gray)
        }
    }
}

fun millisToDate(millis: Long, strFormat: String): String{
    val sdf = SimpleDateFormat(strFormat)
    return sdf.format(Date(millis))
}

fun tintMyDrawable(drawable: Drawable, color: Int): Drawable {
    var drawable = drawable
    drawable = DrawableCompat.wrap(drawable)
    DrawableCompat.setTint(drawable, color)
    DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)
    return drawable
}

fun showAlert(context:Context, errorString: String){
    context.alert(errorString) {
        positiveButton(context.getString(R.string.ok)) {

        }
    }.show().apply {
        getButton(AlertDialog.BUTTON_POSITIVE)?.let { it.textColor = ContextCompat.getColor(context, R.color.colorPrimaryDark) }
    }
}

fun showalertConfirmation(context: Context, errorString: String, listener:() -> Unit){
    context.alert(errorString) {
        positiveButton("Ya") {
            listener()
        }
        negativeButton("Tidak"){

        }
    }.show().apply {
        getButton(AlertDialog.BUTTON_POSITIVE)?.let {
            it.backgroundColor = Color.TRANSPARENT
            it.textColor = ContextCompat.getColor(context, R.color.colorPrimaryDark)
        }
        getButton(AlertDialog.BUTTON_NEGATIVE)?.let {
            it.backgroundColor = Color.TRANSPARENT
            it.textColor = ContextCompat.getColor(context, R.color.colorPrimaryDark)
        }
    }
}

fun showalertInformation(context: Context, errorString: String, listener:() -> Unit){
    context.alert(errorString) {
        positiveButton(context.getString(R.string.ok)) {
            listener()
            it.dismiss()
        }
    }.show().apply {
        getButton(AlertDialog.BUTTON_POSITIVE)?.let {
            it.backgroundColor = Color.TRANSPARENT
            it.textColor = ContextCompat.getColor(context, R.color.colorPrimaryDark)
        }
    }
}

fun showalertTwoButtons(
    context: Context,
    message: String,
    positiveText: String = "Ya",
    neutralText: String = "Tidak",
    positiveListener: () -> Unit = {},
    neutralListener: () -> Unit = {}
) {
    val alertDialog = AlertDialog.Builder(context)
        .setMessage(message)
        .setPositiveButton(positiveText) { dialog, _ ->
            positiveListener()
            dialog.dismiss()
        }
        .setNegativeButton(neutralText) { dialog, _ ->
            neutralListener()
            dialog.dismiss()
        }
        .create()

    alertDialog.show()

    // Styling tombol
    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)?.let {
        it.setBackgroundColor(Color.TRANSPARENT)
        it.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
    }

    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.let {
        it.setBackgroundColor(Color.TRANSPARENT)
        it.setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
    }
}

fun showalertThreeButtons(
    context: Context,
    message: String,
    positiveText: String = "Ya",
    neutralText: String = "Mungkin",
    negativeText: String = "Tidak",
    positiveListener: () -> Unit = {},
    neutralListener: () -> Unit = {},
    negativeListener: () -> Unit = {}
) {
    val alertDialog = AlertDialog.Builder(context)
        .setMessage(message)
        .setPositiveButton(positiveText) { dialog, _ ->
            positiveListener()
            dialog.dismiss()
        }
        .setNeutralButton(neutralText) { dialog, _ ->
            neutralListener()
            dialog.dismiss()
        }
        .setNegativeButton(negativeText) { dialog, _ ->
            negativeListener()
            dialog.dismiss()
        }
        .create()

    alertDialog.show()

    // Styling tombol
    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)?.let {
        it.setBackgroundColor(Color.TRANSPARENT)
        it.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
    }

    alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL)?.let {
        it.setBackgroundColor(Color.TRANSPARENT)
        it.setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
    }

    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.let {
        it.setBackgroundColor(Color.TRANSPARENT)
        it.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
    }
}

fun alertFinish(context: Context, errorString: String){
    context.alert(errorString) {
        positiveButton(context.getString(R.string.ok)) {
            (context as AppCompatActivity).finish()
        }
    }.show().apply {
        getButton(AlertDialog.BUTTON_POSITIVE)?.let { it.textColor = Color.WHITE }
    }
}

fun formatKtp(
    jumlah: String, npemecah: Int,
    insert: Char
): String {
    val data = jumlah.reversed().toCharArray()
    var x = ""
    for (i in data.indices) {
        if ((data.size - i) % npemecah == 0) {
            x = insert + x
        }
        x = data[i] + x
    }
    return "$x"
}

fun convertCurrency(
    jumlah: String, npemecah: Int,
    insert: Char, simbol: String
): String {
    var jumlah = jumlah
    if (jumlah == "" || jumlah == "null") {
        return "$simbol 0"
    }
    val data = jumlah.toCharArray()
    jumlah = ""
    for (i in data.indices) {
        if ((data.size - i) % npemecah == 0) {
            jumlah += insert
        }
        jumlah += data[i]
    }
    if (data.size % npemecah == 0) {
        jumlah = jumlah.substring(1, jumlah.length)
    }
    return "$simbol $jumlah"
}

fun convertCurrencyNo(
    jumlah: String, npemecah: Int,
    insert: Char
): String {
    var jumlah = jumlah
    if (jumlah == "" || jumlah == "null") {
        return "0"
    }
    val data = jumlah.toCharArray()
    jumlah = ""
    for (i in data.indices) {
        if ((data.size - i) % npemecah == 0) {
            jumlah += insert
        }
        jumlah += data[i]
    }
    if (data.size % npemecah == 0) {
        jumlah = jumlah.substring(1, jumlah.length)
    }
    return "$jumlah"
}

fun converNumber(
    jumlah: String, npemecah: Int,
    insert: Char
): String {
    var jumlah = jumlah
    if (jumlah == "" || jumlah == "null") {
        return "0"
    }
    val data = jumlah.toCharArray()
    jumlah = ""
    for (i in data.indices) {
        if ((data.size - i) % npemecah == 0) {
            jumlah += insert
        }
        jumlah += data[i]
    }
    if (data.size % npemecah == 0) {
        jumlah = jumlah.substring(1, jumlah.length)
    }
    return "$jumlah"
}

fun convertDateTimeZone(str: String, frmt: String) : String{
    var sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    try{
        val dt = sdf.parse(str)
        sdf = SimpleDateFormat(frmt)
        return sdf.format(dt)
    }catch (e: Exception){

    }
    return str
}

fun convertDateTime(str: String, frmt: String) : String{
    var sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    try{
        val dt = sdf.parse(str)
        sdf = SimpleDateFormat(frmt)
        return sdf.format(dt)
    }catch (e: Exception){

    }
    return str
}

fun getSha256(value: String): String? {
    return try {
        val md: MessageDigest = MessageDigest.getInstance("SHA-256")
        val byteData = md.digest(value.toByteArray(Charset.forName("UTF-8")))
        bytesToHex(byteData)
    } catch (ex: java.lang.Exception) {
        throw RuntimeException(ex)
    }
}

private fun bytesToHex(bytes: ByteArray): String? {
    val result = StringBuilder()
    for (b in bytes) {
        result.append(String.format("%02x", b and 0xFF.toByte()))
    }
    return result.toString()
}

fun getDeviceName(): String? {
    val os = Build.VERSION.RELEASE
    val manufacturer = Build.MANUFACTURER.replace("-", " ")
    val model = Build.MODEL.replace("-", " ")
    return if (model.startsWith(manufacturer)) {
        os + "-" + capitalize(model)
    } else os + "-" + capitalize(manufacturer) + "_" + model
}

private fun capitalize(str: String): String {
    if (TextUtils.isEmpty(str)) {
        return str
    }
    val arr = str.toCharArray()
    var capitalizeNext = true
    val phrase = StringBuilder()
    for (c in arr) {
        if (capitalizeNext && Character.isLetter(c)) {
            phrase.append(Character.toUpperCase(c))
            capitalizeNext = false
            continue
        } else if (Character.isWhitespace(c)) {
            capitalizeNext = true
        }
        phrase.append(c)
    }
    return phrase.toString()
}

fun slideUp(view: View) {
    view.visibility = View.VISIBLE
    val animate = TranslateAnimation(
        0.toFloat(),  // fromXDelta
        0.toFloat(),  // toXDelta
        view.height.toFloat(),  // fromYDelta
        0.toFloat()
    ) // toYDelta
    animate.duration = 1000
    animate.fillAfter = true
    view.startAnimation(animate)
}

fun slideDown(view: View) {
    view.visibility = View.GONE
    val animate = TranslateAnimation(
        0.toFloat(),  // fromXDelta
        0.toFloat(),  // toXDelta
        0.toFloat(),  // fromYDelta
        view.height.toFloat()
    ) // toYDelta
    animate.duration = 1000
    animate.fillAfter = true
    view.startAnimation(animate)
}

fun startAnimationShowUp(view: View) {

    val animator1 = ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofFloat(
        View.TRANSLATION_Y, -40f, 0f))
    animator1.duration = 1000
    animator1.interpolator = PathInterpolatorCompat.create(0f, 0f, 1f, 1f)

    val animator2 = ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofFloat(
        View.ALPHA, 0f, 1f))
    animator2.duration = 1000
    animator2.interpolator = PathInterpolatorCompat.create(0.42f, 0f, 0.58f, 1f)

    val animatorSet1 = AnimatorSet()
    animatorSet1.playTogether(animator1, animator2)
    animatorSet1.setTarget(view)

    val animatorSet2 = AnimatorSet()
    animatorSet2.playTogether(animatorSet1)
    animatorSet2.start()
}

fun startAnimationGoneDown(view: View) {

    val animator1 = ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0f, -40f))
    animator1.duration = 1000
    animator1.interpolator = PathInterpolatorCompat.create(0f, 0f, 1f, 1f)

    val animator2 = ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofFloat(View.ALPHA, 1f, 0f))
    animator2.duration = 1000
    animator2.interpolator = PathInterpolatorCompat.create(0.42f, 0f, 0.58f, 1f)

    val animatorSet1 = AnimatorSet()
    animatorSet1.playTogether(animator1, animator2)
    animatorSet1.setTarget(view)

    val animatorSet2 = AnimatorSet()
    animatorSet2.playTogether(animatorSet1)
    animatorSet2.start()
}

fun getStringFile(f: File): String? {
    var inputStream: InputStream? = null
    var encodedFile = ""
    val lastVal: String
    try {
        inputStream = FileInputStream(f.absolutePath)
        val buffer = ByteArray(10240) //specify the size to allow
        var bytesRead: Int
        val output = ByteArrayOutputStream()
        val output64 = Base64OutputStream(output, Base64.NO_WRAP)
        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
            output64.write(buffer, 0, bytesRead)
        }
        output64.close()
        encodedFile = output.toString()
    } catch (e1: FileNotFoundException) {
        e1.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    lastVal = encodedFile
    return lastVal
}

fun getStringImage(bmp: Bitmap): String? {
    val baos = ByteArrayOutputStream()
    bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val imageBytes = baos.toByteArray()
    return Base64.encodeToString(imageBytes, Base64.DEFAULT)
}

fun addImageToGallery(
    context: Context,
    filepath: String?,
    title: String?,
    description: String?
) {
    val values = ContentValues()
    values.put(MediaStore.Images.Media.TITLE, title)
    values.put(MediaStore.Images.Media.DESCRIPTION, description)
    values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
    values.put(MediaStore.MediaColumns.DATA, filepath)
    context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
}

fun getDate() : String {
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    return format.format(Date())
}