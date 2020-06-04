package com.example.maciej.smim

import android.app.Activity
import android.view.View.VISIBLE
import android.widget.LinearLayout
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.core.internal.deps.guava.collect.Iterables
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import com.example.maciej.smim.login_register.LoginActivity
import com.example.maciej.smim.users.User
import com.example.maciej.smim.users.UserService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import junit.framework.TestCase.*

import org.junit.*


class MenuInstrumentedTest {
    private lateinit var db: FirebaseDatabase
    private lateinit var userService: UserService
    private lateinit var auth: FirebaseAuth

    private val testUserName = "test_user"
    private val testFriendName = "test_user_2"

    @get:Rule var activityTestRule = ActivityTestRule(MenuActivity::class.java)

    @Before
    fun setUp() {
        db = FirebaseDatabase.getInstance()
        userService = UserService()
        auth = FirebaseAuth.getInstance()
        Thread.sleep(1000)

        if (auth.currentUser != null) {
            val name = auth.currentUser!!.displayName
            if (name != null && name.contains("test_")) {
                auth.currentUser!!.delete()
            }
            else
                auth.signOut()
        }

        val testUser = userService.findUserByName(testUserName)
        if (testUser != null) {
            auth.signInWithEmailAndPassword(testUser.email!!, "password").addOnCompleteListener {
                auth.currentUser!!.delete()
            }
        }

        val email = "test@user.com"
        val password = "password"
        val username = testUserName
        auth.createUserWithEmailAndPassword(email, password)
        val user = User(Int.MAX_VALUE.toString(), email, username)
        userService.addUserToDB(user)
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(testUserName).build()
            auth.currentUser?.updateProfile(profileUpdates)
        }

        val other_user = User(Short.MAX_VALUE.toString(), "other@user.com", testFriendName)
        userService = UserService()
        userService.addUserToDB(other_user)
    }

    @After
    fun tearDown() {
        if(auth.currentUser != null)
            auth.currentUser!!.delete()
        else {
            val testUser = userService.findUserByName(testUserName)
            if (testUser != null) {
                auth.signInWithEmailAndPassword(testUser.email!!, "password").addOnCompleteListener {
                    auth.currentUser!!.delete()
                }
            }
        }
        db.getReference("users").child(testUserName).removeValue()
        db.getReference("users").child(testFriendName).removeValue()
        db.getReference("invitations").child(testUserName).removeValue()
        db.getReference("invitations").child(testFriendName).removeValue()
    }

    @Test fun testOpenScoreBoard() {
        onView(withId(R.id.scoreboardButton)).perform(click())
        Thread.sleep(1000)

        Assert.assertEquals(ScoreboardActivity::class.java, getCurrentActivity()!!::class.java)
    }

    @Test fun testSignOut() {
        Thread.sleep(2000)
        assertNotNull(auth.currentUser)

        onView(withId(R.id.logoutButton)).perform(click())
        Thread.sleep(1000)

        assertEquals(LoginActivity::class.java, getCurrentActivity()!!::class.java)
        assertNull(auth.currentUser)
    }

    @Test fun testQuit() {
        onView(withId(R.id.quitButton)).perform(click())
        Thread.sleep(1000)

        assertTrue(activityTestRule.activity.isDestroyed)
    }

    @Test fun testHotSeatGame() {
        onView(withId(R.id.hotseatButton)).perform(click())
        Thread.sleep(1000)

        assertEquals(HotseatGameActivity::class.java, getCurrentActivity()!!::class.java)
    }

    @Test fun testInvitationToOneself() {
        onView(withId(R.id.friendsName)).perform(ViewActions.typeText(testUserName))
        onView(withId(R.id.multiplayerButton)).perform(click())
        Thread.sleep(3000)
    }

    @Test fun testEmptyInvitation() {
        onView(withId(R.id.multiplayerButton)).perform(click())
        Thread.sleep(3000)
    }

    @Test fun testSuccessfulInvitation() {
        onView(withId(R.id.friendsName)).perform(ViewActions.typeText(testFriendName))
        onView(withId(R.id.multiplayerButton)).perform(click())
        Thread.sleep(3000)
    }

    @Test fun testInvitationReceived() {
        db.getReference("invitations").child(testUserName)
            .child("from whom").setValue(testFriendName)
        activityTestRule.finishActivity()
        Thread.sleep(3000)
        activityTestRule.launchActivity(null)
        Thread.sleep(1000)
        val invitation = activityTestRule.activity.findViewById<LinearLayout>(R.id.invitationView)
        assertEquals(invitation.visibility, VISIBLE)
    }

    @Throws(Throwable::class)
    fun getCurrentActivity(): Activity? {
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        val activity = arrayOfNulls<Activity>(1)
        InstrumentationRegistry.getInstrumentation().runOnMainSync(Runnable {
            val activities: Collection<Activity> =
                ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED)
            activity[0] = Iterables.getOnlyElement(activities)
        })
        return activity[0]
    }
}