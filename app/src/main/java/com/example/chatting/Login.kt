package com.example.chatting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {
    
    private lateinit var mAuth:FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        
        mAuth= FirebaseAuth.getInstance()
        

        btn_signup.setOnClickListener{
            val i= Intent(this,SignUp::class.java)
            startActivity(i)
        }
        
        btn_login.setOnClickListener{
            val email =edt_email.text.toString()
            val password=edt_pass.text.toString()
            
            login(email,password)
        }

    }

    private fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val i=Intent(this,MainActivity::class.java)
                    finish()
                    startActivity(i)
                } else {
                    Toast.makeText(this,"User does not exist", Toast.LENGTH_LONG).show()

                }
            }
    }
}