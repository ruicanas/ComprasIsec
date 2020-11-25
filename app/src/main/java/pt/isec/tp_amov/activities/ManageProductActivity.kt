package pt.isec.tp_amov.activities

import android.os.Bundle
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
        findViewById<EditText>(R.id.edBrand).setText(sL!!.returnProduct(prodId)!!.brand)
        findViewById<EditText>(R.id.edPrice).setText(sL!!.returnProduct(prodId)!!.price.toString())
        findViewById<EditText>(R.id.edNotes).setText(sL!!.returnProduct(prodId)!!.notes)
        findViewById<EditText>(R.id.edQuantity).setText(sL!!.returnProduct(prodId)!!.amount.toString())
        setCategory(sL!!.returnProduct(prodId)!!.category)
        setUnit(sL!!.returnProduct(prodId)!!.units)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_new_product, menu)
        if(type == "create"){
            supportActionBar?.title = getString(R.string.titleAddProdList) + " " + Model.getListById(listId)?.name
            menu!!.getItem(0).isVisible = true
            menu!!.getItem(1).isVisible = false
        }
        else{
            supportActionBar?.title = getString(R.string.titleEditProdList) + " " + Model.getListById(listId)?.name
            menu!!.getItem(0).isVisible = false
            menu!!.getItem(1).isVisible = true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.newProdCheck) {
            val name: String = findViewById<EditText>(R.id.edProductName).text.toString()
            val brand: String = findViewById<EditText>(R.id.edBrand).text.toString()
            var price: String = findViewById<EditText>(R.id.edPrice).text.toString()
            val notes: String = findViewById<EditText>(R.id.edNotes).text.toString()
            val amount: String = findViewById<EditText>(R.id.edQuantity).text.toString()

            if (price.isEmpty())
                price = "0.0"

            //TODO - the id is temporary - find a better way
            Model.addProduct(name, brand, price.toDouble(), amount.toDouble(), getUnit(), getCategory(), notes, null, listId)
            finish()
        }

        if(item.itemId == R.id.editProdCheck){

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

    /**
     * NOT SETTING!
     */
    private fun setCategory(category: Categories){ //Not ideal strings
        var counter = 0
        for(i in Categories.values()){
            if(i == category){
                spCategory.setSelection(counter, true)
                break
            }
            counter++
        }
        spCategory.adapter
    }

    /**
     * NOT SETTING!
     */
    private fun setUnit(unit: UnitsMeasure){
        var counter = 0
        for(i in UnitsMeasure.values()){
            if(i == unit){
                spCategory.setSelection(counter, true)
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
}