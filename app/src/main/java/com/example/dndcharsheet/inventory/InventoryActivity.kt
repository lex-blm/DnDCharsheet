package com.example.dndcharsheet.inventory

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.dndcharsheet.R

class InventoryActivity : AppCompatActivity() {

    private lateinit var inventoryInput: EditText
    private lateinit var addItemButton: Button
    private lateinit var inventoryList: ListView
    private lateinit var sharedPreferences: SharedPreferences

    // SharedPreferences file name
    val sharedPreferencesFile = "DnDCharSheetSharedPreferences"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)

        sharedPreferences = getSharedPreferences(sharedPreferencesFile, Context.MODE_PRIVATE)
        inventoryList = findViewById(R.id.inventory_list)
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

    private fun updateInventoryList() {
        // Create an InventoryAdapter
        val adapter = InventoryAdapter(this, character.inventory)

        // Set the adapter to the inventoryList
        inventoryList.adapter = adapter
    }
}