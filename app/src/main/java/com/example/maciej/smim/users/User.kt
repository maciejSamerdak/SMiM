package com.example.maciej.smim.users

class User {
    var uid: String? = null
    var email: String? = null
    var name: String? = null
    var wins: Long = 0
    var draws: Long = 0
    var loses: Long = 0

    constructor() {}
    constructor(uid: String?, email: String?, name: String?) {
        this.uid = uid
        this.email = email
        this.name = name
    }
}