package com.cme.speedtrackers.model

data class UserEquipment (
    var ID: Long? = null,
    var MarcaID: Long? = null,
    var ModeloID: Long? = null,
    var modeloNome: String? = null,
    var CorID: Long? = null,
    var equipamentoAtivo: Boolean? = null,
    var UserUID : String?= null,
    var kmTraveled: Float? = null,
    var firstUsage : String?= null
)