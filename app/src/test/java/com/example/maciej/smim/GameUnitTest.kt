package com.example.maciej.smim

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.Test

import junitparams.JUnitParamsRunner
import junitparams.Parameters

@RunWith(JUnitParamsRunner::class)
class GameUnitTest {
    private lateinit var game: Game

    @Before
    fun setUp() {
        this.game = Game(3,3)
    }

    @Test
    @Parameters(method = "test_updateScore_with_given_score_Parameters")
    fun test_updateScore_with_given_score(startingScore: IntArray, player: Int, score: Int) {
        game.scoreCount[0] = startingScore[0]
        game.scoreCount[1] = startingScore[1]
        game.updateScore(player, score)
        val expectedScore = startingScore.clone()
        expectedScore[player - 1] += score
        assertArrayEquals(expectedScore, game.scoreCount)
    }

    @Test
    @Parameters(method = "test_updateScore_default_incrementation_Parameters")
    fun test_updateScore_default_incrementation(player: Int) {
        val previousScore = game.scoreCount.clone()
        game.updateScore(player)
        val expectedScore = previousScore.clone()
        expectedScore[player - 1]++
        assertArrayEquals(expectedScore, game.scoreCount)
    }

    @Test
    @Parameters(method = "test_switchTurn_Parameters")
    fun test_switchTurn(isPlayerOneTurn: Boolean) {
        game.isPlayerOneTurn = isPlayerOneTurn
        game.switchTurn()
        assertEquals(!isPlayerOneTurn, game.isPlayerOneTurn)
    }

    @Test
    @Parameters(method = "test_getPlayerMark_Parameters")
    fun test_getPlayerMark(isPlayerOneTurn: Boolean, expectedPlayerMarkIndex: Int) {
        game.isPlayerOneTurn = isPlayerOneTurn
        val playerMark = game.getPlayerMark()
        assertEquals(game.playerMarks[expectedPlayerMarkIndex], playerMark)
    }

    @Test
    @Parameters(method = "test_isBoardFull_Parameters")
    fun test_isBoardFull(fields: Array<Array<String>>,expectedResult: Boolean){
        game.refreshFields(fields)
        assertEquals(expectedResult, game.isBoardFull())
    }

    @Test
    fun test_resetBoard(){
        game.refreshFields(arrayOf(arrayOf("x","o","o"),arrayOf("x","o","x"),arrayOf("o","x","o")))
        game.resetBoard()
        assertArrayEquals(arrayOf(arrayOf("","",""),arrayOf("","",""),arrayOf("","","")),game.fields)
    }

    @Test
    @Parameters(method = "test_checkFields_Parameters")
    fun test_checkFields(numberOfButton: Int, fields: Array<Array<String>>, expectedResult: Boolean){
        game.refreshFields(fields)
        assertEquals(game.checkFields(numberOfButton, -1,3), expectedResult)
    }

    @SuppressWarnings("unused")
    fun test_updateScore_with_given_score_Parameters() = arrayOf(
        arrayOf(intArrayOf(0, 0), 1, 1),
        arrayOf(intArrayOf(0, 0), 2, 2),
        arrayOf(intArrayOf(0, 0), 2, 10),
        arrayOf(intArrayOf(5, 5), 1, -5)
    )

    @SuppressWarnings("unused")
    fun test_updateScore_default_incrementation_Parameters() = arrayOf(arrayOf(1), arrayOf(2))

    @SuppressWarnings("unused")
    fun test_switchTurn_Parameters() = arrayOf(arrayOf(true), arrayOf(false))

    @SuppressWarnings("unused")
    fun test_getPlayerMark_Parameters() = arrayOf(arrayOf(true, 0), arrayOf(false, 1))

    @SuppressWarnings("unused")
    fun test_isBoardFull_Parameters() = arrayOf(
        arrayOf(arrayOf(arrayOf("","",""),arrayOf("","",""),arrayOf("","","")),false),
        arrayOf(arrayOf(arrayOf("x","o","o"),arrayOf("x","o","x"),arrayOf("o","x","o")),true),
        arrayOf(arrayOf(arrayOf("x","","o"),arrayOf("o","x","o"),arrayOf("x","o","")),false)
    )

    @SuppressWarnings("unused")
    fun test_checkFields_Parameters() = arrayOf(
        arrayOf(8, arrayOf(arrayOf("O", "X", ""), arrayOf("", "O", "X"), arrayOf("", "", "O")), true),
        arrayOf(6, arrayOf(arrayOf("", "X", "O"), arrayOf("", "O", "X"), arrayOf("O", "", "")), true),
        arrayOf(2, arrayOf(arrayOf("", "X", "O"), arrayOf("", "X", "O"), arrayOf("", "", "O")), true),
        arrayOf(7, arrayOf(arrayOf("", "O", "X"), arrayOf("", "O", "X"), arrayOf("", "O", "")), true),
        arrayOf(0, arrayOf(arrayOf("O", "X", "O"), arrayOf("O", "X", "X"), arrayOf("O", "O", "X")), true),
        arrayOf(4, arrayOf(arrayOf("O", "X", "X"), arrayOf("X", "O", "O"), arrayOf("X", "O", "X")), false)
    )
}
