package com.cme.speedtrackers.classes

import java.text.SimpleDateFormat
import java.util.*

data class InfoShoe(
    var Brand_ID: String? = null,
    var Brand_Name: String? = null,
    var Model_ID: Long? = 0,
    var Model_Name: String? = null,
    var Color_ID: Long? = 0,
    var Shoe_TimeStamp: Long? = 0,
    var Shoe_User_UID: Long?=0,
    var Color_Nome: String?= null,
    var Shoe_ID: Long?=0,
    var KmTraveled: Double? = 0.0,
    var FirstUsage : String?= getCurrentDate(),
    var EquipamentoAtivo: Boolean? = true
)
private fun getCurrentDate(): String{
    val sdf = SimpleDateFormat("dd-mm-yyyy")
    val currentDate = sdf.format(Date())
    return currentDate
}