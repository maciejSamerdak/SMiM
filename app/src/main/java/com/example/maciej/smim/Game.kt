package com.example.maciej.smim

import android.graphics.Color
import android.widget.Button
import android.widget.GridLayout

open class Game(private val board: GridLayout) {
    val scoreCount: IntArray = intArrayOf(0, 0)
    var isPlayerOneTurn: Boolean = false
    var playerMarks: Array<PlayerMark> = arrayOf(PlayerMark("X", Color.RED), PlayerMark("O", Color.BLUE))

    fun updateScore(player: Int, score:Int=1) {
        scoreCount[player - 1] += score
    }

    open fun switchTurn() {
        isPlayerOneTurn = !isPlayerOneTurn
    }

    open fun getPlayerMark(): PlayerMark {
        return if (isPlayerOneTurn) playerMarks[0] else playerMarks[1]
    }

    fun isBoardFull(): Boolean {
        for ( x in 0 until board.childCount){
            val child = board.getChildAt(x)
            if((child as Button).text.equals(""))
                return false
        }
        return true
    }

    fun resetBoard(){
        for ( x in 0 until board.childCount){
            val child = board.getChildAt(x)
            (child as Button).text = ""
        }
    }

    inner class PlayerMark(val symbol: String, val color: Int)
}
