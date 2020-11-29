package pt.isec.tp_amov.objects

import android.graphics.Bitmap

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
                    units: UnitsMeasure, category: Categories, notes: String){
        this.name = name
        this.brand = brand
        this.price = price
        this.amount = amount
        this.units = units
        this.category = category
        this.notes = notes
    }

    fun addPhoto(bitmap: Bitmap) {
        photo = bitmap
    }
}