package com.mubarak.android_degger_hilt.di

import android.app.Application
import android.net.Uri.decode
import android.util.Log
import androidx.room.Room
import com.mubarak.android_degger_hilt.network.ApiService
import com.mubarak.android_degger_hilt.room.HomeDatabase
import com.mubarak.android_degger_hilt.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.net.CookiePolicy
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val NO_OF_LOG_CHAR = 1000
@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    companion object{
        private const val TAG = "ApiClient"
        private const val CONNECT_TIMEOUT_MULTIPLIER = 1
        private const val DEFAULT_CONNECT_TIMEOUT_IN_SEC = 60 * CONNECT_TIMEOUT_MULTIPLIER
        private const val DEFAULT_WRITE_TIMEOUT_IN_SEC = 60 * CONNECT_TIMEOUT_MULTIPLIER
        private const val DEFAULT_READ_TIMEOUT_IN_SEC = 60 * CONNECT_TIMEOUT_MULTIPLIER
    }
    private val sDispatcher: Dispatcher? = null


    @Singleton
    @Provides
    fun providesApiObj(): ApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttpClientBuilder().build())
            .build()
            .create(ApiService::class.java)
    }

        private fun getOkHttpClientBuilder(): OkHttpClient.Builder {
        /*OkHttp client builder*/
        val oktHttpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(
                (CONNECT_TIMEOUT_MULTIPLIER * DEFAULT_CONNECT_TIMEOUT_IN_SEC).toLong(),
                TimeUnit.SECONDS
            )
            .writeTimeout(
                (CONNECT_TIMEOUT_MULTIPLIER * DEFAULT_WRITE_TIMEOUT_IN_SEC).toLong(),
                TimeUnit.SECONDS
            )
            .readTimeout(
                (CONNECT_TIMEOUT_MULTIPLIER * DEFAULT_READ_TIMEOUT_IN_SEC).toLong(),
                TimeUnit.SECONDS
            )
            .cookieJar(JavaNetCookieJar(getCookieManager())) /* Using okhttp3 cookie instead of java net cookie*/
        oktHttpClientBuilder.dispatcher(getDispatcher())

        oktHttpClientBuilder.addInterceptor { chain ->
            val builder = chain.request().newBuilder()
                .addHeader("Content-Type", "text/html")
                .addHeader("authKey", "")
                .addHeader("token", "")
               .addHeader("Authorization",  Credentials.basic(username = "", password = ""))
            chain.proceed(builder.build())
        }
        oktHttpClientBuilder.addInterceptor(getHttpLoggingInterceptor())
        oktHttpClientBuilder.addInterceptor { chain ->
            var request = chain.request()

            printPostmanFormattedLog(request)

            var response = chain.proceed(request)
            Log.d(TAG, "intercept: " + response.code)
            val token = response.header("token")
            response
        }
        return oktHttpClientBuilder
    }
    private fun getHttpLoggingInterceptor(): Interceptor {
        val loggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                if (message.length > NO_OF_LOG_CHAR) {
                    for (noOfLogs in 0..message.length / NO_OF_LOG_CHAR) {
                        if (noOfLogs * NO_OF_LOG_CHAR + NO_OF_LOG_CHAR < message.length) {
                            Log.d(
                                TAG,
                                message.substring(
                                    noOfLogs * NO_OF_LOG_CHAR,
                                    noOfLogs * NO_OF_LOG_CHAR + NO_OF_LOG_CHAR
                                )
                            )
                        } else {
                            Log.d(
                                TAG,
                                message.substring(noOfLogs * NO_OF_LOG_CHAR, message.length)
                            )
                        }
                    }
                } else {
                    Log.d(TAG, message)
                }
            }
        })
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    private fun printPostmanFormattedLog(request: Request) {
        try {
            val allParams: String
            allParams = if (request.method == "GET" || request.method == "DELETE") {
                request.url.toString().substring(
                    request.url.toString().indexOf("?") + 1,
                    request.url.toString().length
                )
            } else {
                val buffer = Buffer()
                request.body!!.writeTo(buffer)
                buffer.readString(Charset.forName("UTF-8"))
            }
            val params =
                allParams.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val paramsString = StringBuilder("\n")
            for (param in params) {
                paramsString.append(decode(param.replace("=", ":")))
                paramsString.append("\n")
            }
            Log.d(TAG, paramsString.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun getCookieManager(): CookieManager {
        val cookieManager = CookieManager()
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
        return cookieManager
    }

    fun getDispatcher(): Dispatcher {
        return sDispatcher ?: Dispatcher()
    }

    /***
     * Room data base
     */

    @Singleton
    @Provides
    fun provideDatabase(app: Application): HomeDatabase =
        Room.databaseBuilder(app,
            HomeDatabase::class.java,
            "local_database"
        )
            .allowMainThreadQueries()
            .addMigrations(HomeDatabase.migration_1_2)
            .build()

}