package pt.isec.tp_amov.utils

import java.io.Serializable

class Configuration: Serializable {
    val categories : MutableList<String> = ArrayList()
    val units : MutableList<String> = ArrayList()
}