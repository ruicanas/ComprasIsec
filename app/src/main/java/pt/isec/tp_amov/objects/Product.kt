package pt.isec.tp_amov.objects

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import pt.isec.tp_amov.R

data class Product (var id: Int, var name: String, var brand: String, var price: Double, var amount:Double,
                    var units: String, var category: String, var notes: String, var image: Bitmap?) {
    var prodChecked = false

    fun productExists(name: String, brand:String, price: Double, amount: Double,
                        units: UnitsMeasure, category: Categories, notes: String): Boolean {
        if(this.name == name && this.brand == brand && this.price == price && this.amount == amount
                && this.units.equals(units) && this.category.equals(category) && this.notes == notes)
            return true
        return false
    }

    fun editProduct(name: String, brand:String, price: Double, amount: Double,
                    units: String, category: String, image: Bitmap?, notes: String){
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
}