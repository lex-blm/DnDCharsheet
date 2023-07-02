package com.example.dndcharsheet.inventory

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.example.dndcharsheet.character.CharacterActivity
import com.example.dndcharsheet.R

class InventoryAdapter(context: Context, private val items: List<InventoryItem>) :
    ArrayAdapter<InventoryItem>(context, R.layout.inventory_item, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemView = convertView ?: LayoutInflater.from(context).inflate(R.layout.inventory_item, parent, false)

        // Get the current item
        val item = items[position]

        // Set the item name
        val itemNameTextView = itemView.findViewById<TextView>(R.id.item_name)
        itemNameTextView.text = item.name

        // Set the edit button click listener
        val editButton = itemView.findViewById<Button>(R.id.edit_button)
        editButton.setOnClickListener {
            (context as CharacterActivity).editItem(item)
            (context as CharacterActivity).saveData()
        }

        // Set the remove button click listener
        val removeButton = itemView.findViewById<Button>(R.id.remove_button)
        removeButton.setOnClickListener {
            (context as CharacterActivity).removeItem(item)
            (context as CharacterActivity).saveData()
        }

        return itemView
    }
}
