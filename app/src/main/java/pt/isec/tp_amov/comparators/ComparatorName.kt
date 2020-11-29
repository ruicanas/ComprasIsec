package pt.isec.tp_amov.comparators

import pt.isec.tp_amov.objects.Product

class ComparatorName : Comparator<Product> {
    override fun compare(o1: Product?, o2: Product?): Int {
        if (o1 != null && o2 != null) {
            if(o1.name > o2.name){
                return 1
            } else if(o1.name < o2.name){
                return -1
            }
            return 0
        }
        return -9999
    }
}