package pt.isec.tp_amov.objects

import java.io.Serializable

data class DataProduct (var name: String, var category: String): Serializable {
    var lastPrices: MutableList<Double> = ArrayList()
    var nTimesUsed = 1
}