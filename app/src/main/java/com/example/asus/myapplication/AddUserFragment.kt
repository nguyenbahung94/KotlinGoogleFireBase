package com.example.asus.myapplication

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login_api19.*
import kotlinx.android.synthetic.main.fragment_add_user.*

/**
 * Created by asus on 9/6/17.
 */
class AddUserFragment : Fragment() {
    private lateinit var database: DatabaseReference
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = layoutInflater.inflate(R.layout.fragment_add_user, container, false)
        database = FirebaseDatabase.getInstance().reference
        val btnClick = view.findViewById<Button>(R.id.btnAddUser)
        btnClick.setOnClickListener {
            if (edtNameCreate == null || edtIdCreate == null || edtPasswordCreate == null) {
                Toast.makeText(activity, "Cant be null", Toast.LENGTH_SHORT).show()
            } else
                if (TextUtils.isEmpty(edtIdCreate.text) || TextUtils.isEmpty(edtName.text) || TextUtils.isEmpty(edtPassword.text)) {
                    Toast.makeText(activity, "Cant be null", Toast.LENGTH_SHORT).show()
                } else {
                    val key: String = database.child("user").key
//                               val c = Calendar.getInstance()
//                               val day: Int = c.get(Calendar.DAY_OF_MONTH)
//                               val month: Int = c.get(Calendar.MONTH)
//                               val year: Int = c.get(Calendar.YEAR)
//                               val key: String = day.toString() + (month + 1) + year.toString()
                    //      database.child("user").child(key).setValue(user)
                    val user = User(edtName.text.toString(), edtIdCreate.text.toString(), edtPassword.text.toString(), "false")
                    user.key = key
                    database.child("user").child(key).setValue(user)


                }
        }

        return view
    }
}