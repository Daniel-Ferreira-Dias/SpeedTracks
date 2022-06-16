package com.cme.speedtrackers.classes

import android.app.Application
import com.cme.speedtrackers.dialogs.BottomSheetShoesFragment
import com.cme.speedtrackers.model.Shoes
import com.example.bookapplicationv1.classes.User

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
        var New_Name = ""
        var currentUser = User()
        var isAdmin: Boolean?= null
    }
}