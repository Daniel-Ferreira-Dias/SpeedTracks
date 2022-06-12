package com.example.bookapplicationv1.classes

class ModelComment {
    var id = ""
    var modelId = ""
    var timestamp = ""
    var comment = ""
    var uid = ""
    var rating = ""
    var userPhoto = ""

    constructor()

    constructor(id: String, modelId: String, timestamp: String, comment: String, uid: String, rating: String) {
        this.id = id
        this.modelId = modelId
        this.timestamp = timestamp
        this.comment = comment
        this.uid = uid
        this.rating = rating
    }


}