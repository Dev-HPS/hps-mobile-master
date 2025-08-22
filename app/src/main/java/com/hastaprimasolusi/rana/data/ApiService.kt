package com.hastaprimasolusi.rana.data

import com.hastaprimasolusi.rana.data.network.requesthelper.ApproveRequest
import com.hastaprimasolusi.rana.data.network.requesthelper.AttendanceRequest
import com.hastaprimasolusi.rana.data.network.requesthelper.CartRequest
import com.hastaprimasolusi.rana.data.network.requesthelper.CheckoutRequest
import com.hastaprimasolusi.rana.data.network.requesthelper.ConfirmRequest
import com.hastaprimasolusi.rana.data.network.requesthelper.DeliverRequest
import com.hastaprimasolusi.rana.data.network.requesthelper.KembaliBarangRequest
import com.hastaprimasolusi.rana.data.network.requesthelper.LoginRequest
import com.hastaprimasolusi.rana.data.network.requesthelper.PasswordRequest
import com.hastaprimasolusi.rana.data.network.requesthelper.PayRequest
import com.hastaprimasolusi.rana.data.network.requesthelper.PembatalanRequest
import com.hastaprimasolusi.rana.data.network.requesthelper.PhotoRequest
import com.hastaprimasolusi.rana.data.network.requesthelper.QrRequest
import com.hastaprimasolusi.rana.data.network.requesthelper.QrRequestSpg
import com.hastaprimasolusi.rana.data.network.requesthelper.RegisterRequest
import com.hastaprimasolusi.rana.data.network.response.DeliveryCourierResponse
import com.hastaprimasolusi.rana.data.network.response.LoginResponse
import com.hastaprimasolusi.rana.data.network.response.LpResponse
import com.hastaprimasolusi.rana.data.network.response.PayMethodResponse
import com.hastaprimasolusi.rana.data.network.response.PaymentDetailResponse
import com.hastaprimasolusi.rana.data.network.response.ProfileResponse
import com.hastaprimasolusi.rana.data.network.response.UpdatePhotoResponse
import com.hastaprimasolusi.rana.data.network.response.attendance.AttendanceCheckResponse
import com.hastaprimasolusi.rana.data.network.response.canvas.ListTokoResponse
import com.hastaprimasolusi.rana.data.network.response.canvas.RiwayatCanvasResponse
import com.hastaprimasolusi.rana.data.network.response.canvas.RiwayatDetCnvsResponse
import com.hastaprimasolusi.rana.data.network.response.master.KabKotaResponse
import com.hastaprimasolusi.rana.data.network.response.master.KecamatanResponse
import com.hastaprimasolusi.rana.data.network.response.master.KelurahanResponse
import com.hastaprimasolusi.rana.data.network.response.master.ProvinsiResponse
import com.hastaprimasolusi.rana.data.network.response.order.CartListResponse
import com.hastaprimasolusi.rana.data.network.response.order.CartResponse
import com.hastaprimasolusi.rana.data.network.response.order.CheckOutResponse
import com.hastaprimasolusi.rana.data.network.response.order.HistoryDetailResponse
import com.hastaprimasolusi.rana.data.network.response.order.HistoryOrderResponse
import com.hastaprimasolusi.rana.data.network.response.order.OrderDetailResponse
import com.hastaprimasolusi.rana.data.network.response.order.OrderStatusResponse
import com.hastaprimasolusi.rana.data.network.response.order.PaymentResponse
import com.hastaprimasolusi.rana.data.network.response.order.PembatalanResponse
import com.hastaprimasolusi.rana.data.network.response.order.QrResponse
import com.hastaprimasolusi.rana.data.network.response.order.ReportResponse
import com.hastaprimasolusi.rana.data.network.response.payment.PaymentStatusResponse
import com.hastaprimasolusi.rana.data.network.response.produk.CategoryResponse
import com.hastaprimasolusi.rana.data.network.response.produk.ProdukDetailResponse
import com.hastaprimasolusi.rana.data.network.response.produk.ProdukListResponse
import com.hastaprimasolusi.rana.data.network.response.report.MutasiResponse
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created By maasrahman on 2020-04-15
 */
interface ApiService {
    @POST("auth")
    fun login(@Body loginRequest: LoginRequest): Deferred<LoginResponse>

    @GET("product")
    fun getProdukList(@Query("offset") offset: String?, @Query("limit") limit: String?,
                      @Query("search") search: String?, @Query("category") category: String?,
                      @Query("type") type: String?, @Query("lp_code") lpCode: String?)
            : Deferred<ProdukListResponse>

