package com.example.maciej.smim

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Button
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val playerOneScoreView = findViewById<TextView>(R.id.player_1_score)
        val playerTwoScoreView = findViewById<TextView>(R.id.player_2_score)
        val currentPlayerNumberView = findViewById<TextView>(R.id.player_number)

        val board = findViewById<GridLayout>(R.id.board)

        var game = Game(board.rowCount,board.columnCount)
        var currentScore = game.scoreCount
        playerOneScoreView.text = currentScore[0].toString()
        playerTwoScoreView.text = currentScore[1].toString()
        currentPlayerNumberView.text = if (game.isPlayerOneTurn) "1" else "2"

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val fieldPixelSize = displayMetrics.widthPixels / board.columnCount
        val markPixelSize = fieldPixelSize/4.5f

        for ( x in 0 until board.columnCount * board.rowCount) {
            val field = Button(this)
            field.layoutParams = LinearLayout.LayoutParams(fieldPixelSize, fieldPixelSize)
            field.textSize = markPixelSize
            field.tag = x
            board.addView(field)

            field.setOnClickListener {
                // It might be best to handle all of this within the Game class but can't tell for sure
                val playerMark = game.getPlayerMark()
                val numberOfButton: Int = field.tag as Int
                if(field.text.equals("")) {
                    field.text = playerMark.symbol
                    game.refreshFields(boardToArray(board))
                    field.setTextColor(playerMark.color)

                    if(game.checkFields(numberOfButton)){
                        if(game.isPlayerOneTurn) game.updateScore(1)
                        else game.updateScore(2)
                        game.resetBoard()
                        resetBoardView(board)
                    }
                    else if(game.isBoardFull()){
                        println("It's a draw!")
                        game.resetBoard()
                        resetBoardView(board)
                    }
                    game.switchTurn()
                    // This part is changing the display, so it can stay here. We might consider making a function out of it
                    currentScore = game.scoreCount
                    playerOneScoreView.text = currentScore[0].toString()
                    playerTwoScoreView.text = currentScore[1].toString()
                    currentPlayerNumberView.text = if (game.isPlayerOneTurn) "1" else "2"
                }
            }
        }
    }

    fun boardToArray(board: GridLayout): Array<Array<String>>{
        var fields = Array(board.rowCount,{Array(board.columnCount,{""})})
        for(i in 0 until board.rowCount){
            for(j in 0 until board.columnCount){
                fields[i][j] = (board.getChildAt(i*board.rowCount+j) as Button).text.toString()
            }
        }
        return fields
    }

    fun resetBoardView(board: GridLayout){
        for ( x in 0 until board.childCount){
            val child = board.getChildAt(x)
            (child as Button).text = ""
        }
    }

}