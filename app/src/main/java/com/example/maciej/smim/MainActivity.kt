package com.example.maciej.smim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var game: Game
    private lateinit var playerOneScoreView: TextView
    private lateinit var playerTwoScoreView: TextView
    private lateinit var currentPlayerNumberView: TextView
    private lateinit var board: GridLayout
    private lateinit var confirmationButton: Button
    private var lastPickedField: Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playerOneScoreView = findViewById(R.id.player_1_score)
        playerTwoScoreView = findViewById(R.id.player_2_score)
        currentPlayerNumberView = findViewById(R.id.player_number)
        confirmationButton = findViewById(R.id.confirm)

        board = findViewById(R.id.board)

        game = Game(board.rowCount,board.columnCount)

        refreshView()
        changeButtonState(confirmationButton)

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
                placeMark(field, x)
            }
        }
    }

    fun placeMark(field: Button, index: Int){
        val playerMark = game.getPlayerMark()
        if(field.text.equals("")) {
            field.text = playerMark.symbol
            field.setTextColor(playerMark.color)
            if(lastPickedField >= 0){
                val previousField = board.getChildAt(lastPickedField) as Button
                previousField.text = ""
            }
            else{
                changeButtonState(confirmationButton)
            }
            lastPickedField = index
        }
    }

    fun refreshView(){
        val currentScore = game.scoreCount
        playerOneScoreView.text = currentScore[0].toString()
        playerTwoScoreView.text = currentScore[1].toString()
        currentPlayerNumberView.text = if (game.isPlayerOneTurn) "1" else "2"
    }

    fun boardToArray(): Array<Array<String>>{
        var fields = Array(board.rowCount,{Array(board.columnCount,{""})})
        for(i in 0 until board.rowCount){
            for(j in 0 until board.columnCount){
                fields[i][j] = (board.getChildAt(i*board.rowCount+j) as Button).text.toString()
            }
        }
        return fields
    }

    fun resetBoardView(){
        for ( x in 0 until board.childCount){
            val child = board.getChildAt(x)
            (child as Button).text = ""
        }
    }

    fun confirmMove(view: View){
        game.refreshFields(boardToArray())
        if(game.checkFields(lastPickedField)){
            if(game.isPlayerOneTurn) game.updateScore(1)
            else game.updateScore(2)
            game.resetBoard()
            resetBoardView()
        }
        else if(game.isBoardFull()){
            println("It's a draw!")
            game.resetBoard()
            resetBoardView()
        }
        game.switchTurn()
        lastPickedField = -1
        changeButtonState(view as Button)
        refreshView()
    }

    fun changeButtonState(button: Button){
        button.isEnabled = !button.isEnabled
        button.isClickable = !button.isClickable
    }
}