package com.cme.speedtrackers.classes

import android.app.Application
import com.cme.speedtrackers.dialogs.BottomSheetShoesFragment
import com.cme.speedtrackers.model.Shoes

class GlobalClass : Application() {
    companion object{
        var Brand_ID =""
        var Brand_Name = ""
        var Model_ID =""
        var Model_Name = ""
        var Color_ID = ""
        var ImageURL = ""
        var currentProduto = Shoes()
        var activityShoe = Shoes()
        var currentDialog = BottomSheetShoesFragment()
        var shoe_Imagem = ""
    }
}