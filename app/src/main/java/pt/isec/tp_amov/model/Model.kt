package pt.isec.tp_amov.model

import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import pt.isec.tp_amov.R
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
    lateinit var bitmap: Bitmap

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

    fun getProdById(prodID: Int, listID: Int): Product? {
        for (prod in getListById(listID)!!.productList) {
            if (prod.id == prodID)
                return prod
        }
        return null
    }

    /*fun getProdBitmap(id: Int): Bitmap {

    }*/

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

    fun setDefaultListName(id: Int, name: String) {
        for (i in allLists) {
            if (i.id == id)
                i.name = name
        }
    }

    fun receiveProduct(name: String, brand: String, price: Double, amount: Double,
                       unit: UnitsMeasure, category: Categories, notes: String, img: Bitmap?, listId: Int): Boolean{
        val prod = Product( idProductsCounter, name, brand, price, amount, unit, category, notes, img)
        val dataProd = DataProduct(name, category)
        if(!allProducts.contains(dataProd)){
            allProducts.add(dataProd)
            if(price > 0){
                addPriceData(name, category, price)
            }
        }
        else{
            incrementProdUsed(name, category)
            if(price > 0){
                addPriceData(name, category, price)
            }
        }
        val shopList = searchForList(listId) ?: return false
        shopList.addProduct(prod)
        return true
    }

    fun updateDataBase(oldName: String, oldCategory: Categories, oldPrice: Double,
                       newName: String, newCategory: Categories, newPrice: Double){
        handleOldData(oldName, oldCategory, oldPrice)
        handleNewData(newName, newCategory, newPrice)
    }

    fun removeDataBase(oldName: String, oldCategory: Categories, oldPrice: Double){
        handleOldData(oldName, oldCategory, oldPrice)
    }

    fun removeListDataBase(shoppingList: ShoppingList) {
        val duplicate = copyList(shoppingList)
        archiveList(duplicate)
        for(p in shoppingList.productList){
            removeDataBase(p.name, p.category, p.price)
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

    private fun addPriceData(name: String, category: Categories, price: Double){
        for(dP in allProducts){
            if(dP.name == name && dP.category == category){
                if(dP.lastPrices.size < 3){
                    dP.lastPrices.add(price)
                }
                else{
                    dP.lastPrices.removeAt(0)
                    dP.lastPrices.add(price)
                }
            }
        }
    }

    private fun removePriceData(name: String, category: Categories, price: Double){
        for(dP in allProducts){
            if(dP.name == name && dP.category == category){
                dP.lastPrices.remove(price)
            }
        }
    }

    fun getPrices(product: Product): String?{
        for(dP in allProducts){
            if(dP.name == product.name && dP.category == product.category){
                return dP.lastPrices.toString()
            }
        }
        return null
    }

    private fun handleNewData(newName: String, newCategory: Categories, newPrice: Double) {
        val dataProd = DataProduct(newName, newCategory)
        if(!allProducts.contains(dataProd)){
            allProducts.add(dataProd)
        }else{
            incrementProdUsed(dataProd.name, dataProd.category)
        }
        if(newPrice > 0){
            addPriceData(newName, newCategory, newPrice)
        }
    }

    private fun handleOldData(oldName: String, oldCategory: Categories, oldPrice: Double){
        for(dP in allProducts){
            if(dP.name == oldName && dP.category == oldCategory){
                dP.nTimesUsed--
                if(dP.nTimesUsed == 0){
                    allProducts.remove(dP)
                }
                dP.lastPrices.remove(oldPrice)
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

    fun removeImageFromProduct(prodID: Int, listID: Int) {
        val prod = getProdById(prodID, listID)
    }

    fun debugAllListsAsString() : String{
        return allLists.toString()
    }

    fun debugAllProductsAsString() : String{
        return allProducts.toString()
    }

    fun updateDataPrices(oldName: String, oldCategory: Categories, oldPrice: Double,
                         newName: String, newCategory: Categories, newPrice: Double){
        removePriceData(oldName, oldCategory, oldPrice)
        if(newPrice > 0){
            addPriceData(newName, newCategory, newPrice)
        }
    }
}