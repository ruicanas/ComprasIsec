package pt.isec.tp_amov.activities

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import pt.isec.tp_amov.R
import pt.isec.tp_amov.objects.Categories
import pt.isec.tp_amov.objects.Product
import pt.isec.tp_amov.objects.UnitsMeasure


/**
 * This activity is going to be responsible for the creation and edition of a product
 */
class ManageProductActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    lateinit var spCategory: Spinner
    lateinit var spUnit: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_product)

        supportActionBar?.title = getString(R.string.titleAddProd) + " " + intent.getStringExtra("ListName")

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
            val quantity: String = findViewById<EditText>(R.id.edQuantity).text.toString()

            if (name.isEmpty()) {
                Toast.makeText(applicationContext, getString(R.string.no_product_name), Toast.LENGTH_LONG).show()
                return false
            }
            if (brand.isEmpty()) {
                Toast.makeText(applicationContext, getString(R.string.no_product_brand), Toast.LENGTH_LONG).show()
                return false
            }
            if (price.isEmpty()) {
                Toast.makeText(applicationContext, getString(R.string.no_product_price), Toast.LENGTH_LONG).show()
                return false
            }
            if (quantity == "0") {
                Toast.makeText(applicationContext, getString(R.string.no_product_quantity), Toast.LENGTH_LONG).show()
                return false
            }

            //TODO - the id is temporary - find a better way
            val product = Product(0, name, brand, price.toDouble(), quantity.toDouble(), getUnit(), getCategory(), notes, null)
            returnProduct(product)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun returnProduct(product: Product) {
        /*val returnIntent = Intent().apply {
            putExtra("Product", product as Serializable)
        }
        setResult(RESULT_OK, returnIntent)
        finish()*/
    }

    private fun getCategory(): Categories { //Not ideal strings
        val prompt = spCategory.selectedItem.toString()
        if (prompt == getString(R.string.fruit_vegetables))
            return Categories.FRUIT_VEGETABLES
        if (prompt == getString(R.string.dairy))
            return Categories.DAIRY
        if (prompt == getString(R.string.fat))
            return Categories.FAT
        if (prompt == getString(R.string.protein))
            return Categories.PROTEIN
        return Categories.STARCHY_FOOD
    }

    private fun getUnit(): UnitsMeasure {
        val prompt = spUnit.selectedItem.toString()
        if (prompt == getString(R.string.boxes))
            return UnitsMeasure.BOXES
        if (prompt == getString(R.string.kg))
            return UnitsMeasure.KG
        if (prompt == getString(R.string.grams))
            return UnitsMeasure.GRAMS
        if (prompt == getString(R.string.liter))
            return UnitsMeasure.LITERS
        return UnitsMeasure.UNITS
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        //setup listener for on spinner item selected
        val spinner: Spinner = findViewById(R.id.spinnerCat)
        spinner.onItemSelectedListener = this
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    fun onIncQuantity(view: View) {
        try {
            var editText: EditText = findViewById(R.id.edQuantity)
            var text: String = editText.text.toString()
            var num: Int = text.toInt()
            num += 1
            editText.setText(num.toString())
            Log.i("onQuantity: ", num.toString())
        } catch (nfe: NumberFormatException) {
            Log.i("onQuantity catch ", "inc")
            nfe.toString()
        }
    }
    fun onDecQuantity(view: View) {
        try {
            var editText: EditText = findViewById(R.id.edQuantity)
            var text: String = editText.text.toString()
            var num: Int = text.toInt()
            num -= 1
            editText.setText(num.toString())
            Log.i("onQuantity: ", num.toString())
        } catch (nfe: NumberFormatException) {
            Log.i("onQuantity catch ", "dec")
            nfe.toString()
        }
    }
}