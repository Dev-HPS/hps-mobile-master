package com.hastaprimasolusi.rana.data.network

import com.hastaprimasolusi.rana.data.ApiService
import com.hastaprimasolusi.rana.data.network.requesthelper.ApproveRequest
import com.hastaprimasolusi.rana.data.network.requesthelper.AttendanceRequest
import com.hastaprimasolusi.rana.data.network.requesthelper.CartRequest
import com.hastaprimasolusi.rana.data.network.requesthelper.CategoryRequest
import com.hastaprimasolusi.rana.data.network.requesthelper.CheckoutRequest
import com.hastaprimasolusi.rana.data.network.requesthelper.ConfirmRequest
import com.hastaprimasolusi.rana.data.network.requesthelper.DeliverRequest
import com.hastaprimasolusi.rana.data.network.requesthelper.KembaliBarangRequest
import com.hastaprimasolusi.rana.data.network.requesthelper.ListProdukRequest
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

/**
 * Created By maasrahman on 5/14/20
 */

interface ApiRepository {
    suspend fun authentication(loginRequest: LoginRequest): ResultData<LoginResponse>
    suspend fun getListProduk(produkRequest: ListProdukRequest): ResultData<ProdukListResponse>
    suspend fun getProdukDetail(idProduk: String): ResultData<ProdukDetailResponse>
    suspend fun getListCategory(categoryRequest: CategoryRequest): ResultData<CategoryResponse>
    suspend fun cartList(): ResultData<CartListResponse>
    suspend fun cartAdd(cartRequest: CartRequest): ResultData<CartResponse>
    suspend fun cartUpdate(cartRequest: CartRequest): ResultData<CartResponse>
    suspend fun cartDelete(id: String): ResultData<CartResponse>
    suspend fun checkOut(): ResultData<CheckOutResponse>
    suspend fun checkOutLp(checkoutRequest: CheckoutRequest): ResultData<CheckOutResponse>
    suspend fun getHistoryPesan(
        limit: String,
        offset: String,
        status: String,
        dtStart: String,
        dtEnd: String,
    ): ResultData<HistoryOrderResponse>

    suspend fun getHistoryPesanDetail(id: String): ResultData<HistoryDetailResponse>
    suspend fun getPesanan(
        limit: String,
        offset: String,
        status: String,
        dtStart: String,
        dtEnd: String,
    ): ResultData<HistoryOrderResponse>

    suspend fun getPesananDetail(id: String): ResultData<OrderDetailResponse>
    suspend fun approvePesanan(approveRequest: ApproveRequest): ResultData<OrderDetailResponse>
    suspend fun confirmPesanan(confirmRequest: ConfirmRequest): ResultData<OrderDetailResponse>
    suspend fun getPayMethod(): ResultData<PayMethodResponse>
    suspend fun setPayMethod(payRequest: PayRequest): ResultData<PaymentResponse>
    suspend fun getOrderStatus(type: String): ResultData<OrderStatusResponse>
    suspend fun getDeliverCourier(): ResultData<DeliveryCourierResponse>
    suspend fun setDeliveryCourier(deliverRequest: DeliverRequest): ResultData<DeliveryCourierResponse>
    suspend fun getRiwayatCanvasser(
        limit: String,
        offset: String,
        status: String,
        dtStart: String,
        dtEnd: String,
    ): ResultData<RiwayatCanvasResponse>

    suspend fun getRiwayatCanvasserDetail(id: String): ResultData<RiwayatDetCnvsResponse>
    suspend fun getRiwayatSpg(
        limit: String,
        offset: String,
        status: String,
        dtStart: String,
        dtEnd: String,
    ): ResultData<RiwayatCanvasResponse>

    suspend fun getRiwayatSpgDetail(id: String): ResultData<RiwayatDetCnvsResponse>

