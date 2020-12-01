package pt.isec.tp_amov.objects

data class DataProduct (var name: String, var category: Categories) {
    var lastPrices: MutableList<Double> = ArrayList()
    var nTimesUsed = 1
}