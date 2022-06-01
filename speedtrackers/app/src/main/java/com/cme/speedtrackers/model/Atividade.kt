package com.cme.speedtrackers.model

class Atividade(
    var ID: Long = 0,
    var NomeAtividade: String = "",
    var DistanciaPercorrida: Double = 0.0,
    var TipoExercicio: String = "",
    var Duracao: Int = 0,
    var Data: String = "",
    var ImageURL: String = "",
    var Shoe_ID: Long = 0,
    var User_UID: String = "",
    var MapURL: String = randomMap()
)

var mapsURLs = ArrayList<String>()
private fun randomMap(): String {
    var mapsURLs = ArrayList<String>()
    mapsURLs.add("https://firebasestorage.googleapis.com/v0/b/speedtrackersupdated.appspot.com/o/mapas%2Fmapa4.PNG?alt=media&token=6d243598-a5c0-456a-b172-268530bf6e0e")
    mapsURLs.add("https://firebasestorage.googleapis.com/v0/b/speedtrackersupdated.appspot.com/o/mapas%2Fmapa3.PNG?alt=media&token=cd3d0c7c-96ef-48d6-97b2-28f78e98d56d")
    mapsURLs.add("https://firebasestorage.googleapis.com/v0/b/speedtrackersupdated.appspot.com/o/mapas%2Fmapa2.PNG?alt=media&token=fb6d948b-b003-46ed-a199-8deba9beb0b7")
    mapsURLs.add("https://firebasestorage.googleapis.com/v0/b/speedtrackersupdated.appspot.com/o/mapas%2Fmapa1.png?alt=media&token=b32fde08-bf1f-4fc2-af10-f91557c83149")
    mapsURLs.add("https://firebasestorage.googleapis.com/v0/b/speedtrackersupdated.appspot.com/o/mapas%2Fmapa.png?alt=media&token=4ba18fce-f392-411f-9ff8-9763db83420e")
    var int: Int = (0..4).random()
    return mapsURLs[int]
}

