package com.example.maciej.smim

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.maciej.smim.login_register.LoginActivity
import com.example.maciej.smim.users.User
import com.example.maciej.smim.users.UserService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class LoginInstrumentedTest {
    private lateinit var db: FirebaseDatabase
    private lateinit var userService: UserService
    private lateinit var auth: FirebaseAuth

    private val testName = "test_user"
    private val testPassword = "password"
    private val testEmail = "test@user.com"

    @get:Rule var activityTestRule = ActivityTestRule(LoginActivity::class.java)

    @Before
    fun setUp() {
        db = FirebaseDatabase.getInstance()
        userService = UserService()
        auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            val name = auth.currentUser!!.displayName
            if (name != null && name.contains("test_")) {
                auth.currentUser!!.delete()
            }
            else
                auth.signOut()
        }

        val testUser = userService.findUserByName(testName)
        if (testUser != null) {
            auth.signInWithEmailAndPassword(testUser.email!!, testPassword).addOnCompleteListener {
                auth.currentUser!!.delete()
            }
        }
    }

    @After
    fun tearDown() {
        if(auth.currentUser != null)
            auth.currentUser!!.delete()
        db.getReference("users").child(testName).removeValue()
    }

    @Test fun testLogInSuccess() {
        auth.createUserWithEmailAndPassword(testEmail, testPassword)
        val player = User(Int.MAX_VALUE.toString(), testEmail, testName)
        userService.addUserToDB(player)

        onView(withId(R.id.email)).perform(typeText(testEmail))
        onView(withId(R.id.password)).perform(typeText(testPassword))
        onView(withId(R.id.btn_login)).perform(click())
        Thread.sleep(3000)

        assertEquals(auth.currentUser!!.email, testEmail)
    }

    @Test fun testLogInFail() {
        onView(withId(R.id.email)).perform(typeText(testEmail))
        onView(withId(R.id.password)).perform(typeText(testPassword))
        onView(withId(R.id.btn_login)).perform(click())
        Thread.sleep(3000)

        assertNull(auth.currentUser)
    }

    @Test fun testSignUp() {
        onView(withId(R.id.btn_signup)).perform(click())
        onView(withId(R.id.email)).perform(typeText(testEmail))
        onView(withId(R.id.friendsName)).perform(typeText(testName))
        onView(withId(R.id.password)).perform(typeText(testPassword))
        onView(withId(R.id.sign_up_button)).perform(click())
        Thread.sleep(3000)

        val db_user = userService.findUserByName(testName)
        assertNotNull(db_user)
        assertEquals(db_user!!.email, auth.currentUser!!.email)
        auth.currentUser!!.delete()
    }
}