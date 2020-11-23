package pt.isec.tp_amov.model

import pt.isec.tp_amov.objects.Product
import pt.isec.tp_amov.objects.ShoppingList

object Model{
    private val allProducts: ShoppingList = ShoppingList("allProducts")
    private val allLists: MutableList<ShoppingList> = ArrayList()

    fun addProduct(prod: Product, shopList: ShoppingList): Boolean{
        if(!allProducts.productExists(prod)){
            allProducts.addProduct(prod)
        }
        if(allLists.contains(shopList)){
            shopList.addProduct(prod)
            return true
        }
        return false
    }

    fun addList(shopList: ShoppingList) : Boolean{
        return allLists.add(shopList)
    }
}