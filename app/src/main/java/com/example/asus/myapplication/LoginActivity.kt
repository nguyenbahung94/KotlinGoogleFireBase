package com.example.asus.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login_api19.*

/**
 * Created by asus on 9/6/17.
 */
class LoginActivity : AppCompatActivity() {
    private var instance: LoginActivity? = null
    private lateinit var database: DatabaseReference
    private lateinit var prefs: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_api19)
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.INTERNET), 123)
        prefs = this.getSharedPreferences(common.PREFS_FILENAME, 0)
        instance = this
        database = FirebaseDatabase.getInstance().reference
        btnLogin.setOnClickListener {
            var login: Boolean = false
            val Id: String = edtName.text.trim().toString()
            val pass: String = edtPassword.text.trim().toString()
            database.child("user").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {}
                override fun onDataChange(p0: DataSnapshot?) {
                    if (p0 != null) {
                        for (childSnapshot in p0.getChildren()) {
                            val user = childSnapshot.getValue(User::class.java)
                            if (user != null) {
                                if (user.id.trim() == Id) {
                                    if (user.pass.trim() == pass) {
                                        login = true
                                        if (user.admin.trim() == "true") {
                                            Toast.makeText(applicationContext, "Hi Admin", Toast.LENGTH_SHORT).show()
                                        }
                                        if (user.admin.trim() == "false") {
                                            val editer = prefs.edit()
                                            editer.putString(common.NAME, user.name)
                                            editer.putString(common.KEY, "-"+user.key)
                                            Log.e("Common",user.name+"//key//"+user.key)
                                            editer.apply()
                                            Toast.makeText(applicationContext, "Hi User ", Toast.LENGTH_SHORT).show()
                                            val intent = Intent(instance, MainActivity::class.java)
                                            intent.putExtra("number", 1)
                                            startActivity(intent)
                                        }
                                    }
                                }
                            }

                        }
                        if (!login) {
                            Toast.makeText(applicationContext, "Login Failed", Toast.LENGTH_SHORT).show()
                        }

                    }

                }

            })


            /*  if (name != "" && pass != "") {
                  if (name == "admin" && pass == "admin") {
                      Toast.makeText(this, " ahihi ^_^ ", Toast.LENGTH_SHORT).show()
                      val intent = Intent(this, MainActivity::class.java)
                      startActivity(intent)
                  }
              } else {
                  Toast.makeText(this, " ahihi do ngok ", Toast.LENGTH_SHORT).show()
              }*/
        }

    }

}