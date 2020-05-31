package com.example.maciej.smim

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class ScoreboardActivity : AppCompatActivity()  {
    private var db = FirebaseDatabase.getInstance()
    private lateinit var row1: TableRow
    private lateinit var table: TableLayout
    private var listOfRows: MutableList<TableRow> = mutableListOf<TableRow>()
    private var listOfChildren: MutableList<DataSnapshot> = mutableListOf<DataSnapshot>()
    private var listOfTextViews: MutableList<MutableList<TextView>> = mutableListOf<MutableList<TextView>>()
    private var rowsNumber: Int = 0
    private lateinit var ref: DatabaseReference
    private var fontSize: Float = 20F
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scoreboard)
        row1 = findViewById(R.id.row1)
        table = findViewById(R.id.table)
        listOfRows.add(findViewById(R.id.row2))
        listOfTextViews.add(mutableListOf<TextView>())
        listOfTextViews[0].add(findViewById(R.id.username))
        listOfTextViews[0].add(findViewById(R.id.winsNumber))
        listOfTextViews[0].add(findViewById(R.id.drawsNumber))
        listOfTextViews[0].add(findViewById(R.id.losesNumber))
        listOfTextViews[0][0].textSize = fontSize
        listOfTextViews[0][1].textSize = fontSize
        listOfTextViews[0][2].textSize = fontSize
        listOfTextViews[0][3].textSize = fontSize
        listOfTextViews[0][0].text = "Player"
        listOfTextViews[0][1].text = "Wins"
        listOfTextViews[0][2].text = "Draws"
        listOfTextViews[0][3].text = "Loses"

        db.getReference("users").orderByChild("wins").addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                rowsNumber = dataSnapshot.childrenCount.toInt()
                println(dataSnapshot.child("filipW").child("wins").value)
                for (x in 1..rowsNumber){
                    listOfRows.add(TableRow(this@ScoreboardActivity))
                    table.addView(listOfRows[x])
                    listOfTextViews.add(mutableListOf<TextView>())
                    for (y in 0..3){
                        listOfTextViews[x].add(TextView(this@ScoreboardActivity))
                        listOfRows[x].addView(listOfTextViews[x][y])
                    }
                }
                var i: Int = rowsNumber
                for (child in dataSnapshot.children){
                    listOfChildren.add(child)
                    listOfTextViews[i][0].textSize = fontSize
                    listOfTextViews[i][1].textSize = fontSize
                    listOfTextViews[i][2].textSize = fontSize
                    listOfTextViews[i][3].textSize = fontSize
                    listOfTextViews[i][0].text = child.child("name").value.toString()
                    listOfTextViews[i][1].text = child.child("wins").value.toString()
                    listOfTextViews[i][2].text = child.child("draws").value.toString()
                    listOfTextViews[i][3].text = child.child("loses").value.toString()
                    for (j in 1..3){
                        listOfTextViews[i][j].textAlignment = View.TEXT_ALIGNMENT_CENTER
                    }
                    i -= 1

                }
            }
            override fun onCancelled(p0: DatabaseError) {}

        })
    }
}