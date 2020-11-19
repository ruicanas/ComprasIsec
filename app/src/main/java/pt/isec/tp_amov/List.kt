package pt.isec.tp_amov

import android.util.Log
import java.util.ArrayList

data class List (var name:String) {
    var productList : ArrayList<Product> = ArrayList()

    fun addProduct(prod: Product){
        productList.add(prod)
        Log.i("AddProduct","One product as been added")
    }

    fun removeProduct(ID: Int ){
        for (i in productList){
            if(i.id == ID) {
                productList.remove(i)
                Log.i("RemoveProduct","One product as been removed")
            }
        }
        Log.i("RemoveProduct","Failed trying to remove product")
    }
}