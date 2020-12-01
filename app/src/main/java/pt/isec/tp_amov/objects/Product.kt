package pt.isec.tp_amov.objects

import android.content.Context
import android.graphics.Bitmap
import pt.isec.tp_amov.R

data class Product (var id: Int, var name: String, var brand: String, var price: Double, var amount:Double,
                    var units: String, var category: String, var notes: String, var image: Bitmap?) {
    lateinit var photo: Bitmap
    var prodChecked = false

    fun editProduct(name: String, brand:String, price: Double, amount: Double,
                    units: String, category: String, photo: Bitmap?, notes: String){
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
}