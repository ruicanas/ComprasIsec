package pt.isec.tp_amov.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import pt.isec.tp_amov.R

class FragmentChangeUnit : Fragment(){
    val TAG = "FragmentChangeUnits"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i(TAG, "onAttach1: ")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate1: ")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG, "onCreateView1: ")
        val view = inflater.inflate(R.layout.fragment_manage_units, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i(TAG, "onActivityCreated1: ")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause1: ")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop1: ")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(TAG, "onDestroyView1: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy1: ")
    }
    override fun onDetach() {
        super.onDetach()
        Log.i(TAG, "onDetach1: ")
    }
}