package com.cme.speedtrackers.model

import android.media.Image
import android.widget.ImageView

class Marcas {

    var marcaId : Int = 0
    var marcaName : String = ""
    var marcaImage : String = ""

    constructor(){}

    constructor(marcaId: Int, marcaName: String, marcaImage: String){
        this.marcaId = marcaId
        this.marcaName = marcaName
        this.marcaImage = marcaImage
    }
}