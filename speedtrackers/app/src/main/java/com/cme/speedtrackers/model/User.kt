package com.example.bookapplicationv1.classes

class User {
    var userName: String? = null
    var userEmail: String? = null
    var userType: String? = null
    var userTimestamp: Long? = null
    var userVerified: Boolean? = null
    var userUID : String?= null
    var userProfilePic: String?= null

    constructor(){}

    constructor(userName: String?, userEmail: String?, userType: String?, userTimestamp: Long?, userVerified: Boolean?, userUID: String?, userProfilePic: String?){
        this.userName = userName
        this.userEmail = userEmail
        this.userType = userType
        this.userTimestamp = userTimestamp
        this.userVerified = userVerified
        this.userUID = userUID
        this.userProfilePic = userProfilePic
    }
}