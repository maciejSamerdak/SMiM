package com.example.maciej.smim

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.example.maciej.smim.users.UserService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*


class MenuActivity : AppCompatActivity() {
    private var auth = FirebaseAuth.getInstance()
    private var userService = UserService()
    var db = FirebaseDatabase.getInstance()
    var currentUser: FirebaseUser? = auth.currentUser
    val currentUserName = currentUser?.displayName
    private lateinit var invitation: LinearLayout
    private var acceptInvitation: ImageButton? = null
    private  var cancelInvitation:ImageButton? = null
    private var invitingPerson: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()

        val newHotseatGameButton = findViewById<View>(R.id.hotseatButton) as Button
        val newMultiplayerGameButton = findViewById<View>(R.id.multiplayerButton) as Button
        val friendName = findViewById<View>(R.id.friendsName) as EditText
        val scoreBoardButton = findViewById<View>(R.id.scoreboardButton) as Button
        val quitButton = findViewById<View>(R.id.quitButton) as Button
        val logoutButton = findViewById<View>(R.id.logoutButton) as Button

        val intent =  Intent(this, HotseatGameActivity::class.java)

        newMultiplayerGameButton.setOnClickListener {
            val friendsName = friendName.text.toString().trim { it <= ' ' }
            if(TextUtils.isEmpty(friendsName)){
                Toast.makeText(applicationContext,
                    "You have to write name of user you want to play with",
                    Toast.LENGTH_LONG).show()
            }
            else if(userService.findUserByName(friendsName) != null){

                Toast.makeText(applicationContext, "Invitation sent.", Toast.LENGTH_SHORT).show()
                db.getReference("invitations").child(friendsName)
                    .child("from whom").setValue(currentUserName)

            }
            else
                Toast.makeText(applicationContext, "There is no user with such name.", Toast.LENGTH_SHORT).show()
            checkResponse(friendsName)
        }

        newHotseatGameButton.setOnClickListener { startActivity(intent)}

        scoreBoardButton.setOnClickListener {
            val intent = Intent(this@MenuActivity, ScoreboardActivity::class.java)
            startActivity(intent)
        }

        logoutButton.setOnClickListener {
            auth.signOut()
        }

        acceptInvitation!!.setOnClickListener {
            invitation.visibility = View.GONE
            db.getReference("responses").child(invitingPerson!!.text.toString())
                .child("response").setValue(true)
            db.getReference("responses").child(
                invitingPerson!!.text
                    .toString()
            ).child("from whom").setValue(currentUserName)
            val intent = Intent(
                this@MenuActivity,
                MultiplayerGameActivity::class.java
            )
            intent.putExtra("friend", invitingPerson!!.text.toString())
            intent.putExtra("playerNumber", 2)

            if (currentUserName != null) {
                db.getReference("invitations").child(currentUserName)
                    .removeValue()
            }
            startActivity(intent)
        }
        cancelInvitation!!.setOnClickListener {
            db.getReference("responses").child(invitingPerson!!.text.toString())
                .child("response").setValue(false)
            invitation.visibility = View.GONE
            db.getReference("responses").child(invitingPerson!!.text.toString())
                .child("from whom").setValue(currentUserName)
            if (currentUserName != null) {
                db.getReference("invitations").child(currentUserName).removeValue()
            }
        }

        listenerOfInvitations()

    }

    private fun checkResponse(friendsName: String){
        if (currentUserName != null) {
            db.getReference("responses")
                .child(currentUserName)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.child("response").value != null) {
                            if (dataSnapshot.child("response").value == true) {
                                val intent = Intent(applicationContext, MultiplayerGameActivity::class.java)
                                intent.putExtra("friend", friendsName)
                                intent.putExtra("playerNumber", 1)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                db.getReference("responses")
                                    .child(currentUserName).removeValue()
                                db.getReference("invitations").child(friendsName)
                                    .removeValue()
                                applicationContext.startActivity(intent)
                            } else {
                                db.getReference("invitations").child(friendsName)
                                    .removeValue()
                                Toast.makeText(applicationContext, "Invitation was rejected.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                db.getReference("responses")
                                    .child(currentUserName).removeValue()
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
        }
    }

    private fun listenerOfInvitations(){
        if (currentUserName != null) {
            db.getReference("invitations").child(currentUserName)
                .addChildEventListener(object : ChildEventListener {
                    override fun onChildAdded(dataSnapshot: DataSnapshot, @Nullable s: String?
                    ) {
                        invitingPerson!!.text = dataSnapshot.value.toString()
                        invitation.visibility = View.VISIBLE
                    }

                    override fun onChildChanged(dataSnapshot: DataSnapshot, @Nullable s: String?) {}

                    override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                        invitation.visibility = View.INVISIBLE
                    }

                    override fun onChildMoved(dataSnapshot: DataSnapshot, @Nullable s: String?) {}

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
        }
    }

    private fun initView(){
        setContentView(R.layout.activity_menu)
        invitingPerson  = findViewById(R.id.textView)
        acceptInvitation = findViewById(R.id.acceptInvitation)
        cancelInvitation = findViewById(R.id.cancelInvitation)
        invitation = findViewById(R.id.invitationView)
        invitation.visibility = View.INVISIBLE
    }
}
