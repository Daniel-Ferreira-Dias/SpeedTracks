package com.cme.speedtrackers.model

import com.cme.speedtrackers.classes.InfoShoe

class Shoes(
    var Brand_Nome: String = "",
    var Model_Nome: String = "",
    var Color_Nome: String = "",
    var Brand_ID: Long = 0,
    var Model_ID: Long = 0,
    var Shoe_ID: Long = 0,
    var Shoe_TimeStamp: Long = 0,
    var Shoe_User_UID: String = "",
    var listaShoeInfo: ArrayList<InfoShoe> = ArrayList()
) {
    fun adicionarInfoProduto(listaShoe: InfoShoe) {
        listaShoeInfo.add(listaShoe)
    }
}