package pt.isec.tp_amov.activities

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import pt.isec.tp_amov.R
import pt.isec.tp_amov.model.Model
import pt.isec.tp_amov.objects.Categories
import pt.isec.tp_amov.objects.UnitsMeasure


/**
 * This activity is going to be responsible for the creation and edition of a product
 */
class ManageProductActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    lateinit var spCategory: Spinner
    lateinit var spUnit: Spinner
    lateinit var type: String
    var listId = -1
    var prodId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_product)
        listId = intent.getIntExtra("listId", -1)
        prodId = intent.getIntExtra("productId", -1)
        type = intent.getStringExtra("type")!!
        //Verify if the ID is valid
        if(listId == -1){
            finish()
        }

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
        if(prodId != -1){
            fillOptions()
        }
    }

    private fun fillOptions() {
        val sL = Model.getListById(listId)
        findViewById<EditText>(R.id.edProductName).setText(sL!!.returnProduct(prodId)!!.name)
        findViewById<EditText>(R.id.edBrand).setText(sL.returnProduct(prodId)!!.brand)
        findViewById<EditText>(R.id.edPrice).setText(sL.returnProduct(prodId)!!.price.toString())
        findViewById<EditText>(R.id.edNotes).setText(sL.returnProduct(prodId)!!.notes)
        findViewById<EditText>(R.id.edQuantity).setText(sL.returnProduct(prodId)!!.amount.toString())
        setCategory(sL.returnProduct(prodId)!!.category)
        setUnit(sL.returnProduct(prodId)!!.units)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_new_product, menu)
        if(type == "create"){
            supportActionBar?.title = getString(R.string.titleAddProdList) + " " + Model.getListById(listId)?.name
            menu!!.getItem(0).isVisible = true
            menu.getItem(1).isVisible = false
        }
        else{
            supportActionBar?.title = getString(R.string.titleEditProdList) + " " + Model.getListById(listId)?.name
            menu!!.getItem(0).isVisible = false
            menu.getItem(1).isVisible = true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.newProdCheck) {
            val name: String = findViewById<EditText>(R.id.edProductName).text.toString()
            val brand: String = findViewById<EditText>(R.id.edBrand).text.toString()
            val price: String = findViewById<EditText>(R.id.edPrice).text.toString()
            val notes: String = findViewById<EditText>(R.id.edNotes).text.toString()
            val quantity: String = findViewById<EditText>(R.id.edQuantity).text.toString()

            if (name.isEmpty()) {
                Toast.makeText(applicationContext, getString(R.string.no_product_name), Toast.LENGTH_LONG).show()
                return false
            }
            if (price.isEmpty()) {
                Toast.makeText(applicationContext, getString(R.string.no_product_price), Toast.LENGTH_LONG).show()
                return false
            }
            if (quantity == "0") { //TODO - prevent zero (better way)
                Toast.makeText(applicationContext, getString(R.string.no_product_quantity), Toast.LENGTH_LONG).show()
                return false
            }

            //TODO - the id is temporary - find a better way
            Model.addProduct(name, brand, price.toDouble(), quantity.toDouble(), getUnit(), getCategory(), notes, null, listId)
            finish()
        }

        if(item.itemId == R.id.editProdCheck){


            finish()
        }
        return super.onOptionsItemSelected(item)
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


    private fun setCategory(category: Categories){ //Not ideal strings
        var counter = 0
        for(i in Categories.values()){
            if(i == category){
                spCategory.setSelection(counter)
                spCategory.invalidate()
                break
            }
            counter++
        }
    }


    private fun setUnit(unit: UnitsMeasure){
        var counter = 0
        for(i in UnitsMeasure.values()){
            if(i == unit){
                spUnit.setSelection(counter)
                spUnit.invalidate()
                break
            }
            counter++
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        //setup listener for on spinner item selected
        val spinner: Spinner = findViewById(R.id.spinnerCat)
        spinner.onItemSelectedListener = this
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    fun onIncQuantity(view: View) {
        val editText: EditText = findViewById(R.id.edQuantity)
        val text: String = editText.text.toString()
        try {
            var num: Int = text.toInt()
            num += 1
            editText.setText(num.toString())

            Log.i("onQuantityInc int: ", num.toString())
        }
        catch (nfe: NumberFormatException) {
            var num: Double = text.toDouble()
            num += 1.0
            editText.setText(num.toString())

            Log.i("onQuantityInc double: ", num.toString())
        }
    }

    fun onDecQuantity(view: View) {
        val editText: EditText = findViewById(R.id.edQuantity)
        val text: String = editText.text.toString()
        try {
            var num: Int = text.toInt()
            if (num - 1 <= 0)
                num = 0
            else
                num -= 1
            editText.setText(num.toString())

            Log.i("onQuantityDec int: ", num.toString())
        }
        catch (nfe: NumberFormatException) {
            var num: Double = text.toDouble()
            if (num - 1.0 <= 0.0)
                num = 0.0
            else
                num -= 1.0
            editText.setText(num.toString())

            Log.i("onQuantityDec double: ", num.toString())
        }
    }
}