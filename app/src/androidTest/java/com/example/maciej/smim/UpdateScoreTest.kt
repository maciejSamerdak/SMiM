package com.example.maciej.smim


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class UpdateScoreTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(HotseatGameActivity::class.java)

    @Test
    fun updateScoreTest() {
        val button = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.board),
                        childAtPosition(
                            withId(R.id.main_layout),
                            2
                        )
                    ),
                    11
                ),
                isDisplayed()
            )
        )
        button.perform(click())

        val appCompatButton3 = onView(
            allOf(
                withId(R.id.confirm), withText("Confirm move"),
                childAtPosition(
                    allOf(
                        withId(R.id.main_layout),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatButton3.perform(click())

        val button2 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.board),
                        childAtPosition(
                            withId(R.id.main_layout),
                            2
                        )
                    ),
                    12
                ),
                isDisplayed()
            )
        )
        button2.perform(click())

        val appCompatButton4 = onView(
            allOf(
                withId(R.id.confirm), withText("Confirm move"),
                childAtPosition(
                    allOf(
                        withId(R.id.main_layout),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatButton4.perform(click())

        val button3 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.board),
                        childAtPosition(
                            withId(R.id.main_layout),
                            2
                        )
                    ),
                    20
                ),
                isDisplayed()
            )
        )
        button3.perform(click())

        val appCompatButton5 = onView(
            allOf(
                withId(R.id.confirm), withText("Confirm move"),
                childAtPosition(
                    allOf(
                        withId(R.id.main_layout),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatButton5.perform(click())

        val button4 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.board),
                        childAtPosition(
                            withId(R.id.main_layout),
                            2
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        button4.perform(click())

        val appCompatButton6 = onView(
            allOf(
                withId(R.id.confirm), withText("Confirm move"),
                childAtPosition(
                    allOf(
                        withId(R.id.main_layout),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatButton6.perform(click())

        val button5 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.board),
                        childAtPosition(
                            withId(R.id.main_layout),
                            2
                        )
                    ),
                    21
                ),
                isDisplayed()
            )
        )
        button5.perform(click())

        val appCompatButton7 = onView(
            allOf(
                withId(R.id.confirm), withText("Confirm move"),
                childAtPosition(
                    allOf(
                        withId(R.id.main_layout),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatButton7.perform(click())

        val button6 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.board),
                        childAtPosition(
                            withId(R.id.main_layout),
                            2
                        )
                    ),
                    23
                ),
                isDisplayed()
            )
        )
        button6.perform(click())

        val appCompatButton8 = onView(
            allOf(
                withId(R.id.confirm), withText("Confirm move"),
                childAtPosition(
                    allOf(
                        withId(R.id.main_layout),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatButton8.perform(click())

        val button7 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.board),
                        childAtPosition(
                            withId(R.id.main_layout),
                            2
                        )
                    ),
                    30
                ),
                isDisplayed()
            )
        )
        button7.perform(click())

        val appCompatButton9 = onView(
            allOf(
                withId(R.id.confirm), withText("Confirm move"),
                childAtPosition(
                    allOf(
                        withId(R.id.main_layout),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatButton9.perform(click())

        val button8 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.board),
                        childAtPosition(
                            withId(R.id.main_layout),
                            2
                        )
                    ),
                    34
                ),
                isDisplayed()
            )
        )
        button8.perform(click())

        val appCompatButton10 = onView(
            allOf(
                withId(R.id.confirm), withText("Confirm move"),
                childAtPosition(
                    allOf(
                        withId(R.id.main_layout),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatButton10.perform(click())

        val button9 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.board),
                        childAtPosition(
                            withId(R.id.main_layout),
                            2
                        )
                    ),
                    40
                ),
                isDisplayed()
            )
        )
        button9.perform(click())

        val appCompatButton11 = onView(
            allOf(
                withId(R.id.confirm), withText("Confirm move"),
                childAtPosition(
                    allOf(
                        withId(R.id.main_layout),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatButton11.perform(click())

        val button10 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.board),
                        childAtPosition(
                            withId(R.id.main_layout),
                            2
                        )
                    ),
                    45
                ),
                isDisplayed()
            )
        )
        button10.perform(click())

        val appCompatButton12 = onView(
            allOf(
                withId(R.id.confirm), withText("Confirm move"),
                childAtPosition(
                    allOf(
                        withId(R.id.main_layout),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatButton12.perform(click())

        val textView = onView(
            allOf(
                withId(R.id.player_1_score), withText("1"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.main_layout),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("1")))

        val textView2 = onView(
            allOf(
                withId(R.id.player_2_score), withText("0"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.main_layout),
                        1
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("0")))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
