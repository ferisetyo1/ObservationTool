package feri.com.observationtool.api

import com.google.gson.GsonBuilder
import feri.com.observationtool.util.Constant
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

object ApiConfig {
    private lateinit var interceptor: HttpLoggingInterceptor
    private lateinit var okHttpClient: OkHttpClient
    private var retrofit: Retrofit? = null


    val client: Retrofit
        get() {
            interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            okHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectionSpecs(
                    Arrays.asList(
                        ConnectionSpec.MODERN_TLS,
                        ConnectionSpec.CLEARTEXT,
                        ConnectionSpec.COMPATIBLE_TLS
                    )
                )
                .followRedirects(true)
                .followSslRedirects(true)
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .cache(null)
                .build()

            var gson = GsonBuilder().setLenient().create()

            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(okHttpClient)
                    .build()
            }
            return retrofit!!

        }

    public fun getApi(): ApiService {
        return client.create(ApiService::class.java)
    }
}