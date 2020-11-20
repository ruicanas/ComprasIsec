package pt.isec.tp_amov.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pt.isec.tp_amov.R

/**
 * This activity is going to be responsible for the creation and edition of a product
 */
class ManageProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_product)
    }


}