package pt.isec.tp_amov

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

/**
 * This activity is going to be responsible for the creation and edition of a product
 */
class ManageProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_product)
    }
}