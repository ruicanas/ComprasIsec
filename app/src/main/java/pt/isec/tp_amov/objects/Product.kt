package pt.isec.tp_amov.objects

import java.io.Serializable

data class Product (var id: Int, var name: String, var brand: String, var price: Double, var amount:Double,
                    var units: UnitsMeasure, var category: Categories, var notes: String, var image: String?): Serializable {}