package com.example.nbhung.testcallapi

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

/**
 * Created by nbhung on 10/10/2017.
 */
interface Api {
    @POST("admin/api/login")
    fun login(@Body user: User): Observable<Result>

    companion object Factory {
        fun create(): Api {
            val okhttp=OkHttpClient.Builder()
                    .connectTimeout(60,TimeUnit.SECONDS)
                    .writeTimeout(60,TimeUnit.SECONDS)
                    .readTimeout(60,TimeUnit.SECONDS)
                    .build()
            val retrofit = Retrofit.Builder()
                    .client(okhttp)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://salty-mesa-47932.herokuapp.com/")
                    .build()
            return retrofit.create(Api::class.java)
        }
    }

}