package com.example.maciej.smim.login_register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.maciej.smim.R
import com.google.firebase.auth.FirebaseAuth


class ResetPasswordActivity : AppCompatActivity() {
    private var inputEmail: EditText? = null
    private var btnReset: Button? = null
    private var btnBack: Button? = null
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        inputEmail = findViewById<View>(R.id.friendsName) as EditText
        btnReset = findViewById<View>(R.id.btn_reset_password) as Button
        btnBack = findViewById<View>(R.id.btn_back) as Button
        auth = FirebaseAuth.getInstance()
        btnBack!!.setOnClickListener { finish() }
        btnReset!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val email = inputEmail!!.text.toString().trim { it <= ' ' }
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(
                        application,
                        "Enter your registered email id",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this@ResetPasswordActivity,
                                "We have sent you instructions to reset your password!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this@ResetPasswordActivity,
                                "Failed to send reset email!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        })
    }
}
