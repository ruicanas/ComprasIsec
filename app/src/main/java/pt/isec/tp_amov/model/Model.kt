package pt.isec.tp_amov.model

import android.content.Context
import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import pt.isec.tp_amov.R
import pt.isec.tp_amov.objects.*
import pt.isec.tp_amov.utils.Configuration
import java.io.*
import javax.security.auth.login.LoginException

object Model {
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
    var archivedLists: MutableList<ShoppingList> = ArrayList()
    var allLists: MutableList<ShoppingList> = ArrayList()
    var allProducts: MutableList<DataProduct> = ArrayList()
    var config = Configuration()

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
                       img: ByteArray?,
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

    //There is a problem either during save or load.
    //For some reason either writing or falling

    //Save Model to storage
    fun save(context: Context) {
        val dirs = context.filesDir
        if (dirs.exists()) {
            dirs.delete()
            Toast.makeText(context, context.getString(R.string.saved), Toast.LENGTH_SHORT).show()
        }
        val fos = context.openFileOutput("model.bin", Context.MODE_PRIVATE)
        val os = ObjectOutputStream(fos)
        os.writeObject(idList)
        os.writeObject(idProducts)
        os.writeObject(archivedLists)
        os.writeObject(allLists)
        os.writeObject(allProducts)
        os.writeObject(config)
        os.close()
        fos.close()
    }

    //Load Model from storage
    fun load(context: Context) {
        try {
            val dirs = context.filesDir
            if (dirs.exists()) {
                val file = File("model.bin")
                val fis = context.openFileInput(file.path)
                val ois = ObjectInputStream(fis)
                idList = ois.readObject() as Int
                idProducts = ois.readObject() as Int
                archivedLists = ois.readObject() as ArrayList<ShoppingList>
                allLists = ois.readObject() as ArrayList<ShoppingList>
                allProducts = ois.readObject() as ArrayList<DataProduct>
                config = ois.readObject() as Configuration
                ois.close()
                fis.close()
            }
        } catch (eof: EOFException) {
            eof.printStackTrace()
            initialConfigs(context)
            Toast.makeText(context, context.getString(R.string.error_loading), Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
            initialConfigs(context)
        }
    }

    private fun initialConfigs(context: Context) {
        if(config.units.isEmpty()) {
            config.units.add(context.getString(R.string.units))
            config.units.add(context.getString(R.string.kg))
            config.units.add(context.getString(R.string.grams))
            config.units.add(context.getString(R.string.liter))
            config.units.add(context.getString(R.string.boxes))
        }
        if(config.categories.isEmpty()) {
            config.categories.add(context.getString(R.string.fruit_vegetables))
            config.categories.add(context.getString(R.string.starchy_food))
            config.categories.add(context.getString(R.string.dairy))
            config.categories.add(context.getString(R.string.protein))
            config.categories.add(context.getString(R.string.fat))
        }
    }
}