    @GET("product")
    fun getProductListCall(@Query("offset") offset: String, @Query("limit") limit: String,
                           @Query("search") search: String, @Query("category") category: String,
                           @Query("type") type: String)
            : Call<ProdukListResponse>

    @GET("product/{id}")
    fun getProdukDetail(@Path("id") id: String): Deferred<ProdukDetailResponse>

    @GET("categories")
    fun getCategoryList(@Query("limit") limit: String, @Query("type") type: String): Deferred<CategoryResponse>

    @GET("cart")
    fun getCartList(): Deferred<CartListResponse>

    @POST("cart")
    fun actionCart(@Body cartRequest: CartRequest): Deferred<CartResponse>

    @PUT("cart")
    fun actionCartUpdate(@Body cartRequest: CartRequest): Deferred<CartResponse>

    @DELETE("cart/{id}")
    fun actionCartDelete(@Path("id") id: String): Deferred<CartResponse>

    @DELETE("cart/{id}")
    fun actionCartDeleteCall(@Path("id") id: String): Call<CartResponse>

    @POST("checkout")
    fun actionCheckOut(): Deferred<CheckOutResponse>

    @POST("checkout")
    fun actionCheckOutLp(@Body checkoutRequest: CheckoutRequest): Deferred<CheckOutResponse>

    @GET("history")
    fun getHistoryPesan(@Query("limit") limit: String, @Query("offset") offset: String,
                        @Query("status") status: String, @Query("DT_START") dtStart: String,
                        @Query("DT_END") dtEnd: String) : Deferred<HistoryOrderResponse>

    @GET("history/{id}")
    fun getHistoryPesanDetail(@Path("id") id: String) : Deferred<HistoryDetailResponse>

    @GET("order")
    fun getPesanan(@Query("limit") limit: String, @Query("offset") offset: String,
                   @Query("status") status: String, @Query("DT_START") dtStart: String,
                   @Query("DT_END") dtEnd: String) : Deferred<HistoryOrderResponse>

    @GET("order/{id}")
    fun getPesananDetail(@Path("id") id: String) : Deferred<OrderDetailResponse>

    @POST("order/approve")
    fun approvePesanan(@Body approveRequest: ApproveRequest): Deferred<OrderDetailResponse>

    @POST("order-confirm")
    fun orderConfirm(@Body confirmRequest: ConfirmRequest): Deferred<OrderDetailResponse>

    @GET("pay-method")
    fun getPayMethod() : Deferred<PayMethodResponse>

    @POST("payment")
    fun setPayMethod(@Body payRequest: PayRequest) : Deferred<PaymentResponse>

    @GET("order/status")
    fun getOrderStatus(@Query("type") type: String): Deferred<OrderStatusResponse>

    @GET("delivery/courier")
    fun getDeliveryCourier(): Deferred<DeliveryCourierResponse>

    @POST("delivery")
    fun setDeliveryCourier(@Body deliverRequest: DeliverRequest) : Deferred<DeliveryCourierResponse>

    @GET("canvasser")
    fun getRiwayatCanvasser(@Query("limit") limit: String, @Query("offset") offset: String,
                            @Query("status") status: String, @Query("DT_START") dtStart: String,
                            @Query("DT_END") dtEnd: String) : Deferred<RiwayatCanvasResponse>

    @GET("canvasser/{id}")
    fun getRiwayatCanvasserDetail(@Path("id") id: String) : Deferred<RiwayatDetCnvsResponse>

    //new feature SPG
    @GET("spg")
    fun getRiwayatSpg(@Query("limit") limit: String, @Query("offset") offset: String,
                            @Query("status") status: String, @Query("DT_START") dtStart: String,
                            @Query("DT_END") dtEnd: String) : Deferred<RiwayatCanvasResponse>

    @GET("spg/{id}")
    fun getRiwayatSpgDetail(@Path("id") id: String) : Deferred<RiwayatDetCnvsResponse>

    @POST("bag")
    fun actionPosAdd(@Body cartRequest: CartRequest): Deferred<CartResponse>

    @GET("bag")
    fun actionPosGet(): Deferred<CartListResponse>

//    @GET("bag")
//    fun actionPosGet(): Deferred<CanvasBagResponse>

    @PUT("bag")
    fun actionPosUpdate(@Body cartRequest: CartRequest): Deferred<CartResponse>

    @DELETE("bag/{id}")
    fun actionPosDelete(@Path("id") id: String): Deferred<CartResponse>

