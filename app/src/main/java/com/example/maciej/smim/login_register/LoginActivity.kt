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
import com.example.maciej.smim.MenuActivity
import com.example.maciej.smim.R
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {
    private var inputEmail: EditText? = null
    private var inputPassword: EditText? = null
    private lateinit var auth: FirebaseAuth
    private var btnSignup: Button? = null
    private var btnLogin: Button? = null
    private var btnReset: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }

        // set the view now
        setContentView(R.layout.activity_login)

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        inputEmail = findViewById<View>(R.id.email) as EditText
        inputPassword = findViewById<View>(R.id.password) as EditText
        btnSignup = findViewById<View>(R.id.btn_signup) as Button
        btnLogin = findViewById<View>(R.id.btn_login) as Button
        btnReset = findViewById<View>(R.id.btn_reset_password) as Button

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance()

        btnSignup!!.setOnClickListener { startActivity(Intent(this@LoginActivity, SignupActivity::class.java)) }

        btnReset!!.setOnClickListener { startActivity(Intent(this@LoginActivity, ResetPasswordActivity::class.java)) }

        btnLogin!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val email = inputEmail!!.text.toString()
                val password = inputPassword!!.text.toString()
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

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(
                        this@LoginActivity
                    ) { task ->
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful) {
                            // there was an error
                            if (password.length < 6) {
                                inputPassword!!.error = "Password too short"
                            } else {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Authentication failed",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } else {
                            val intent =
                                Intent(this@LoginActivity, MenuActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
            }
        })
    }
}
