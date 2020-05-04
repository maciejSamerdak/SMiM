package com.example.maciej.smim

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val newMultiplayerGameButton = findViewById<Button>(R.id.multiplayerButton)
        val newHotseatGameButton = findViewById<Button>(R.id.hotseatButton)
        val scoreBoardButton = findViewById<Button>(R.id.scoreboardButton)
        val quitButton = findViewById<Button>(R.id.quitButton)

        val intent =  Intent(this, MainActivity::class.java)
        newHotseatGameButton.setOnClickListener({v -> startActivity(intent)})
    }
}
