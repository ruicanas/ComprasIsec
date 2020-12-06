package pt.isec.tp_amov.objects
import java.io.Serializable

data class Product (var id: Int, var name: String, var brand: String, var price: Double, var amount:Double,
                    var units: String, var category: String, var notes: String, var image: ByteArray?): Serializable {
    var prodChecked = false

    fun editProduct(name: String, brand:String, price: Double, amount: Double,
                    units: String, category: String, image: ByteArray?, notes: String){
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