    //    suspend fun getCanvasBag(): ResultData<CanvasBagResponse>
    suspend fun posList(): ResultData<CartListResponse>
    suspend fun posAdd(cartRequest: CartRequest): ResultData<CartResponse>
    suspend fun posUpdate(cartRequest: CartRequest): ResultData<CartResponse>
    suspend fun posDelete(id: String): ResultData<CartResponse>
    suspend fun posListSpg(): ResultData<CartListResponse>
    suspend fun posAddSpg(cartRequest: CartRequest): ResultData<CartResponse>
    suspend fun posUpdateSpg(cartRequest: CartRequest): ResultData<CartResponse>
    suspend fun posDeleteSpg(id: String): ResultData<CartResponse>
    suspend fun posConfirm(qrRequest: QrRequest): ResultData<QrResponse>
    suspend fun posConfirmSpg(qrRequestSpg: QrRequestSpg): ResultData<QrResponse>
    suspend fun attendance(attendanceRequest: AttendanceRequest): ResultData<AttendanceCheckResponse>
    suspend fun attendanceCheck(): ResultData<AttendanceCheckResponse>
    suspend fun actionBatal(pembatalanRequest: PembatalanRequest): ResultData<PembatalanResponse>
    suspend fun getProv(): ResultData<ProvinsiResponse>
    suspend fun getKabKota(idProv: String): ResultData<KabKotaResponse>
    suspend fun getKec(idKota: String): ResultData<KecamatanResponse>
    suspend fun getKel(idKec: String): ResultData<KelurahanResponse>
    suspend fun registerMitra(registerRequest: RegisterRequest): ResultData<LoginResponse>
    suspend fun resetPassword(passwordRequest: PasswordRequest): ResultData<LoginResponse>
    suspend fun updateProfile(registerRequest: RegisterRequest): ResultData<LoginResponse>
    suspend fun updatePhoto(photoRequest: PhotoRequest): ResultData<UpdatePhotoResponse>
    suspend fun getProfile(): ResultData<ProfileResponse>
    suspend fun konfirmKirim(confirmRequest: ConfirmRequest): ResultData<LoginResponse>
    suspend fun getLp(): ResultData<LpResponse>
    suspend fun getDetailPembayaran(noOrder: String): ResultData<PaymentDetailResponse>
    suspend fun getStatusPembayaran(payRequest: PayRequest): ResultData<PaymentDetailResponse>
    suspend fun getReport(
        search: String, status: String, type: String, offset: String, limit: String,
        date1: String, date2: String,
    ): ResultData<ReportResponse>

    suspend fun getPaymentStatus(): ResultData<PaymentStatusResponse>
    suspend fun getMutasi(
        offset: String,
        limit: String,
        date1: String,
        date2: String,
    ): ResultData<MutasiResponse>

    suspend fun getListBarang(lpId: String): ResultData<ProdukListResponse>
    suspend fun kembaliBarang(kembaliBarangRequest: KembaliBarangRequest): ResultData<ProdukListResponse>
    suspend fun getBarangKonfirmasi(): ResultData<ProdukListResponse>
    suspend fun konfirmasiKembaliBarang(): ResultData<ProdukListResponse>
    suspend fun pencairanDana(): ResultData<ProdukListResponse>
    suspend fun getListToko(latitude: String,longitude: String): ResultData<ListTokoResponse>
    suspend fun getAllToko(): ResultData<ListTokoResponse>
    suspend fun daftarToko(registerRequest: RegisterRequest): ResultData<LoginResponse>
}

