package com.example.dndcharsheet

import android.content.Context
import android.widget.ArrayAdapter
import com.example.dndcharsheet.character.Character

class CharacterAdapter(context: Context, private val characters: List<Character>) :
    ArrayAdapter<Character>(context, R.layout.character_item, characters) {
}