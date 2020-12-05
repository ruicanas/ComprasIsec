package pt.isec.tp_amov.model

import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData

object ModelView {
    //SHOW LIST ACTIVITY VARS
    var removeProdID = -1
    var dialogRemoveShowingSL = false
    var dialogHelpShowingSL = false

    //MANAGE PRODUCT ACTIVITY VARS
    var dialogNewCategoryShowing = false
    var dialogNewUnitsShowing = false

    //MAIN ACTIVITY PRODUCT ACTIVITY VARS
    var removeListID = -1
    var dialogNewListShowing = false
    var dialogHelpShowing = false
    var dialogOldListShowing = false
    var dialogRemoveShowing = false
    var deleteImageButton = false
    var popupShowing = false

    //MANAGE FRAGMENTS
    var unitRemoveShowing = false
    var categoryRemoveShowing = false
    var removeString = ""

    var currentFilter = -1
    var dialogText: String = ""
}