class ApiRepositoryImpl(private val apiService: ApiService) : ApiRepository {
    override suspend fun authentication(loginRequest: LoginRequest): ResultData<LoginResponse> {
        return try {
            val result = apiService.login(loginRequest).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun getListProduk(produkRequest: ListProdukRequest): ResultData<ProdukListResponse> {
        return try {
            val result = apiService.getProdukList(
                produkRequest.offset,
                produkRequest.limit, produkRequest.search, produkRequest.category,
                produkRequest.type, produkRequest.lpCode
            ).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun getProdukDetail(idProduk: String): ResultData<ProdukDetailResponse> {
        return try {
            val result = apiService.getProdukDetail(idProduk).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun getListCategory(categoryRequest: CategoryRequest): ResultData<CategoryResponse> {
        return try {
            val result = apiService.getCategoryList(
                categoryRequest.limit ?: "", categoryRequest.type ?: ""
            ).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun cartList(): ResultData<CartListResponse> {
        return try {
            val result = apiService.getCartList().await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun cartAdd(cartRequest: CartRequest): ResultData<CartResponse> {
        return try {
            val result = apiService.actionCart(cartRequest).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun cartUpdate(cartRequest: CartRequest): ResultData<CartResponse> {
        return try {
            val result = apiService.actionCartUpdate(cartRequest).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun cartDelete(id: String): ResultData<CartResponse> {
        return try {
            val result = apiService.actionCartDelete(id).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun checkOut(): ResultData<CheckOutResponse> {
        return try {
            val result = apiService.actionCheckOut().await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun checkOutLp(checkoutRequest: CheckoutRequest): ResultData<CheckOutResponse> {
        return try {
            val result = apiService.actionCheckOutLp(checkoutRequest).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun getHistoryPesan(
        limit: String,
        offset: String,
        status: String,
        dtStart: String,
        dtEnd: String,
    ): ResultData<HistoryOrderResponse> {
        return try {
            val result = apiService.getHistoryPesan(limit, offset, status, dtStart, dtEnd).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun getHistoryPesanDetail(id: String): ResultData<HistoryDetailResponse> {
        return try {
            val result = apiService.getHistoryPesanDetail(id).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun getPesanan(
        limit: String,
        offset: String,
        status: String,
        dtStart: String,
        dtEnd: String,
    ): ResultData<HistoryOrderResponse> {
        return try {
            val result = apiService.getPesanan(limit, offset, status, dtStart, dtEnd).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun getPesananDetail(id: String): ResultData<OrderDetailResponse> {
        return try {
            val result = apiService.getPesananDetail(id).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun approvePesanan(approveRequest: ApproveRequest): ResultData<OrderDetailResponse> {
        return try {
            val result = apiService.approvePesanan(approveRequest).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun confirmPesanan(confirmRequest: ConfirmRequest): ResultData<OrderDetailResponse> {
        return try {
            val result = apiService.orderConfirm(confirmRequest).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun getPayMethod(): ResultData<PayMethodResponse> {
        return try {
            val result = apiService.getPayMethod().await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun setPayMethod(payRequest: PayRequest): ResultData<PaymentResponse> {
        return try {
            val result = apiService.setPayMethod(payRequest).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun getOrderStatus(type: String): ResultData<OrderStatusResponse> {
        return try {
            val result = apiService.getOrderStatus(type).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun getDeliverCourier(): ResultData<DeliveryCourierResponse> {
        return try {
            val result = apiService.getDeliveryCourier().await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun setDeliveryCourier(deliverRequest: DeliverRequest): ResultData<DeliveryCourierResponse> {
        return try {
            val result = apiService.setDeliveryCourier(deliverRequest).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun getRiwayatCanvasser(
        limit: String,
        offset: String,
        status: String,
        dtStart: String,
        dtEnd: String,
    ): ResultData<RiwayatCanvasResponse> {
        return try {
            val result =
                apiService.getRiwayatCanvasser(limit, offset, status, dtStart, dtEnd).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun getRiwayatCanvasserDetail(id: String): ResultData<RiwayatDetCnvsResponse> {
        return try {
            val result = apiService.getRiwayatCanvasserDetail(id).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun getRiwayatSpg(
        limit: String,
        offset: String,
        status: String,
        dtStart: String,
        dtEnd: String,
    ): ResultData<RiwayatCanvasResponse> {
        return try {
            val result = apiService.getRiwayatSpg(limit, offset, status, dtStart, dtEnd).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun getRiwayatSpgDetail(id: String): ResultData<RiwayatDetCnvsResponse> {
        return try {
            val result = apiService.getRiwayatSpgDetail(id).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

//    override suspend fun getCanvasBag(): ResultData<CanvasBagResponse> {
//        return try {
//            val result = apiService.actionPosGet().await()
//            ResultData.Success(result)
//        }catch (ex: Exception){
//            ResultData.Error(ex)
//        }
//    }

    override suspend fun posList(): ResultData<CartListResponse> {
        return try {
            val result = apiService.actionPosGet().await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun posAdd(cartRequest: CartRequest): ResultData<CartResponse> {
        return try {
            val result = apiService.actionPosAdd(cartRequest).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun posUpdate(cartRequest: CartRequest): ResultData<CartResponse> {
        return try {
            val result = apiService.actionPosUpdate(cartRequest).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun posDelete(id: String): ResultData<CartResponse> {
        return try {
            val result = apiService.actionPosDelete(id).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun posConfirm(qrRequest: QrRequest): ResultData<QrResponse> {
        return try {
            val result = apiService.actionCofirmPos(qrRequest).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    //SPG
    override suspend fun posListSpg(): ResultData<CartListResponse> {
        return try {
            val result = apiService.actionPosGetSpg().await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun posAddSpg(cartRequest: CartRequest): ResultData<CartResponse> {
        return try {
            val result = apiService.actionPosAddSpg(cartRequest).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun posUpdateSpg(cartRequest: CartRequest): ResultData<CartResponse> {
        return try {
            val result = apiService.actionPosUpdateSpg(cartRequest).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun posDeleteSpg(id: String): ResultData<CartResponse> {
        return try {
            val result = apiService.actionPosDeleteSpg(id).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun posConfirmSpg(qrRequestSpg: QrRequestSpg): ResultData<QrResponse> {
        return try {
            val result = apiService.actionCofirmPosSpg(qrRequestSpg).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun attendance(attendanceRequest: AttendanceRequest): ResultData<AttendanceCheckResponse> {
        return try {
            val result = apiService.attendance(attendanceRequest).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun attendanceCheck(): ResultData<AttendanceCheckResponse> {
        return try {
            val result = apiService.attendanceCheck().await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun actionBatal(pembatalanRequest: PembatalanRequest): ResultData<PembatalanResponse> {
        return try {
            val result = apiService.actionBatalkan(pembatalanRequest).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun getProv(): ResultData<ProvinsiResponse> {
        return try {
            val result = apiService.provinsiAsync().await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun getKabKota(idProv: String): ResultData<KabKotaResponse> {
        return try {
            val result = apiService.kotaKabAsync(idProv).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun getKec(idKota: String): ResultData<KecamatanResponse> {
        return try {
            val result = apiService.kecamatanAsync(idKota).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun getKel(idKec: String): ResultData<KelurahanResponse> {
        return try {
            val result = apiService.keluarahAsync(idKec).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun registerMitra(registerRequest: RegisterRequest): ResultData<LoginResponse> {
        return try {
            val result = apiService.registerAsync(registerRequest).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun resetPassword(passwordRequest: PasswordRequest): ResultData<LoginResponse> {
        return try {
            val result = apiService.passwordAsync(passwordRequest).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun updateProfile(registerRequest: RegisterRequest): ResultData<LoginResponse> {
        return try {
            val result = apiService.updateProfileAsync(registerRequest).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun updatePhoto(photoRequest: PhotoRequest): ResultData<UpdatePhotoResponse> {
        return try {
            val result = apiService.updatePhotoAsync(photoRequest).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun getProfile(): ResultData<ProfileResponse> {
        return try {
            val result = apiService.getProfileAsync().await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun konfirmKirim(confirmRequest: ConfirmRequest): ResultData<LoginResponse> {
        return try {
            val result = apiService.konfirmKirim(confirmRequest).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun getLp(): ResultData<LpResponse> {
        return try {
            val result = apiService.getLocalPartner().await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun getDetailPembayaran(noOrder: String): ResultData<PaymentDetailResponse> {
        return try {
            val result = apiService.detailPembayaran(noOrder).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun getStatusPembayaran(payRequest: PayRequest): ResultData<PaymentDetailResponse> {
        return try {
            val result = apiService.cekStatusPembayaran(payRequest).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun getReport(
        search: String,
        status: String,
        type: String,
        offset: String,
        limit: String,
        date1: String,
        date2: String,
    ): ResultData<ReportResponse> {
        return try {
            val result =
                apiService.getReportAsync(search, status, type, offset, limit, date1, date2).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun getPaymentStatus(): ResultData<PaymentStatusResponse> {
        return try {
            val result = apiService.getPaymentStatusAsync().await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun getMutasi(
        offset: String,
        limit: String,
        date1: String,
        date2: String,
    ): ResultData<MutasiResponse> {
        return try {
            val result = apiService.getMutasiAsync(offset, limit, date1, date2).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun getListBarang(lpId: String): ResultData<ProdukListResponse> {
        return try {
            val result = apiService.getListKembaliAsync(lpId).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun kembaliBarang(kembaliBarangRequest: KembaliBarangRequest): ResultData<ProdukListResponse> {
        return try {
            val result = apiService.kembalikanBarangAsync(kembaliBarangRequest).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun getBarangKonfirmasi(): ResultData<ProdukListResponse> {
        return try {
            val result = apiService.getBarangKonfirmasiAsync().await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun konfirmasiKembaliBarang(): ResultData<ProdukListResponse> {
        return try {
            val result = apiService.konfirmasiKembaliAsync().await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun pencairanDana(): ResultData<ProdukListResponse> {
        return try {
            val result = apiService.pencairanDanaAsync().await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun getListToko(latitude: String, longitude: String): ResultData<ListTokoResponse> {
        return try {
            val result = apiService.getListTokoAsync(latitude,longitude).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun daftarToko(registerRequest: RegisterRequest): ResultData<LoginResponse> {
        return try {
            val result = apiService.registerTokoAsync(registerRequest).await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }

    override suspend fun getAllToko(): ResultData<ListTokoResponse> {
        return try {
            val result = apiService.getAllTokoAsync().await()
            ResultData.Success(result)
        } catch (ex: Exception) {
            ResultData.Error(ex)
        }
    }
}