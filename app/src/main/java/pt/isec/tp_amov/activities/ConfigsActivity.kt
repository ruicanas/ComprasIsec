package pt.isec.tp_amov.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import pt.isec.tp_amov.R
import pt.isec.tp_amov.interfaces.ConfigOptionsInterface

class ConfigsActivity : AppCompatActivity(), ConfigOptionsInterface{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configs)
        hideFrags()
    }

    private fun hideFrags() {
        val fm: FragmentManager = supportFragmentManager
        val fragConfUnit: Fragment? = fm.findFragmentById(R.id.fragConfigUnit)
        val fragConfCat: Fragment? = fm.findFragmentById(R.id.fragConfigCategory)
        val fragConfLang: Fragment? = fm.findFragmentById(R.id.fragConfigLang)
        fm.beginTransaction()
            .hide(fragConfCat!!)
            .hide(fragConfLang!!)
            .hide(fragConfUnit!!)
            .commit()
    }

    override fun SwapToChangeCategory() {
        val fm: FragmentManager = supportFragmentManager
        val fragConfCat: Fragment? = fm.findFragmentById(R.id.fragConfigCategory)
        val fragOptConf: Fragment? = fm.findFragmentById(R.id.fragConfigOpts)
        fm.beginTransaction()
            .setCustomAnimations(android.R.animator.fade_in,0, 0, android.R.animator.fade_out)
            .show(fragConfCat!!)
            .hide(fragOptConf!!)
            .commit()
    }

    override fun SwapToChangeUnit() {
        TODO("Not yet implemented")
    }

    override fun SwapToChangeLanguage() {
        TODO("Not yet implemented")
    }
}