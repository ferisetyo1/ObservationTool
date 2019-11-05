package feri.com.observationtool.api

import feri.com.observationtool.api.dao.ResultStringDao
import feri.com.observationtool.util.Constant
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @Headers(
        "Content-Type: application/json",
        "secret_key:${Constant.SECRET_KEY}"
    )
    @POST("c_kelas/tambahkelas")
    fun tambahkelas(
        @Body request:RequestBody
    ): Call<ResultStringDao>
}