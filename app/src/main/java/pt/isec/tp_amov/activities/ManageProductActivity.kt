package pt.isec.tp_amov.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.media.Image
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import pt.isec.tp_amov.R
import pt.isec.tp_amov.model.Model
import pt.isec.tp_amov.model.ModelView
import pt.isec.tp_amov.objects.Categories
import pt.isec.tp_amov.objects.UnitsMeasure
import java.io.File
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*


/**
 * This activity is going to be responsible for the creation and edition of a product
 */

class ManageProductActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private val tagMpa = "ManageProductActivity"

    private lateinit var spCategory: Spinner
    private lateinit var spUnit: Spinner
    private lateinit var type: String
    private var dataName: String? = null
    private var dataCat: String? = null
    private var listId = -1
    private var prodId = -1

    private var bitmap: Bitmap? = null
    private lateinit var imageView: ImageView
    private var edText: String = ""

    private lateinit var dialogNewCategory: AlertDialog
    private lateinit var dialogNewUnit: AlertDialog

    /**
     * Camera vals
     */
    private val cameraPermissionCode = 101
    private val galleryPermissionCode = 102
    private val cameraIntentCode = 11
    private val galleryIntentCode = 12

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_product)

        imageView = findViewById(R.id.productImageView)

        listId = intent.getIntExtra("listId", -1)
        prodId = intent.getIntExtra("productId", -1)
        type = intent.getStringExtra("type")!!
        dataName = intent.getStringExtra("dataName")
        dataCat = intent.getStringExtra("dataCat")

        //Verify if the ID is valid
        if(listId == -1){
            Log.i(tagMpa, "onCreate: Received an invalid list id.")
            finish()
        }

        //Handle spinners
        spCategory = findViewById(R.id.spinnerCat)
        spUnit = findViewById(R.id.spinnerUnit)
        loadCategories()
        loadUnits()
        //Check which type was received
        if(prodId != -1 && type == "edit"){
            fillOptions()
        }
        else if(type == "reuseData" && dataName != null && dataCat != null){
            fillPartialOpts()
        }

        val currency = findViewById<TextView>(R.id.currency)
        currency.text = getString(R.string.currency)

        if (savedInstanceState != null) {
            if (ModelView.dialogNewCategoryShowing)
                onNewCategory(findViewById(R.id.addNewCategory))
            if (ModelView.dialogNewUnitsShowing)
                onNewUnitType(findViewById(R.id.addNewUnit))
            if (ModelView.hasImage) {
                val byteArray: ByteArray = savedInstanceState.getByteArray("image")!!
                bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                imageView.setImageBitmap(bitmap)
            }
        }
    }

    override fun onDestroy() {
        try {
            if (dialogNewUnit.isShowing)
                dialogNewUnit.dismiss()
        } catch (e: UninitializedPropertyAccessException) { }
        try {
            if (dialogNewCategory.isShowing)
                dialogNewCategory.dismiss()
        } catch (e: UninitializedPropertyAccessException) { }

        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (ModelView.dialogNewCategoryShowing) {
            if (edText.isNotEmpty())
                ModelView.dialogText = edText
        }
        if (ModelView.dialogNewUnitsShowing) {
            if (edText.isNotEmpty())
                ModelView.dialogText = edText
        }

        super.onSaveInstanceState(outState)
    }

    private fun loadUnits() {
        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_item, Model.config.units)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spUnit.adapter = arrayAdapter
    }

    private fun loadCategories() {
        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_item, Model.config.categories)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCategory.adapter = arrayAdapter
    }

    private fun removeItemCategoryDlg(item: String){
        val builder = AlertDialog.Builder(this)     //Construct the builder
        val inflater = this.layoutInflater
        val viewLayout : View = inflater.inflate(R.layout.dialog_remove_item, null)  //The layout we want to inflate

        val msg = StringBuilder()
        msg.append(getString(R.string.remove_item_description_dlg)).append(" ").append(item).append("?")
        viewLayout.findViewById<TextView>(R.id.tvRemoveItemDlg).text = msg.toString()

        builder.setView(viewLayout)
        builder.setPositiveButton(getString(R.string.delete_dlg)) {dialog, id ->
            Model.config.categories.remove(item)
        }
        builder.setNegativeButton(getString(R.string.cancel_list)) { dialog, id -> dialog.dismiss() }
        builder.show()
    }

    private fun removeItemUnitsDlg(item: String){
        val builder = AlertDialog.Builder(this)     //Construct the builder
        val inflater = this.layoutInflater
        val viewLayout : View = inflater.inflate(R.layout.dialog_remove_item, null)  //The layout we want to inflate

        val msg = StringBuilder()
        msg.append(getString(R.string.remove_item_description_dlg)).append(" ").append(item).append("?")
        viewLayout.findViewById<TextView>(R.id.tvRemoveItemDlg).text = msg.toString()

        builder.setView(viewLayout)
        builder.setPositiveButton(getString(R.string.delete_dlg)) {dialog, id ->
            Model.config.units.remove(item)
        }
        builder.setNegativeButton(getString(R.string.cancel_list)) { dialog, id -> dialog.dismiss() }
        builder.show()
    }

    private fun fillOptions() {
        val sL = Model.getListById(listId)
        findViewById<EditText>(R.id.edProductName).setText(sL!!.returnProduct(prodId)!!.name)
        findViewById<EditText>(R.id.edBrand).setText(sL.returnProduct(prodId)!!.brand)
        findViewById<EditText>(R.id.edPrice).setText(sL.returnProduct(prodId)!!.price.toString())
        findViewById<EditText>(R.id.edNotes).setText(sL.returnProduct(prodId)!!.notes)
        findViewById<EditText>(R.id.edQuantity).setText(sL.returnProduct(prodId)!!.amount.toString())

        val imageBtn = findViewById<ImageButton>(R.id.deleteImageBtn)
        val img = sL.returnProduct(prodId)!!.image
        findViewById<ImageView>(R.id.productImageView).setImageBitmap(img)
        if (img != null)
            imageBtn.visibility = View.VISIBLE

        setCategory(sL.returnProduct(prodId)!!.category)
        setUnit(sL.returnProduct(prodId)!!.units)
    }
    private fun fillPartialOpts() {
        findViewById<EditText>(R.id.edProductName).setText(dataName)
        searchCategory(dataCat)
    }

    //Handle the spinners information
    private fun getCategory(): String { //Not ideal strings
        return spCategory.selectedItem.toString()
    }

    private fun searchCategory(category: String?){
        if(category == null){
            return
        }
        var counter = 0
        for(i in Model.config.categories){
            if(i.toString() == category){
                spCategory.setSelection(counter)
                spCategory.invalidate()
                break
            }
            counter++
        }
    }

    private fun getUnit(): String {
        return spUnit.selectedItem.toString()
    }

    private fun setCategory(category: String){ //Not ideal strings
        var counter = 0
        for(i in Model.config.categories){
            if(i == category){
                spCategory.setSelection(counter)
                spCategory.invalidate()
                break
            }
            counter++
        }
    }

    private fun setUnit(unit: String){
        var counter = 0
        for(i in Model.config.units){
            if(i == unit){
                spUnit.setSelection(counter)
                spUnit.invalidate()
                break
            }
            counter++
        }
    }

    //Create the option on the menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_new_product, menu)
        if(type == "create" || type == "reuseData"){
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

    //Will handle the items clicked by the user on the menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
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
        if (quantity == "0") { //TODO - prevent zero (find better way)
            Toast.makeText(applicationContext, getString(R.string.no_product_quantity), Toast.LENGTH_LONG).show()
            return false
        }

        bitmap = try {
            val bitmapDrawable: BitmapDrawable = imageView.drawable as BitmapDrawable
            bitmapDrawable.bitmap
        } catch (e: TypeCastException) {
            null
        }

        if(item.itemId == R.id.newProdCheck) {
            Model.receiveProduct(name, brand, price.toDouble(), quantity.toDouble(), getUnit(), getCategory(), notes, bitmap, listId)
            finish()
        }

        if(item.itemId == R.id.editProdCheck){
            val prod = Model.getProdById(prodId, listId)

            if(prod!!.name != name) {
                //If the name of the product changed and the product doesn't exist in the database, adds the product to the "database" and to the list
                //We cant forget to update the database, because we got one item that is not being used anymore
                Model.updateDataBase(prod.name, prod.category, prod.price, name, getCategory(), price.toDouble())
                prod.editProduct(name, brand, price.toDouble(), quantity.toDouble(), getUnit(), getCategory(), bitmap, notes)
            } else {
                //If the product is in the database and it was modified, we're just going to modify our product
                if (prod.price == price.toDouble()) {
                    prod.editProduct(name, brand, price.toDouble(), quantity.toDouble(), getUnit(), getCategory(), bitmap, notes)
                } else {
                    Model.updateDataPrices(prod.name, prod.category, prod.price, name, getCategory(), price.toDouble())
                    prod.editProduct(name, brand, price.toDouble(), quantity.toDouble(), getUnit(), getCategory(), bitmap, notes)
                }
            }
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    //Will increment the amount of a product
    fun onIncQuantity(view: View) {
        val editText: EditText = findViewById(R.id.edQuantity)
        val text: String = editText.text.toString()
        try {
            var num: Int = text.toInt()
            num += 1
            editText.setText(num.toString())

            Log.i("onQuantityInc int", num.toString())
        }
        catch (nfe: NumberFormatException) {
            var num: Double = text.toDouble()
            num += 1.0
            editText.setText(num.toString())

            Log.i("onQuantityInc double", num.toString())
        }
    }

    //Will decrement the amount of a product
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

    fun onOpenCamera(view: View) {
        //Ask for camera permissions
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), cameraPermissionCode)
        else {
            Log.i("Permissions", "Camera permission already granted")

            val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            //code 1 is gallery access
            startActivityForResult(takePicture, cameraIntentCode)
        }
    }

    fun onOpenGalley(view: View) {
        //Ask for storage permissions
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), galleryPermissionCode)
        }
        else {
            Log.i("Permissions", "Galley permission already granted")

            val selectPicture = Intent(Intent.ACTION_PICK)
            selectPicture.type = "image/*"
            //code 2 is gallery access
            startActivityForResult(selectPicture, galleryIntentCode)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == cameraPermissionCode) { //CAMERA PERMISSION ACCESS
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("Permissions", "Camera permission granted")

                val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                //code 1 is gallery access
                startActivityForResult(takePicture, cameraIntentCode)
            }
            else
                Log.i("Permissions", "Camera permission denied")
        }
        else if (requestCode == galleryPermissionCode) { //GALLERY PERMISSION ACCESS
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("Permissions", "Gallery permission granted")

                val selectPicture = Intent(Intent.ACTION_PICK)
                selectPicture.type = "image/*"
                //code 2 is gallery access
                startActivityForResult(selectPicture, galleryIntentCode)
            }
            else
                Log.i("Permissions", "Gallery permission denied")
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private lateinit var filePath : String

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == cameraIntentCode && resultCode == Activity.RESULT_OK && data != null) { //Camera Access
            if (data.extras == null)
                Toast.makeText(applicationContext, "Error loading image", Toast.LENGTH_LONG).show()

            val bitmap = data.extras?.get("data") as Bitmap
            imageView.setImageBitmap(bitmap) //set in image bitmap
            ModelView.hasImage = true
            val btn = findViewById<ImageButton>(R.id.deleteImageBtn)
            btn.visibility = View.VISIBLE
            ModelView.deleteImageButton = true
        }
        else if (requestCode == galleryIntentCode && resultCode == Activity.RESULT_OK && data != null) { //Gallery Access
            var uri = data.data?.apply {
                val cursor = contentResolver.query(
                        this,
                        arrayOf(MediaStore.Images.ImageColumns.DATA),
                        null,
                        null,
                        null
                )

                if (cursor != null && cursor.moveToFirst())
                    filePath = cursor.getString(0)

                //Get the bitmap
                bitmap = BitmapFactory.decodeFile(filePath)
                imageView.setImageBitmap(bitmap) //set in image bitmap
                ModelView.hasImage = true
                val btn = findViewById<ImageButton>(R.id.deleteImageBtn)
                btn.visibility = View.VISIBLE
                ModelView.deleteImageButton = true
            }
            return
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    /*lateinit var currentPhotoPath: String
    private fun saveImage(bitmap: Bitmap): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmm").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir *//* directory *//*).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }*/



    fun onNewCategory(view: View) {
        ModelView.dialogNewCategoryShowing = true
        val inflater = this.layoutInflater
        val view: View = inflater.inflate(R.layout.dialog_new_category, null) //The layout to inflate
        val editText = view.findViewById<EditText>(R.id.newUnitName)

        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        builder.setCancelable(true)
        builder.setOnCancelListener { ModelView.dialogNewCategoryShowing = false }
        builder.setPositiveButton(getString(R.string.add)) { dialog, id ->
            ModelView.dialogNewCategoryShowing = false
            dialog.dismiss()
            newCatName = editText.text.toString()
            addToCategories()
            edText = ""
        }
        builder.setNegativeButton(getString(R.string.dialog_back)) { dialog, id ->
            ModelView.dialogNewCategoryShowing = false
            dialog.dismiss()
        }
        dialogNewCategory = builder.show()
    }

    private fun addToCategories(name: String) {
        if(!Model.config.categories.contains(name)){
            Model.config.categories.add(name)
        }
    }


    fun onNewUnitType(view: View) {
        ModelView.dialogNewUnitsShowing = true
        val inflater = this.layoutInflater
        val view: View = inflater.inflate(R.layout.dialog_new_unit, null) //The layout to inflate
        val editText = view.findViewById<EditText>(R.id.newUnitName)

        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        builder.setCancelable(true)
        builder.setOnCancelListener { ModelView.dialogNewUnitsShowing = false }
        builder.setPositiveButton(getString(R.string.add)) { dialog, id ->
            ModelView.dialogNewUnitsShowing = false
            dialog.dismiss()
            newUnitName = editText.text.toString()
            addToUnits()
            edText = ""
        }
        builder.setNegativeButton(getString(R.string.dialog_back)) { dialog, id ->
            ModelView.dialogNewUnitsShowing = false
            dialog.dismiss()
        }
        dialogNewUnit = builder.show()
    }

    private fun addToUnits(name: String) {
        if (!Model.config.units.contains(name)) {
            Model.config.units.add(name)
        }
    }

    fun onDeletePicture(view: View) {
        ModelView.hasImage = false
        ModelView.deleteImageButton = false
        val btn = findViewById<ImageButton>(R.id.deleteImageBtn)
        btn.visibility = View.INVISIBLE

        imageView.setImageBitmap(null)
        imageView.invalidate()
    }
}