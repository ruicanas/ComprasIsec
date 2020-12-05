package pt.isec.tp_amov.utils

import android.content.res.Resources
import pt.isec.tp_amov.R
import java.io.Serializable

class Configuration: Serializable {
    val categories : MutableList<String> = ArrayList()
    val units : MutableList<String> = ArrayList()
}