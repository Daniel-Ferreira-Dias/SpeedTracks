package com.cme.speedtrackers.model

class Cores {

    var ID_Cor:Long = 0
    var Nome_Cor: String = ""
    var Imagem_Cor: String = ""

    constructor()

    constructor(
        ID: Long
        , Imagem_Cor: String
        , Nome_Cor: String
    ) {
        this.ID_Cor = ID
        this.Imagem_Cor = Imagem_Cor
        this.Nome_Cor = Nome_Cor
    }
}