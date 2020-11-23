package pt.isec.tp_amov.model

import pt.isec.tp_amov.objects.Product
import pt.isec.tp_amov.objects.ShoppingList

object Model{
    private val allProducts: ShoppingList = ShoppingList("allProducts")
    private val allLists: MutableList<ShoppingList> = ArrayList()

    private fun searchForList(name: String) : ShoppingList?{
        for(list in allLists){
            if(list.name == name){
                return list
            }
        }
        return null
    }

    fun getList(name: String) : ShoppingList?{
        return searchForList(name)
    }

    fun getAllLists() : MutableList<ShoppingList>{
        return allLists
    }

    fun addProduct(prod: Product, listName: String): Boolean{
        if(!allProducts.productExists(prod)){
            allProducts.addProduct(prod)
        }
        val shopList = searchForList(listName) ?: return false
        shopList.addProduct(prod)
        return true
    }

    fun addList(name: String) : Boolean{
        val shopList = ShoppingList(name)
        return allLists.add(shopList)
    }

    fun debugAllListsAsString() : String{
        return allLists.toString()
    }

    fun debugAllProductsAsString() : String{
        return allProducts.toString()
    }
}