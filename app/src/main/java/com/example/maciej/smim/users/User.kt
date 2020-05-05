package com.example.maciej.smim.users

import com.google.firebase.database.FirebaseDatabase

class User {
    var uid: String? = null
    var email: String? = null
    var name: String? = null

    constructor() {}
    constructor(uid: String?, email: String?, name: String?) {
        this.uid = uid
        this.email = email
        this.name = name
    }

    fun addUserToDB(){
        val db =  FirebaseDatabase.getInstance().getReference("users")
        if (name != null) {
            db.child(name!!).setValue(this)
        }
    }
}