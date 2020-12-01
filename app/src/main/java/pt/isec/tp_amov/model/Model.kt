package pt.isec.tp_amov.model

import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import pt.isec.tp_amov.objects.*

object Model{
    private val archivedLists: MutableList<ShoppingList> = ArrayList()
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
    private val maxArchivedLists = 10

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

    fun getOldLists(): MutableList<ShoppingList> {
        return archivedLists
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
        val duplicate = copyList(shoppingList)
        archiveList(duplicate)
        for(p in shoppingList.productList){
            removeDataBase(p.name, p.category)
        }
        allLists.remove(shoppingList)
    }

    private fun copyList(list: ShoppingList): ShoppingList {
        val duplicate = ShoppingList(list.name, list.id)
        for (prod in list.productList)
            duplicate.addProduct(prod)
        return duplicate
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

    fun addList(list: ShoppingList): Int {
        allLists.add(list)
        return list.id
    }

    private fun archiveList(shoppingList: ShoppingList) {
        archivedLists.add(shoppingList)
    }

    fun recreateList(id: Int) {
        for(list in archivedLists) {
            if (list.id == id) {
                addList(recreateProducts(list))
                removeListFromArchive(list)
            }
        }
    }

    private fun removeListFromArchive(list: ShoppingList) {
        archivedLists.remove(list)
    }

    private fun recreateProducts(oldList: ShoppingList): ShoppingList {
        var list = ShoppingList(oldList.name, oldList.id)

        if (oldList.getNumberOfProducts() == 0) {
            Log.i("Recreate Prods", "There are no products")
        }

        for (prod in oldList.productList) {
            list.addProduct(Product(prod.id, prod.name, prod.brand, prod.price, prod.amount, prod.units, prod.category, prod.notes, prod.image))
        }

        return list
    }

    fun addPhoto(bitmap: Bitmap, name: String) {}

    fun debugAllListsAsString() : String{
        return allLists.toString()
    }

    fun debugAllProductsAsString() : String{
        return allProducts.toString()
    }
}