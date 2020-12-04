package pt.isec.tp_amov.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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

        if (savedInstanceState != null) {
            val fragment = fm.getFragment(savedInstanceState, "rotate")
            fm.beginTransaction().remove(fragment!!).commit()
        } else {
            setContentView(R.layout.activity_configs)
            init()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val fragment = fm.fragments[fm.fragments.size-1]
        fm.putFragment(outState, "rotate", fragment)
        super.onSaveInstanceState(outState)
    }

    private fun init() {
        fm.beginTransaction()
            .replace(R.id.fragContainer, FragmentConfigOpts())
            .commit()
    }

    override fun swapToChangeCategory() {
        fm.beginTransaction()
            .setCustomAnimations(android.R.animator.fade_in,0)
            .replace(R.id.fragContainer, FragmentChangeCategory())
            .addToBackStack(null)
            .commit()
    }

    override fun swapToChangeUnit() {
        fm.beginTransaction()
            .setCustomAnimations(android.R.animator.fade_in,0)
            .replace(R.id.fragContainer, FragmentChangeUnit())
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() {
        if (fm.backStackEntryCount > 0) {
            fm.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}