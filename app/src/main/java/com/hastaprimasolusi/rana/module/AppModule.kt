package com.hastaprimasolusi.rana.module

import android.content.Context
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.hastaprimasolusi.rana.BuildConfig
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.data.ApiService
import com.hastaprimasolusi.rana.data.local.DatabaseHelper
import com.hastaprimasolusi.rana.data.local.DbRepository
import com.hastaprimasolusi.rana.data.local.DbRepositoryImp
import com.hastaprimasolusi.rana.data.network.ApiRepository
import com.hastaprimasolusi.rana.data.network.ApiRepositoryImpl
import com.hastaprimasolusi.rana.helper.Helpers
import com.hastaprimasolusi.rana.internal.connectivity.ConnectivityInterceptor
import com.hastaprimasolusi.rana.internal.connectivity.ConnectivityInterceptorImpl
import com.hastaprimasolusi.rana.ui.CommonViewModel
import com.hastaprimasolusi.rana.ui.akun.ProfileViewModel
import com.hastaprimasolusi.rana.ui.canvasser.CanvasViewModel
import com.hastaprimasolusi.rana.ui.daftarmitra.DaftarViewModel
import com.hastaprimasolusi.rana.ui.login.LoginViewModel
import com.hastaprimasolusi.rana.ui.lp.LpViewModel
import com.hastaprimasolusi.rana.ui.mitra.MitraViewModel
import com.hastaprimasolusi.rana.ui.report.ReportViewModel
import com.hastaprimasolusi.rana.utils.UtilsPref
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created By maasrahman on 5/14/20
 */
val appModule = module {

    factory<ConnectivityInterceptor> {
        ConnectivityInterceptorImpl(
            androidContext()
        )
    }

    single {
        createWebService<ApiService>(
            okHttpClient = createHttpClient(androidContext(), get()),
            factory = RxJava2CallAdapterFactory.create(),
            baseUrl = getUrl(androidContext())
        )
    }
    single {
        Room.databaseBuilder(androidApplication(), DatabaseHelper::class.java, "dmlt.db")
            .build()
    }
    single { get<DatabaseHelper>().messageDao() }
    factory<DbRepository> { DbRepositoryImp(messageDao = get()) }
    factory<ApiRepository> { ApiRepositoryImpl(apiService = get()) }

    viewModel { LoginViewModel(apiRepository = get()) }
    viewModel { DaftarViewModel(apiRepository = get()) }
    viewModel { ProfileViewModel(apiRepository = get()) }
    single { CommonViewModel(apiRepository = get()) }
    single { ReportViewModel(apiRepository = get()) }
    single { MitraViewModel(apiRepository = get(), dbRepository = get()) }
    single { LpViewModel(apiRepository = get(), dbRepository = get()) }
    single { CanvasViewModel(apiRepository = get(), dbRepository = get())  }

}

fun getUrl(context: Context): String{
    return if (!Helpers.isEmptyOrNull( UtilsPref.loadString(context.getString(R.string.url)))){
        UtilsPref.loadString(context.getString(R.string.url))
    }else{
        BuildConfig.BASE_URL
    }
}

fun createHttpClient(context: Context, connectivityInterceptor: ConnectivityInterceptor): OkHttpClient {

    val interceptor = Interceptor { chain ->
        if(UtilsPref.loadBoolean(context.getString(R.string.isLoggedIn))){
            val request = chain.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer " + UtilsPref.loadString(context.getString(R.string.userToken)))
                .addHeader("Content-Type", "application/json")
                .build()
            return@Interceptor chain.proceed(request)
        }else{
            val request = chain.request()
                .newBuilder()
                .addHeader("Content-Type", "application/json")
                .build()
            return@Interceptor chain.proceed(request)
        }
    }

    return OkHttpClient.Builder()
        .addInterceptor(interceptor)
//        .addInterceptor(ChuckInterceptor(context))
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .addInterceptor(connectivityInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
}

inline fun <reified T> createWebService(
    okHttpClient: OkHttpClient,
    factory: CallAdapter.Factory, baseUrl: String
): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addCallAdapterFactory(factory)
        .client(okHttpClient)
        .build()
    return retrofit.create(T::class.java)
}