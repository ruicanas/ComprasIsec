package pt.isec.tp_amov.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import pt.isec.tp_amov.R
import pt.isec.tp_amov.objects.Categories
import pt.isec.tp_amov.objects.Product
import pt.isec.tp_amov.objects.UnitsMeasure
import java.io.Serializable


/**
 * This activity is going to be responsible for the creation and edition of a product
 */
class ManageProductActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    lateinit var spCategory: Spinner
    lateinit var spUnit: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_product)

        //TODO - this is working properly
        supportActionBar?.title = R.string.new_product.toString()

        spCategory = findViewById(R.id.spinnerCat)
        spUnit = findViewById(R.id.spinnerUnit)

        //create array adapter for the spinner
        ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_item).also { adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spCategory.adapter = adapter
        }

        //create array adapter for the spinner
        ArrayAdapter.createFromResource(this, R.array.unit_array, android.R.layout.simple_spinner_item).also { adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spUnit.adapter = adapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_new_product, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.newProdCheck) {
            val name: String = findViewById<EditText>(R.id.edProductName).text.toString()
            val brand: String = findViewById<EditText>(R.id.edBrand).text.toString()
            var price: String = findViewById<EditText>(R.id.edPrice).text.toString()
            val notes: String = findViewById<EditText>(R.id.edNotes).text.toString()
            val amount: String = findViewById<EditText>(R.id.edAmount).text.toString()

            if (price.isEmpty())
                price = "0.0"

            //TODO - the id is temporary - find a better way
            val product = Product(0, name, brand, price.toDouble(), amount.toDouble(), getUnit(), getCategory(), notes, null)
            returnProduct(product)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun returnProduct(product: Product) {
        val returnIntent = Intent().apply {
            putExtra("Product", product as Serializable)
        }
        setResult(RESULT_OK, returnIntent)
        finish()
    }

    private fun getCategory(): Categories {
        val prompt = spCategory.selectedItem.toString()

        if (prompt.equals(Categories.FRUIT_VEGETABLES))
            return Categories.FRUIT_VEGETABLES
        if (prompt.equals(Categories.DAIRY))
            return Categories.DAIRY
        if (prompt.equals(Categories.FAT))
            return Categories.FAT
        if (prompt.equals(Categories.PROTEIN))
            return Categories.PROTEIN
        return Categories.STARCHY_FOOD
    }

    private fun getUnit(): UnitsMeasure{
        val prompt = spUnit.selectedItem.toString()

        if (prompt.equals(UnitsMeasure.BOXES))
            return UnitsMeasure.BOXES
        if (prompt.equals(UnitsMeasure.KG))
            return UnitsMeasure.KG
        if (prompt.equals(UnitsMeasure.GRAMS))
            return UnitsMeasure.GRAMS
        if (prompt.equals(UnitsMeasure.LITERS))
            return UnitsMeasure.LITERS
        return UnitsMeasure.UNITS
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        //setup listener for on spinner item selected
        val spinner: Spinner = findViewById(R.id.spinnerCat)
        spinner.onItemSelectedListener = this
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}


}