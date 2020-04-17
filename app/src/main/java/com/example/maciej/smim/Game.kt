package com.example.maciej.smim

import android.graphics.Color

open class Game(numberOfRows: Int, numberOfColumns: Int) {
    val scoreCount: IntArray = intArrayOf(0, 0)
    var isPlayerOneTurn: Boolean = false
    var playerMarks: Array<PlayerMark> = arrayOf(PlayerMark("X", Color.RED), PlayerMark("O", Color.BLUE))
    var fields : Array<Array<String>> = Array(numberOfRows,{Array(numberOfColumns,{""})})

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
        for (i in fields.indices) {
            for (j in fields[i].indices) {
                if(fields[i][j] == ""){
                    return false
                }
            }
        }
        return true
    }

    fun resetBoard(){
        for (i in fields.indices) {
            for (j in fields[i].indices) {
                fields[i][j]=""
            }
        }
    }

    fun refreshFields(fieldsFromBoard: Array<Array<String>>){
        fields = fieldsFromBoard
    }

    inner class PlayerMark(val symbol: String, val color: Int)
}
