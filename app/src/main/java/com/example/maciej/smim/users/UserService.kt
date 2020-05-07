package com.example.maciej.smim.users

import com.google.firebase.database.*


open class UserService {

    var users = ArrayList<User>()
    private val db =  FirebaseDatabase.getInstance().getReference("users")

    constructor(){
        getAllUsers()     //TODO dodac ladowanie danych
    }

    fun addUserToDB(user: User){

        if (user.name != null) {
            db.child(user.name!!).setValue(user)
        }
    }

    open fun getAllUsers() {
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val arrayList: ArrayList<User> = ArrayList()
                for (snap in dataSnapshot.children) {
                    println("(in getAllUsers) " + snap.value)
                    val user =
                        User(
                            snap.child("uid").value.toString(),
                            snap.child("email").value.toString(),
                            snap.child("name").value.toString()
                        )
                    arrayList.add(user)
                }
                users = arrayList
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun findUserByEmail(email: String): User?{
        for(user in users){
            if(user.email.equals(email))
                return user
        }
        return null
    }

    fun findUserByName(name: String): User?{
        for(user in users){
            if(user.name.equals(name))
                return user
        }
        return null
    }
}

