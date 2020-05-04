package com.example.maciej.smim.login_register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.maciej.smim.MainActivity
import com.example.maciej.smim.R
import com.google.firebase.auth.FirebaseAuth


class SignupActivity : AppCompatActivity() {
    private var inputEmail: EditText? = null
    private var inputPassword: EditText? = null
    private var btnSignIn: Button? = null
    private var btnSignUp: Button? = null
    private var btnResetPassword: Button? = null
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance()
        btnSignIn = findViewById<View>(R.id.sign_in_button) as Button
        btnSignUp = findViewById<View>(R.id.sign_up_button) as Button
        inputEmail = findViewById<View>(R.id.email) as EditText
        inputPassword = findViewById<View>(R.id.password) as EditText
        btnResetPassword = findViewById<View>(R.id.btn_reset_password) as Button

        btnResetPassword!!.setOnClickListener { startActivity(Intent(this@SignupActivity, ResetPasswordActivity::class.java)) }

        btnSignIn!!.setOnClickListener { finish() }

        btnSignUp!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val email = inputEmail!!.text.toString().trim { it <= ' ' }
                val password = inputPassword!!.text.toString().trim { it <= ' ' }
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(
                        applicationContext,
                        "Enter email address!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_SHORT)
                        .show()
                    return
                }
                if (password.length < 6) {
                    Toast.makeText(
                        applicationContext,
                        "Password too short, enter minimum 6 characters!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(
                        this@SignupActivity
                    ) { task ->
                        Toast.makeText(
                            this@SignupActivity,
                            "createUserWithEmail:onComplete:" + task.isSuccessful,
                            Toast.LENGTH_SHORT
                        ).show()
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful) {
                            Toast.makeText(
                                this@SignupActivity,
                                "Authentication failed." + task.exception,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            startActivity(
                                Intent(
                                    this@SignupActivity,
                                    MainActivity::class.java
                                )
                            )
                            finish()
                        }
                    }
            }
        })
    }
}
