package com.example.chatting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.btn_signup
import kotlinx.android.synthetic.main.activity_login.edt_email
import kotlinx.android.synthetic.main.activity_login.edt_pass
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mAuth= FirebaseAuth.getInstance()

        btn_signup.setOnClickListener {
            val name =edt_name.text.toString()

            val email =edt_email.text.toString()
            val password=edt_pass.text.toString()

            signup(name,email,password)
        }

    }

    private fun signup(name: String,email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    addUserToDatebase(name,email,mAuth.currentUser?.uid!!)

                    val i=Intent(this,MainActivity::class.java)
                    startActivity(i)
                } else {
                    Toast.makeText(this,"Some error occurrd",Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun addUserToDatebase(name: String, email: String, uid: String) {
        mDbRef=FirebaseDatabase.getInstance().getReference()
        mDbRef.child("user").child(uid).setValue(User(name,email,uid))
    }
}