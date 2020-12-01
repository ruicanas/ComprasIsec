package pt.isec.tp_amov.objects

import android.content.res.Resources
import android.graphics.Bitmap
import pt.isec.tp_amov.R

data class Product (var id: Int, var name: String, var brand: String, var price: Double, var amount:Double,
                    var units: UnitsMeasure, var category: Categories, var notes: String, var image: Bitmap?) {
    lateinit var photo: Bitmap
    var prodChecked = false

    fun productExists(name: String, brand:String, price: Double, amount: Double,
                        units: UnitsMeasure, category: Categories, notes: String): Boolean {
        if(this.name == name && this.brand == brand && this.price == price && this.amount == amount
                && this.units == units && this.category == category && this.notes == notes)
            return true
        return false
    }

    fun editProduct(name: String, brand:String, price: Double, amount: Double,
                    units: UnitsMeasure, category: Categories, photo: Bitmap?, notes: String){
        this.name = name
        this.brand = brand
        this.price = price
        this.amount = amount
        this.units = units
        this.category = category
        this.notes = notes
        if (photo != null)
            this.photo = photo
    }

    fun addPhoto(bitmap: Bitmap) {
        photo = bitmap
    }

    fun getCategory(): String {
        /*return when (category) {
            Categories.FRUIT_VEGETABLES -> Resources.getSystem().getString(R.string.fruit_vegetables)
            Categories.STARCHY_FOOD -> Resources.getSystem().getString(R.string.starchy_food)
            Categories.DAIRY -> Resources.getSystem().getString(R.string.dairy)
            Categories.PROTEIN -> Resources.getSystem().getString(R.string.protein)
            Categories.FAT -> Resources.getSystem().getString(R.string.fat)
        }*/
        return ""
    }

    fun getUnit(): String {
        /*return when (units) {
            UnitsMeasure.UNITS -> Resources.getSystem().getString(R.string.units)
            UnitsMeasure.KG -> Resources.getSystem().getString(R.string.kg)
            UnitsMeasure.GRAMS -> Resources.getSystem().getString(R.string.grams)
            UnitsMeasure.LITERS -> Resources.getSystem().getString(R.string.liter)
            UnitsMeasure.BOXES -> Resources.getSystem().getString(R.string.boxes)
        }*/
        return ""
    }
}