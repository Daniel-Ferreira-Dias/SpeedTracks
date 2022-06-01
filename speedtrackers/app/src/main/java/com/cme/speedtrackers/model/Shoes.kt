package com.cme.speedtrackers.model

import com.cme.speedtrackers.classes.InfoShoe
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/*data class Shoes (
    var  Brand_Nome: String = "",
    var Model_Nome: String = "",
    var Shoe_Nome: String = "",
    var Color_Nome: String = "",
    var Brand_ID: String = "",
    var Model_ID: String = "",
    var Shoe_ID: Long = 0,
    var Shoe_TimeStamp: Long = 0,
    var Shoe_User_UID: String = "",
    var KmTraveled: Double? = 0.0,
    var FirstUsage : String?= "",
    var EquipamentoAtivo: Boolean? = true,
    var ImageURL: String = "",
    var ColorURL: String = "",
    var Shoe_Size: Int = 0,
    var listaShoeInfo: ArrayList<InfoShoe> = ArrayList()
){
    fun adicionarInfoProduto(listaShoe: InfoShoe) {
        listaShoeInfo.add(listaShoe)
    }
}*/

class Shoes : Serializable {
    var  Brand_Nome: String = ""
    var Model_Nome: String = ""
    var Shoe_Nome: String = ""
    var Color_Nome: String = ""
    var Brand_ID: String = ""
    var Model_ID: String = ""
    var Shoe_ID: Long = 0
    var Shoe_TimeStamp: Long = 0
    var Shoe_User_UID: String = ""
    var KmTraveled: Double? = 0.0
    var FirstUsage : String?= ""
    var EquipamentoAtivo: Boolean? = true
    var ImageURL: String = ""
    var ColorURL: String = ""
    var Shoe_Size: Int = 0
    var listaShoeInfo: ArrayList<InfoShoe> = ArrayList()
}




