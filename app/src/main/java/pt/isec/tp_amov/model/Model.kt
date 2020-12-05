package pt.isec.tp_amov.model

import android.graphics.Bitmap
import android.provider.ContactsContract
import android.util.Log
import pt.isec.tp_amov.objects.*
import pt.isec.tp_amov.utils.Configuration

object  Model{
    private val idListCounter: Int
        get(){
            ++idList
            return idList
        }
    private val idProductsCounter: Int
        get(){
            ++idProducts
            return idProducts
        }
    private var idList = 0
    private var idProducts = 0
    val archivedLists: MutableList<ShoppingList> = ArrayList()
    val allLists: MutableList<ShoppingList> = ArrayList()
    val allProducts: MutableList<DataProduct> = ArrayList()
    val config = Configuration()

    private fun searchForList(id: Int) : ShoppingList?{
        for(list in allLists){
            if(list.id == id){
                return list
            }
        }
        return null
    }

    //Get something by ID's
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

    fun setDefaultListName(id: Int, name: String) {
        for (i in allLists) {
            if (i.id == id)
                i.name = name
        }
    }
    fun receiveProduct(name: String,
                       brand: String,
                       price: Double,
                       amount: Double,
                       unit: String,
                       category: String,
                       notes: String,
                       img: Bitmap?,
                       listId: Int): Boolean {
        val prod = Product(idProductsCounter, name, brand, price, amount, unit, category, notes, img)
        val dataProd = DataProduct(name, category)
        if (!allProducts.contains(dataProd)) {
            allProducts.add(dataProd)
            if (price > 0) {
                addPriceData(name, category, price)
            }
        } else {
            incrementProdUsed(name, category)
            if (price > 0) {
                addPriceData(name, category, price)
            }
        }
        val shopList = searchForList(listId) ?: return false
        shopList.addProduct(prod)
        return true
    }
    fun updateData(oldName: String,
                   oldCategory: String,
                   oldPrice: Double,
                   newName: String,
                   newCategory: String,
                   newPrice: Double){
        handleOldData(oldName, oldCategory, oldPrice)
        handleNewData(newName, newCategory, newPrice)
    }

    //Manage products and lists from allProducts and allLists, respectively
    fun removeProdData(oldName: String, oldCategory: String, oldPrice: Double){
        handleOldData(oldName, oldCategory, oldPrice)
    }
    fun removeListData(shoppingList: ShoppingList) {
        val duplicate = copyList(shoppingList)
        archiveList(duplicate)
        for(p in shoppingList.productList){
            removeProdData(p.name, p.category, p.price)
        }
        allLists.remove(shoppingList)
    }
    private fun archiveList(shoppingList: ShoppingList) {
        archivedLists.add(shoppingList)
    }
    private fun copyList(list: ShoppingList): ShoppingList {
        val duplicate = ShoppingList(list.name, list.id)
        for (prod in list.productList)
            duplicate.addProduct(prod)
        return duplicate
    }
    private fun incrementProdUsed(name: String, category: String) {
        for(dP in allProducts){
            if(dP.name == name && dP.category == category){
                dP.nTimesUsed++
                break
            }
        }
    }



    //ManagePrices
    fun getPrices(product: Product): String?{
        for(dP in allProducts){
            if(dP.name == product.name && dP.category == product.category){
                return dP.lastPrices.toString()
            }
        }
        return null
    }
    fun updateDataPrices(oldName: String,
                         oldCategory: String,
                         oldPrice: Double,
                         newName: String,
                         newCategory: String,
                         newPrice: Double){
        removePriceData(oldName, oldCategory, oldPrice)
        if(newPrice > 0){
            addPriceData(newName, newCategory, newPrice)
        }
    }
    private fun addPriceData(name: String, category: String, price: Double){
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
    private fun removePriceData(name: String, category: String, price: Double){
        for(dP in allProducts){
            if(dP.name == name && dP.category == category){
                dP.lastPrices.remove(price)
            }
        }
    }

    //Insert new data in all products and remove old data from the same list.
    private fun handleNewData(newName: String, newCategory: String, newPrice: Double) {
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
    private fun handleOldData(oldName: String, oldCategory: String, oldPrice: Double){
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

    //Add a list by name
    fun addListByName(name: String) : Int{
        val shopList = ShoppingList(name, idListCounter)
        allLists.add(shopList)
        return idList
    }

    //Re-usage lists
    fun recreateList(id: Int) {
        for(list in archivedLists) {
            if (list.id == id) {
                addListByShoppingList(recreateProducts(list))
                removeListFromArchive(list)
            }
        }
    }
    private fun addListByShoppingList(list: ShoppingList): Int {
        allLists.add(list)
        return list.id
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
            var dataProd = DataProduct(prod.name, prod.category)
            if (!allProducts.contains(dataProd)) {
                allProducts.add(dataProd)
                if (prod.price > 0) {
                    addPriceData(prod.name, prod.category, prod.price)
                }
            } else {
                incrementProdUsed(prod.name, prod.category)
                if (prod.price > 0) {
                    addPriceData(prod.name, prod.category, prod.price)
                }
            }
        }
        return list
    }
}