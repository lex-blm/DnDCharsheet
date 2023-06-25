package com.example.dndcharsheet

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.dndcharsheet.inventory.InventoryAdapter
import com.example.dndcharsheet.inventory.InventoryItem
import com.example.dndcharsheet.character.Character
import android.app.AlertDialog
import android.content.SharedPreferences
import android.text.InputType
import android.view.LayoutInflater
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class MainActivity : AppCompatActivity() {

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


    // SharedPreferences file name
    val sharedPreferencesFile = "DnDCharSheetSharedPreferences"
    private var character: Character = Character()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize your views
        name = findViewById(R.id.name)
        age = findViewById(R.id.age)
        inventoryInput = findViewById(R.id.inventory_input)
        addItemButton = findViewById(R.id.add_item_button)
        inventoryList = findViewById(R.id.inventory_list)
        editCharacterButton = findViewById(R.id.edit_character_button)

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

    override fun onPause() {
        super.onPause()

        // Save data when the app is going to the background
        saveData()
    }

    fun saveData() {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(character)
        editor.putString("character", json)
        editor.apply()
    }

    fun loadData() {
        val gson = Gson()
        val json = sharedPreferences.getString("character", null)
        val type = object : TypeToken<Character>() {}.type
        character = gson.fromJson(json, type) ?: Character()
        name.text = character.name
        age.text = character.age
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
        // Create an InventoryAdapter
        val adapter = InventoryAdapter(this, character.inventory)

        // Set the adapter to the inventoryList
        inventoryList.adapter = adapter
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
