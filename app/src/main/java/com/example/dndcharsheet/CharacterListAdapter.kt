package com.example.dndcharsheet

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.example.dndcharsheet.character.Character
import com.example.dndcharsheet.character.CharacterActivity

class CharacterListAdapter(context: Context, private val characters: List<Character>) :
    ArrayAdapter<Character>(context, R.layout.character_item, characters) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val characterView = convertView ?: LayoutInflater.from(context).inflate(R.layout.character_item, parent, false)

        // Get the current character
        val character = characters[position]

        // Set the character name
        val characterNameTextView = characterView.findViewById<TextView>(R.id.character_name)
        characterNameTextView.text = character.name

        // Set the edit button click listener
        val editButton = characterView.findViewById<Button>(R.id.edit_character_button)
        editButton.setOnClickListener {
            // Do something like starting the edit activity
            // For example:
            val intent = Intent(context, CharacterActivity::class.java).apply {
                putExtra("characterId", character.id.toString())
            }
            context.startActivity(intent)
        }

        // Set the remove button click listener
        val removeButton = characterView.findViewById<Button>(R.id.remove_character_button)
        removeButton.setOnClickListener {
            (context as CharacterListActivity).removeCharacter(character)
            (context as CharacterListActivity).saveData()
        }

        return characterView
    }
}
