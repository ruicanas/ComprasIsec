package pt.isec.tp_amov.model

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

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
    var hasImage = false
    var deleteImageButton = false
    var popupShowing = false

    //CONFIGS ACTIVITY VARS
    var dialogRemoveConfigShowing = false
    lateinit var fragment: Fragment
    var fragmentManager: FragmentManager? = null

    //MANAGE FRAGMENTS
    var currentFrag = -1

    var currentFilter = -1
    var dialogText: String = ""
}