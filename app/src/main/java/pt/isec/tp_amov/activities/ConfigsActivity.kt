package pt.isec.tp_amov.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import pt.isec.tp_amov.R
import pt.isec.tp_amov.fragments.FragmentChangeCategory
import pt.isec.tp_amov.fragments.FragmentChangeUnit
import pt.isec.tp_amov.fragments.FragmentConfigOpts
import pt.isec.tp_amov.interfaces.ConfigOptionsInterface

class ConfigsActivity : AppCompatActivity(), ConfigOptionsInterface{
    private val fm: FragmentManager = supportFragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configs)
        hideFrags()
    }

    private fun hideFrags() {
        fm.beginTransaction()
            .replace(R.id.fragContainer, FragmentConfigOpts())
            .commit()
    }

    override fun SwapToChangeCategory() {
        fm.beginTransaction()
            .setCustomAnimations(android.R.animator.fade_in,0)
            .replace(R.id.fragContainer, FragmentChangeCategory())
            .addToBackStack(null)
            .commit()
    }

    override fun SwapToChangeUnit() {
        fm.beginTransaction()
            .setCustomAnimations(android.R.animator.fade_in,0)
            .replace(R.id.fragContainer, FragmentChangeUnit())
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() {
        if(fm.backStackEntryCount > 0){
            fm.popBackStack()
        }else {
            super.onBackPressed()
        }
    }
}