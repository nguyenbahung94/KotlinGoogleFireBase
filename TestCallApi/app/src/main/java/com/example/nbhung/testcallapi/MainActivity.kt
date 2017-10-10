package com.example.nbhung.testcallapi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnClick.setOnClickListener {
            callApi()
        }
    }

    fun callApi() {
        val response = Api.create()
        val userLogin = User("Jackson15", "123456")
        response.login(userLogin)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    result ->
                    Log.e("result", result.id.toString())
                }, {
                    error ->
                    error.printStackTrace()
                })
    }
}
