package com.example.bookapplicationv1.classes

import java.io.Serializable

class User : Serializable {
    var userName: String? = null
    var userFirstName: String? = null
    var userLastName: String? = null
    var userEmail: String? = "TESTEEE"
    var userType: String? = null
    var userTimestamp: Long? = null
    var userVerified: Boolean? = null
    var userUID : String?= null
    var userProfilePic: String?= null
    var isAdmin: Boolean?= null

    constructor(){}

    constructor(userName: String?, userEmail: String?, userType: String?, userTimestamp: Long?, userVerified: Boolean?, userUID: String?, userProfilePic: String?, isAdmin: Boolean?){
        this.userName = userName
        this.userEmail = userEmail
        this.userType = userType
        this.userTimestamp = userTimestamp
        this.userVerified = userVerified
        this.userUID = userUID
        this.userProfilePic = userProfilePic
        if (isAdmin != null){
            this.isAdmin = isAdmin
        }
        else {
            this.isAdmin = false
        }
        this.userFirstName = null
        this.userLastName = null

    }
}
