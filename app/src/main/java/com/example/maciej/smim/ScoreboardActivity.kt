package com.example.maciej.smim

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class ScoreboardActivity : AppCompatActivity()  {
    private var db = FirebaseDatabase.getInstance()
    private lateinit var rrr: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scoreboard)
        rrr  = findViewById(R.id.textView)
    }
}