package com.example.maciej.smim

import android.graphics.Color

open class Game() {
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

    inner class PlayerMark(val symbol: String, val color: Int)
}
