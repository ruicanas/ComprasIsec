package pt.isec.tp_amov.objects

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import pt.isec.tp_amov.R

data class Product (var id: Int, var name: String, var brand: String, var price: Double, var amount:Double,
                    var units: UnitsMeasure, var category: Categories, var notes: String, var image: Bitmap?) {
    var prodChecked = false

    fun productExists(name: String, brand:String, price: Double, amount: Double,
                        units: UnitsMeasure, category: Categories, notes: String): Boolean {
        if(this.name == name && this.brand == brand && this.price == price && this.amount == amount
                && this.units == units && this.category == category && this.notes == notes)
            return true
        return false
    }

    fun editProduct(name: String, brand:String, price: Double, amount: Double,
                    units: UnitsMeasure, category: Categories, image: Bitmap?, notes: String){
        this.name = name
        this.brand = brand
        this.price = price
        this.amount = amount
        this.units = units
        this.category = category
        this.notes = notes
        if (image != null)
            this.image = image
        else
            this.image = null
    }

    fun getCategory(context: Context): String {
        return when (category) {
            Categories.FRUIT_VEGETABLES -> context.getString(R.string.fruit_vegetables)
            Categories.STARCHY_FOOD -> context.getString(R.string.starchy_food)
            Categories.DAIRY -> context.getString(R.string.dairy)
            Categories.PROTEIN -> context.getString(R.string.protein)
            Categories.FAT -> context.getString(R.string.fat)
        }
    }

    fun getUnit(context: Context): String {
        return when (units) {
            UnitsMeasure.UNITS -> context.getString(R.string.units)
            UnitsMeasure.KG -> context.getString(R.string.kg)
            UnitsMeasure.GRAMS -> context.getString(R.string.grams)
            UnitsMeasure.LITERS -> context.getString(R.string.liter)
            UnitsMeasure.BOXES -> context.getString(R.string.boxes)
        }
    }
}