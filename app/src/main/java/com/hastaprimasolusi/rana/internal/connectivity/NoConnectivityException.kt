package com.hastaprimasolusi.rana.internal.connectivity

import java.io.IOException

class NoConnectivityException : IOException() {
    override val message: String?
        get() = "Koneksi tidak tersedia, silahkan mencoba kembali"
}