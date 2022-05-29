package com.cme.speedtrackers.model

import com.cme.speedtrackers.classes.InfoShoe
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Shoes(
    var Brand_Nome: String = "",
    var Model_Nome: String = "",
    var Color_Nome: String = "",
    var Brand_ID: String = "",
    var Model_ID: String = "",
    var Shoe_ID: Long = 0,
    var Shoe_TimeStamp: Long = 0,
    var Shoe_User_UID: String = "",
    var KmTraveled: Double? = 0.0,
    var FirstUsage : String?= getCurrentDate(),
    var EquipamentoAtivo: Boolean? = true,
    var ImageURL: String = "",
    var listaShoeInfo: ArrayList<InfoShoe> = ArrayList()
)
{
    fun adicionarInfoProduto(listaShoe: InfoShoe) {
        listaShoeInfo.add(listaShoe)
}
}
private fun getCurrentDate(): String{
    val sdf = SimpleDateFormat("dd-mm-yyyy")
    val currentDate = sdf.format(Date())
    return currentDate
}