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
    var User_UID: String = ""
)