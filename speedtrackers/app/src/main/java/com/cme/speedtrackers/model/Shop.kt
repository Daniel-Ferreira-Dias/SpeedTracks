package com.cme.speedtrackers.model

class Shop {

    var ID_Modelo: Long = 0
    var Nome_Modelo: String = ""
    var Imagem_Modelo: String = ""
    var ID_Marca: Long = 0
    var Nome_Marca: String = ""
    var Imagem_Marca: String = ""
    var Preço: Long = 0
    var Stock: Long = 0
    var Descrição: String = ""
    var Total: Long = 0

    constructor()

    constructor(
        ID_Modelo: Long, Imagem_Modelo: String, Nome_Modelo: String,
        ID_Marca: Long, Nome_Marca: String, Imagem_Marca: String,
        Preço: Long, Stock: Long, Descrição: String,Total : Long,
    ) {
        this.ID_Modelo = ID_Modelo
        this.Imagem_Modelo = Imagem_Modelo
        this.Nome_Modelo = Nome_Modelo
        this.ID_Marca = ID_Marca
        this.Nome_Marca = Nome_Marca
        this.Imagem_Marca = Imagem_Marca
        this.Preço = Preço
        this.Stock = Stock
        this.Descrição = Descrição
        this.Total = Total
    }
}