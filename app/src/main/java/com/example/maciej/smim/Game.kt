package com.example.maciej.smim

import android.graphics.Color

open class Game(numberOfRows: Int, numberOfColumns: Int) {
    val scoreCount: IntArray = intArrayOf(0, 0)
    var isPlayerOneTurn: Boolean = false
    var playerMarks: Array<PlayerMark> = arrayOf(PlayerMark("X", Color.RED), PlayerMark("O", Color.BLUE))
    var fields : Array<Array<String>> = Array(numberOfRows) {Array(numberOfColumns) {""} }

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

    fun checkFields(numberOfButton: Int, toWin: Int = 5): Boolean{
        val numberOfRows: Int = fields.size
        val numberOfColumns: Int = fields[0].size
        val columnNumber: Int = numberOfButton % numberOfRows
        val rowNumber: Int = (numberOfButton - columnNumber) / numberOfColumns
        val columnNumberRight: Int = numberOfColumns - columnNumber - 1
        val marksToWin: Int = toWin
        var currentRow: Int
        var currentColumn: Int

        //horizontally
        var counter: Int = 0
        val mark = getPlayerMark().symbol

        for(i in 0 until numberOfColumns){
            if(fields[rowNumber][i] == mark) counter += 1
            else counter = 0

            if(counter == marksToWin) return true
        }

        //vertically
        counter = 0
        for(i in 0 until numberOfRows){
            if(fields[i][columnNumber] == mark) counter += 1
            else counter = 0

            if(counter == marksToWin) return true
        }

        //diagonally
        counter = 0
        currentRow = if(rowNumber > columnNumber) (rowNumber - columnNumber) else 0
        currentColumn = if(rowNumber > columnNumber) 0 else (columnNumber - rowNumber)
        while(currentRow < numberOfRows && currentColumn < numberOfColumns){
            if(fields[currentRow][currentColumn] == mark) counter += 1
            else counter = 0
            currentRow += 1
            currentColumn += 1
            if(counter == marksToWin) return true
        }

        counter = 0
        currentRow = if(rowNumber > columnNumberRight) rowNumber - columnNumberRight else 0
        currentColumn = if(rowNumber > columnNumberRight) numberOfColumns - 1 else columnNumber + rowNumber
        while(currentRow < numberOfRows && currentColumn >= 0){
            if(fields[currentRow][currentColumn] == mark) counter +=1
            else counter = 0
            currentRow += 1
            currentColumn -= 1
            if(counter == marksToWin) return true
        }

        return false
    }

    inner class PlayerMark(val symbol: String, val color: Int)
}
