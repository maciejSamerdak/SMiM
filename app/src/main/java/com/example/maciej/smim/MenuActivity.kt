package com.example.maciej.smim

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class MenuActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val newMultiplayerGameButton = findViewById<Button>(R.id.multiplayerButton)
        val newHotseatGameButton = findViewById<Button>(R.id.hotseatButton)
        val scoreBoardButton = findViewById<Button>(R.id.scoreboardButton)
        val quitButton = findViewById<Button>(R.id.quitButton)
        val logoutButton = findViewById<Button>(R.id.logoutButton)

        val intent =  Intent(this, HotseatGameActivity::class.java)
        newHotseatGameButton.setOnClickListener { startActivity(intent)}
        logoutButton.setOnClickListener {
            auth.signOut()
        }
    }
}
