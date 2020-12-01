package pt.isec.tp_amov.comparators

import pt.isec.tp_amov.objects.DataProduct
import pt.isec.tp_amov.objects.Product
import java.util.Comparator

class ComparatorCategoryData : Comparator<DataProduct> {
    override fun compare(o1: DataProduct?, o2: DataProduct?): Int {
        if (o1 != null && o2 != null) {
            if(o1.category > o2.category){
                return 1
            } else if(o1.category < o2.category){
                return -1
            }
            return 0
        }
        return -9999
    }
}