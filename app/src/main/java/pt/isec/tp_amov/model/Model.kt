package pt.isec.tp_amov.model

import android.graphics.Bitmap
import pt.isec.tp_amov.objects.*

object Model{
    val allProducts: MutableList<DataProduct> = ArrayList()
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

    fun receiveProduct(name: String, brand: String, price: Double, amount: Double,
                       unit: UnitsMeasure, category: Categories, notes: String, img: Bitmap?, listId: Int): Boolean{
        val prod = Product( idProductsCounter, name, brand, price, amount, unit, category, notes, img)
        val dataProd = DataProduct(name, category)
        if(!allProducts.contains(dataProd)){
            allProducts.add(dataProd)
        }
        else{
            incrementProdUsed(name, category)
        }
        val shopList = searchForList(listId) ?: return false
        shopList.addProduct(prod)
        return true
    }

    fun updateDataBase(oldName: String, oldCategory: Categories,
                       newName: String, newCategory: Categories){
        handleOldData(oldName, oldCategory)
        handleNewData(newName, newCategory)
    }

    fun removeDataBase(oldName: String, oldCategory: Categories){
        handleOldData(oldName, oldCategory)
    }

    fun removeListDataBase(shoppingList: ShoppingList) {
        for(p in shoppingList.productList){
            removeDataBase(p.name, p.category)
        }
        allLists.remove(shoppingList)
    }

    private fun incrementProdUsed(name: String, category: Categories) {
        for(dP in allProducts){
            if(dP.name == name && dP.category == category){
                dP.nTimesUsed++
                break
            }
        }
    }

    private fun handleNewData(newName: String, newCategory: Categories) {
        val dataProd = DataProduct(newName, newCategory)
        if(!allProducts.contains(dataProd)){
            allProducts.add(dataProd)
        }else{
            incrementProdUsed(dataProd.name, dataProd.category)
        }

    }

    private fun handleOldData(oldName: String, oldCategory: Categories){
        for(dP in allProducts){
            if(dP.name == oldName && dP.category == oldCategory){
                dP.nTimesUsed--
                if(dP.nTimesUsed == 0){
                    allProducts.remove(dP)
                }
                break
            }
        }
    }

    fun addList(name: String) : Int{
        val shopList = ShoppingList(name, idListCounter)
        allLists.add(shopList)
        return idList
    }

    fun addPhoto(bitmap: Bitmap, name: String) {
    }

    fun debugAllListsAsString() : String{
        return allLists.toString()
    }

    fun debugAllProductsAsString() : String{
        return allProducts.toString()
    }
}