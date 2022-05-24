package com.cme.speedtrackers.model

import android.media.Image
import android.widget.ImageView

class Marcas {

    var ID:Long = 0
    var Nome: String = ""
    var Imagem: String = ""

    constructor()

    constructor(
        ID: Long
        , Imagem: String
        , Nome: String
    ) {
        this.ID = ID
        this.Imagem = Imagem
        this.Nome = Nome
    }
}