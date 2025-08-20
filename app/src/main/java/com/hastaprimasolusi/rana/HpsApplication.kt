package com.hastaprimasolusi.rana

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller
import com.google.android.libraries.places.api.Places
import com.hastaprimasolusi.rana.module.appModule
import com.orhanobut.hawk.Hawk
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import javax.net.ssl.SSLContext


/**
 * Created By maasrahman on 2020-04-25
 */
class HpsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Hawk.init(this).build()
        startKoin {
            androidContext(this@HpsApplication)
            modules(appModule)
        }
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.google_maps_key))
            Places.createClient(applicationContext)
        }
        try {
            ProviderInstaller.installIfNeeded(this)
            val sslContext: SSLContext = SSLContext.getInstance("TLSv1.2")
            sslContext.init(null, null, null)
            sslContext.createSSLEngine()
        } catch (e: GooglePlayServicesRepairableException) {
            // Prompt the user to install/update/enable Google Play services.
            GoogleApiAvailability.getInstance()
                .showErrorNotification(this, e.connectionStatusCode)
        } catch (e: GooglePlayServicesNotAvailableException) {
            // Indicates a non-recoverable error: let the user know.
        }
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

//    override fun onProviderInstallFailed(errorCode: Int, recoveryIntent: Intent?) {
//        GoogleApiAvailability.getInstance().apply {
//            if (isUserResolvableError(errorCode)) {
//                onProviderInstallerNotAvailable()
//            } else {
//                onProviderInstallerNotAvailable()
//            }
//        }
//    }
//
//    override fun onProviderInstalled() {
//
//    }

    private fun onProviderInstallerNotAvailable() {

    }
}