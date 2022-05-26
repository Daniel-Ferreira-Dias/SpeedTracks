package com.cme.speedtrackers.model

class Modelos {

    var ID_Modelo:Long = 0
    var Nome_Modelo: String = ""
    var Imagem_Modelo: String = ""

    constructor()

    constructor(
        ID_Modelo: Long
        , Imagem_Modelo: String
        , Nome_Modelo: String
    ) {
        this.ID_Modelo = ID_Modelo
        this.Imagem_Modelo = Imagem_Modelo
        this.Nome_Modelo = Nome_Modelo
    }
}