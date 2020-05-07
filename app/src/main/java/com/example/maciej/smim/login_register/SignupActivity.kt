package com.example.maciej.smim.login_register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.maciej.smim.MenuActivity
import com.example.maciej.smim.R
import com.example.maciej.smim.users.User
import com.example.maciej.smim.users.UserService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest


class SignupActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private var inputEmail: EditText? = null
    private var inputUsername: EditText? = null
    private var inputPassword: EditText? = null
    private var btnSignIn: Button? = null
    private var btnSignUp: Button? = null
    private var btnResetPassword: Button? = null
    private var userService = UserService()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()
        btnSignIn = findViewById<View>(R.id.sign_in_button) as Button
        btnSignUp = findViewById<View>(R.id.sign_up_button) as Button
        inputEmail = findViewById<View>(R.id.email) as EditText
        inputUsername = findViewById<View>(R.id.friendsName) as EditText
        inputPassword = findViewById<View>(R.id.password) as EditText
        btnResetPassword = findViewById<View>(R.id.btn_reset_password) as Button

        btnResetPassword!!.setOnClickListener { startActivity(Intent(this@SignupActivity, ResetPasswordActivity::class.java)) }

        btnSignIn!!.setOnClickListener { finish() }

        btnSignUp!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val email = inputEmail!!.text.toString().trim { it <= ' ' }
                val password = inputPassword!!.text.toString().trim { it <= ' ' }
                val username = inputUsername!!.text.toString().trim { it <= ' ' }
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_SHORT
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
                            val profileUpdates = UserProfileChangeRequest.Builder()
                                    .setDisplayName(inputUsername!!.text.toString()).build()
                            auth.currentUser?.updateProfile(profileUpdates)
                            var user = User(auth.currentUser?.uid,email,username)
                            userService.addUserToDB(user)
                            startActivity(
                                Intent(
                                    this@SignupActivity,
                                    MenuActivity::class.java
                                )
                            )
                            finish()
                        }
                    }
            }
        })
    }
}