    //SPG Bag
    @POST("bag-spg")
    fun actionPosAddSpg(@Body cartRequest: CartRequest): Deferred<CartResponse>

    @GET("bag-spg")
    fun actionPosGetSpg(): Deferred<CartListResponse>

    @PUT("bag-spg")
    fun actionPosUpdateSpg(@Body cartRequest: CartRequest): Deferred<CartResponse>

    @DELETE("bag-spg/{id}")
    fun actionPosDeleteSpg(@Path("id") id: String): Deferred<CartResponse>

    @POST("qr")
    fun actionCofirmPos(@Body qrRequest: QrRequest): Deferred<QrResponse>

    @POST("qr-sale")
    fun actionCofirmPosSpg(@Body qrRequest: QrRequestSpg): Deferred<QrResponse>

    @POST("pembatalan")
    fun actionBatalkan(@Body pembatalanRequest: PembatalanRequest): Deferred<PembatalanResponse>

    @GET("master/provinsi")
    fun provinsiAsync(): Deferred<ProvinsiResponse>

    @GET("master/kota-kabupaten")
    fun kotaKabAsync(@Query("provinsi") prov: String): Deferred<KabKotaResponse>

    @GET("master/kecamatan")
    fun kecamatanAsync(@Query("kota-kabupaten") kabkota: String): Deferred<KecamatanResponse>

    @GET("master/desa-kelurahan")
    fun keluarahAsync(@Query("kecamatan") kecamatan: String): Deferred<KelurahanResponse>

    @POST("register")
    fun registerAsync(@Body registerRequest: RegisterRequest): Deferred<LoginResponse>

    @POST("attendance")
    fun attendance(@Body attendanceRequest: AttendanceRequest): Deferred<AttendanceCheckResponse>

    @GET("attendance")
    fun attendanceCheck(): Deferred<AttendanceCheckResponse>

    @POST("me/reset-password")
    fun passwordAsync(@Body passwordRequest: PasswordRequest): Deferred<LoginResponse>

    @POST("me/update")
    fun updateProfileAsync(@Body registerRequest: RegisterRequest): Deferred<LoginResponse>

    @POST("me/change-photo")
    fun updatePhotoAsync(@Body photoRequest: PhotoRequest): Deferred<UpdatePhotoResponse>

    @GET("me")
    fun getProfileAsync(): Deferred<ProfileResponse>

    @POST("konfirmasi-pengiriman")
    fun konfirmKirim(@Body confirmRequest: ConfirmRequest): Deferred<LoginResponse>

    @GET("local-partner")
    fun getLocalPartner(): Deferred<LpResponse>

    @GET("payment/{trx_code}")
    fun detailPembayaran(@Path("trx_code") trxCode: String): Deferred<PaymentDetailResponse>

    @POST("payment/check-status")
    fun cekStatusPembayaran(@Body payRequest: PayRequest): Deferred<PaymentDetailResponse>

    @GET("payment/status")
    fun getPaymentStatusAsync(): Deferred<PaymentStatusResponse>

    @GET("report")
    fun getReportAsync(@Query("search") search: String, @Query("status") status: String,
                       @Query("type") type: String, @Query("offset") offset: String,
                       @Query("limit") limit: String, @Query("date1") date1: String,
                       @Query("date2") date2: String): Deferred<ReportResponse>

    @GET("mutasi")
    fun getMutasiAsync(@Query("offset") offset: String,
                       @Query("limit") limit: String, @Query("date1") date1: String,
                       @Query("date2") date2: String): Deferred<MutasiResponse>

    @GET("kembali-barang/{lp_id}")
    fun getListKembaliAsync(@Path("lp_id") lpId: String): Deferred<ProdukListResponse>

    @POST("kembali-barang")
    fun kembalikanBarangAsync(@Body kembaliBarangRequest: KembaliBarangRequest): Deferred<ProdukListResponse>

    @GET("kembali-barang/konfirmasi")
    fun getBarangKonfirmasiAsync(): Deferred<ProdukListResponse>

    @POST("kembali-barang/konfirmasi")
    fun konfirmasiKembaliAsync(): Deferred<ProdukListResponse>

    @POST("pencairan")
    fun pencairanDanaAsync(): Deferred<ProdukListResponse>

    @GET("toko")
    fun getListTokoAsync(@Query("latitude") lat: String, @Query("longitude") long: String): Deferred<ListTokoResponse>

    @GET("toko")
    fun getAllTokoAsync(): Deferred<ListTokoResponse>

    @POST("toko")
    fun registerTokoAsync(@Body registerRequest: RegisterRequest): Deferred<LoginResponse>
}