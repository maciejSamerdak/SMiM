package com.example.maciej.smim

import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class HotseatGameInstrumentedTest {

    @get:Rule var activityTestRule = ActivityTestRule(HotseatGameActivity::class.java)

    @Test fun testMarkPlacementConfirmation() {
        val board = activityTestRule.activity.findViewById<GridLayout>(R.id.board)

        assertFalse(activityTestRule.activity.findViewById<Button>(R.id.confirm).isEnabled)

        onView(
            allOf(
                withTagValue(`is`(0 as Any)),
                isDisplayed()
            )
        ).perform(click())

        assertTrue(activityTestRule.activity.findViewById<Button>(R.id.confirm).isEnabled)

        onView(withId(R.id.confirm)).perform(click())

        assertEquals("O", (board.getChildAt(0) as Button).text)
        assertFalse(activityTestRule.activity.findViewById<Button>(R.id.confirm).isEnabled)
    }

    @Test fun testMarkPlacementOnOccupiedField() {
        val board = activityTestRule.activity.findViewById<GridLayout>(R.id.board)

        makeMove(0)

        assertEquals("O", (board.getChildAt(0) as Button).text)

        onView(
            allOf(
                withTagValue(`is`(0 as Any)),
                isDisplayed()
            )
        ).perform(click())

        assertFalse(activityTestRule.activity.findViewById<Button>(R.id.confirm).isEnabled)
        assertEquals("O", (board.getChildAt(0) as Button).text)
    }

    @Test fun testDraw() {
        for (i in 0..9) {
            if (i.rem(3) == 0)
                for (j in 9 downTo 0)
                    makeMove(i * 10 + j)
            else
                for(j in 0..9)
                    makeMove(i * 10 + j)
        }
        assertBoardIsEmpty()
        assertScoreIsEqual("0","0")
    }

    @Test fun testWinInRow() {
        for(game in 0..1) {
            for (i in 0..8) {
                var index = 20 + i / 2
                if (i.rem(2) == 1)
                    index -= 10
                makeMove(index)
            }
            assertBoardIsEmpty()
        }
        assertScoreIsEqual("1", "1")
    }

    @Test fun testWinInColumn() {
        for (game in 0..1) {
            for (i in 0..8) {
                var index = 4 + i / 2 * 10
                if (i.rem(2) == 1)
                    index += 1
                makeMove(index)
            }
            assertBoardIsEmpty()
        }
        assertScoreIsEqual("1","1")
    }

    @Test fun testWinDiagonally() {
        for (game in 0..1) {
            for (i in 0..8) {
                var index = i / 2 * 10 + i / 2
                if (i.rem(2) == 1)
                    index += 1
                makeMove(index)
            }
            assertBoardIsEmpty()
        }
        assertScoreIsEqual("1", "1")
    }

    @Test fun testWinWithFullBoard() {
        fun Int.toBoolean() = this != 0


        for (i in 0..4)
            for (j in 0..4)
                if (!(i.rem(2).toBoolean().xor(j.rem(2).toBoolean())))
                {
                    makeMove(i * 10 + j * 2)
                    makeMove(i * 10 + j * 2 + 1)
                }
                else
                {
                    makeMove(i * 10 + j * 2 + 1)
                    makeMove(i * 10 + j * 2)
                }
        for (i in 5..6)
            for (j in 0..9)
                if (j < 6){
                    val x = j / 2
                    if (i.rem(2).toBoolean().xor(x.rem(2).toBoolean()))
                        makeMove(i * 10 + j)
                    else
                        if (j == x * 2)
                            makeMove(i * 10 + j + 1)
                        else
                            makeMove(i * 10 + j - 1)
                }
                else
                    if (i.rem(2) != 0)
                        makeMove(i * 10 + j)
                    else
                        if(j.rem(2) == 0)
                            makeMove(i * 10 + j + 1)
                        else
                            makeMove(i * 10 + j - 1)

        for(i in 0..2)
            if(i.rem(2) == 0) {
                makeMove(70 + i * 2)
                makeMove(70 + i * 2 + 1)
            }
            else {
                makeMove(70 + i * 2 + 1)
                makeMove(70 + i * 2)
            }
        makeMove(79)
        makeMove(76)
        makeMove(78)
        makeMove(77)

        for (i in 8..9)
            for (j in 0..4)
                if (i.rem(2).toBoolean().xor(j.rem(2).toBoolean())) {
                    makeMove(i * 10 + j * 2)
                    makeMove(i * 10 + j * 2 + 1)
                } else {
                    makeMove(i * 10 + j * 2 + 1)
                    makeMove(i * 10 + j * 2)
                }
        assertBoardIsEmpty()
        assertScoreIsEqual("1", "0")
    }

    private fun makeMove(index: Int) {
        onView(
            allOf(
                withTagValue(`is`(index as Any)),
                isDisplayed()
            )
        ).perform(click())
        onView(withId(R.id.confirm)).perform(click())
    }

    private fun assertBoardIsEmpty() {
        val board = activityTestRule.activity.findViewById<GridLayout>(R.id.board)
        for(i in 0..99) {
            assertEquals("", (board.getChildAt(i) as Button).text)
        }
    }

    private fun assertScoreIsEqual(player_1: String, player_2: String) {
        val player_1_score = activityTestRule.activity.findViewById<TextView>(R.id.player_1_score)
        val player_2_score = activityTestRule.activity.findViewById<TextView>(R.id.player_2_score)
        assertEquals(player_1, player_1_score.text)
        assertEquals(player_2, player_2_score.text)
    }

}