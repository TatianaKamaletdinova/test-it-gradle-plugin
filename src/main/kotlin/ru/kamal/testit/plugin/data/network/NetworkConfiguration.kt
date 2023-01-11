package ru.kamal.testit.plugin.data.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.kamal.testit.plugin.data.network.moshi.BigDecimalAdapter
import ru.kamal.testit.plugin.data.network.moshi.EnumAdapterFactory
import ru.kamal.testit.plugin.data.network.moshi.EnumConverterFactory
import ru.kamal.testit.plugin.data.network.moshi.PrimitiveAdapter
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class NetworkConfiguration(
    testITUrl: String,
    private val privateToken: String
) {

    companion object {
        private const val HEADER = "Authorization"
        private const val HEADER_PREFIX = "PrivateToken"
    }

    private val defaultOkHttpClient: OkHttpClient = OkHttpClient.Builder()
        .build()

    private val defaultMoshi: Moshi = Moshi.Builder()
        .add(BigDecimalAdapter())
        .add(PrimitiveAdapter())
        .add(EnumAdapterFactory())
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val defaultRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(testITUrl)
            .addConverterFactory(UnitOnEmptyBodyConverterFactory())
            .addConverterFactory(EnumConverterFactory())
            .addConverterFactory(
                MoshiConverterFactory.create(defaultMoshi.newBuilder().build()).asLenient()
            )
            .build()
    }

    fun provideTestITApi(): TestITApi {
        val trustAllCerts: Array<TrustManager> = arrayOf(
            object : X509TrustManager {
                override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {}
                override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {}
                override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
            }
        )

        val clientOk: OkHttpClient.Builder = defaultOkHttpClient.newBuilder()
            .addInterceptor(Interceptor { chain ->
                chain.proceed(
                    chain.request()
                        .newBuilder()
                        .addHeader(HEADER, "$HEADER_PREFIX $privateToken")
                        .build()
                )
            })
            .apply {
                val logging = HttpLoggingInterceptor { message -> println(message) }
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                addInterceptor(logging)
            }
            .apply {
                val sslContext: SSLContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, SecureRandom())
                sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
            }
            .hostnameVerifier { _, _ -> true }

        val retrofit = defaultRetrofit.newBuilder()
            .client(clientOk.build())
            .build()

        return retrofit.create(TestITApi::class.java)
    }
}