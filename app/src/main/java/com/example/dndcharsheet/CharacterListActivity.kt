package com.example.dndcharsheet

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.dndcharsheet.character.Character
import com.example.dndcharsheet.character.CharacterActivity
import com.example.dndcharsheet.inventory.InventoryAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.UUID

class CharacterListActivity : AppCompatActivity() {

    private lateinit var characterListView: ListView
    private var characterData: List<Character> = mutableListOf()
    private lateinit var newCharacterButton: Button
    private var characterList = mutableListOf<Character>()
    private lateinit var sharedPreferences: SharedPreferences
    private val sharedPreferencesFile = "DnDCharSheetSharedPreferences"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_list)

        // Initialize your views
        newCharacterButton = findViewById(R.id.new_character_button)
        characterListView = findViewById(R.id.character_list)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(sharedPreferencesFile, Context.MODE_PRIVATE)

        // Set the button's click listener
        newCharacterButton.setOnClickListener {
            // Create a new character
            val newCharacter = Character(name = "")

            // Add the new character to the list
            characterList.add(newCharacter)

            // Save the updated list to SharedPreferences
            saveData()

            // Switch to the CharacterActivity
            val intent = Intent(this, CharacterActivity::class.java).apply {
                // Pass the new character's ID to the activity
                putExtra("characterId", newCharacter.id.toString())
            }
            updateCharacterList()
            startActivity(intent)
        }

        loadData()
    }

    private fun loadData() {
        val gson = Gson()
        val json = sharedPreferences.getString("characterList", null)
        val type = object : TypeToken<List<Character>>() {}.type
        characterList = gson.fromJson(json, type) ?: mutableListOf()
        characterListView.adapter = CharacterListAdapter(this, characterList)
    }

    private fun updateCharacterList() {
        val adapter = CharacterListAdapter(this, characterList)

        // Set the adapter to the inventoryList
        characterListView.adapter = adapter
    }

    fun saveData() {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(characterList)
        editor.putString("characterList", json)
        editor.apply()
    }

    override fun onResume() {
        super.onResume()
        // Reload the data from shared preferences when the activity resumes
        loadData()
    }

    override fun onPause() {
        super.onPause()

        // Save data when the app is going to the background
        saveData()
    }
    fun removeCharacter(character: Character) {
        characterList.remove(character)
        updateCharacterList()
    }
}
