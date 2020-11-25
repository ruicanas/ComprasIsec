package pt.isec.tp_amov.model

import pt.isec.tp_amov.objects.Categories
import pt.isec.tp_amov.objects.Product
import pt.isec.tp_amov.objects.ShoppingList
import pt.isec.tp_amov.objects.UnitsMeasure

object Model{
    private val allProducts: ShoppingList = ShoppingList("allProducts", 0)
    private val allLists: MutableList<ShoppingList> = ArrayList()
    private var idList = 0
    private val idListCounter: Int
        get(){
            ++idList
            return idList
        }
    private var idProducts = 0
    private val idProductsCounter: Int
        get(){
            ++idProducts
            return idProducts
        }


    private fun searchForList(id: Int) : ShoppingList?{
        for(list in allLists){
            if(list.id == id){
                return list
            }
        }
        return null
    }

    fun getListById(id: Int) : ShoppingList?{
        return searchForList(id)
    }

    fun getIdByName(listName: String) : Int{
        for(list in allLists){
            if(list.name == listName){
                return list.id
            }
        }
        return -1
    }

    fun getAllLists() : MutableList<ShoppingList>{
        return allLists
    }

    fun addProduct(name: String, brand: String, price: Double, amount: Double,
                   unit: UnitsMeasure, category: Categories, notes: String, img: String?, listId: Int): Boolean{
        val prod = Product( idProductsCounter, name, brand, price, amount, unit, category, notes, img)
        if(!allProducts.productExists(prod)){
            allProducts.addProduct(prod)
        }
        val shopList = searchForList(listId) ?: return false
        shopList.addProduct(prod)
        return true
    }

    fun addList(name: String) : Int{
        val shopList = ShoppingList(name, idListCounter)
        allLists.add(shopList)
        return idList
    }

    fun debugAllListsAsString() : String{
        return allLists.toString()
    }

    fun debugAllProductsAsString() : String{
        return allProducts.toString()
    }
}