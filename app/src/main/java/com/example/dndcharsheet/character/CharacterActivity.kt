package com.example.dndcharsheet.character

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.dndcharsheet.inventory.InventoryAdapter
import com.example.dndcharsheet.inventory.InventoryItem
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.text.InputType
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.dndcharsheet.R
import com.example.dndcharsheet.inventory.InventoryActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class CharacterActivity : AppCompatActivity() {

    // Declare your views
    private lateinit var name: TextView
    private lateinit var age: TextView
    private lateinit var nameInput: EditText
    private lateinit var ageInput: EditText
    private lateinit var inventoryInput: EditText
    private lateinit var addItemButton: Button
    private lateinit var inventoryList: ListView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editCharacterButton: Button
    private lateinit var openInventoryButton: Button


    // SharedPreferences file name
    val sharedPreferencesFile = "DnDCharSheetSharedPreferences"
    private var character: Character = Character()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)

        // Initialize your views
        name = findViewById(R.id.name)
        age = findViewById(R.id.age)
        inventoryInput = findViewById(R.id.inventory_input)
        addItemButton = findViewById(R.id.add_item_button)
        inventoryList = findViewById(R.id.inventory_list)
        editCharacterButton = findViewById(R.id.edit_character_button)
        openInventoryButton = findViewById(R.id.open_inventory_button)

        // Set TextView fields
        name.text = character.name
        age.text = character.age


        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(sharedPreferencesFile, Context.MODE_PRIVATE)

        // Set OnClickListener for the Edit Character Data button
        editCharacterButton.setOnClickListener {
            showEditCharacterDialog()
        }
        // Load the saved data (if any) when the app starts
        loadData()

        // Update the inventory list
        updateInventoryList()

        addItemButton.setOnClickListener {
            // Get the text from inventoryInput

            val newItem = InventoryItem(name = inventoryInput.text.toString())

            // Add the new item to the inventory
            character.inventory.add(newItem)

            // Clear the inventoryInput
            inventoryInput.setText("")

            // Update the inventoryList and save the updated inventory
            updateInventoryList()
            saveData()
        }

    }

    // Request to open InventoryActivity and handle the result.
    private val startInventoryActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // The Intent that was returned by the activity that was launched
                val data: Intent? = result.data
                val updatedInventory = data?.getSerializableExtra("updatedInventory") as ArrayList<InventoryItem>
                character.inventory = updatedInventory
            }
        }

    // ...

    fun openInventoryActivity() {
        val intent = Intent(this, InventoryActivity::class.java)
        // Pass the inventory to InventoryActivity
        intent.putExtra("inventory", character.inventory)
        // Launch the InventoryActivity
        startInventoryActivity.launch(intent)
    }

    override fun onPause() {
        super.onPause()

        // Save data when the app is going to the background
        saveData()
    }

    fun saveData() {
        val gson = Gson()
        val editor = sharedPreferences.edit()

        // Load the list of characters
        val json = sharedPreferences.getString("characterList", null)
        val type = object : TypeToken<MutableList<Character>>() {}.type
        val characterList: MutableList<Character> = gson.fromJson(json, type) ?: mutableListOf()

        // Check if the character already exists in the list
        val existingCharacter = characterList.find { it.id == character.id }
        if (existingCharacter != null) {
            // If the character exists, update it
            existingCharacter.name = character.name
            existingCharacter.age = character.age
            existingCharacter.inventory = character.inventory
        } else {
            // If the character does not exist, add it to the list
            characterList.add(character)
        }

        // Save the updated list of characters
        val newJson = gson.toJson(characterList)
        editor.putString("characterList", newJson)
        editor.apply()
    }


    fun loadData() {
        val gson = Gson()

        // Load the list of characters
        val json = sharedPreferences.getString("characterList", null)
        val type = object : TypeToken<MutableList<Character>>() {}.type
        val characterList: MutableList<Character> = gson.fromJson(json, type) ?: mutableListOf()

        // Check if a character ID was passed to this activity
        val characterId = intent.getStringExtra("characterId")
        if (characterId != null) {
            // If an ID was passed, load the character with this ID
            character = characterList.find { it.id.toString() == characterId } ?: Character()
        } else {
            // If no ID was passed, create a new character
            character = Character()
        }

        // Set the TextView fields
        name.text = character.name
        age.text = character.age.toString()
    }


    private fun showEditCharacterDialog() {
        // Create an AlertDialog builder
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Edit Character Data")

        // Create a layout for the AlertDialog
        val dialogLayout = LayoutInflater.from(this).inflate(R.layout.dialog_edit_character, null)

        // Initialize the EditText fields
        nameInput = dialogLayout.findViewById(R.id.dialog_name_input)
        ageInput = dialogLayout.findViewById(R.id.dialog_age_input)

        // Set the EditText fields' text
        nameInput.setText(character.name)
        ageInput.setText(character.age)

        // Set the layout to the AlertDialog
        builder.setView(dialogLayout)

        // Set up the buttons
        builder.setPositiveButton("OK") { _, _ ->
            // Update the character's name and age
            character.name = nameInput.text.toString()
            character.age = ageInput.text.toString()

            // Update the TextView fields
            name.text = character.name
            age.text = character.age

            // Save the updated character data
            saveData()
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        // Show the dialog
        builder.show()
    }

    private fun updateInventoryList() {
        // Transform the list of InventoryItem objects into a list of strings
        val inventoryNames = character.inventory.map { it.name }

        // Create an ArrayAdapter
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, inventoryNames)

        // Set the adapter to the inventoryList
        inventoryList.adapter = adapter
//        // Create an InventoryAdapter
//        val adapter = InventoryAdapter(this, character.inventory)
//
//        // Set the adapter to the inventoryList
//        inventoryList.adapter = adapter
    }

    fun editItem(item: InventoryItem) {
        // Create an AlertDialog builder
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Edit Item")

        // Create an input field
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        input.setText(item.name) // Set the current name of the item
        builder.setView(input)

        // Set up the buttons
        builder.setPositiveButton("OK") { dialog, _ ->
            // Update the item's name
            item.name = input.text.toString()

            // Update the inventory list on the screen
            updateInventoryList()

            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        // Show the dialog
        builder.show()
    }

    fun removeItem(item: InventoryItem) {
        character.inventory.remove(item)
        updateInventoryList()
    }
}
