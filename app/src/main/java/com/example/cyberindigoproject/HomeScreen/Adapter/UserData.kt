package com.example.cyberindigoproject.HomeScreen.Adapter

class UserData {

    var id: Int = 0
    var email: String = ""
    var name: String = ""
    var avatar: String = ""

    constructor() {}

    constructor(id: Int, email: String, name: String, avatar: String) {
        this.id = id
        this.email = email
        this.name = name
        this.avatar = avatar
    }


}