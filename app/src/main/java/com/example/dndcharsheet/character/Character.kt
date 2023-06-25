package com.example.dndcharsheet.character

import com.example.dndcharsheet.inventory.InventoryItem
import java.io.Serializable
import java.util.UUID

data class Character(
    var id: UUID = UUID.randomUUID(),
    var name: String = "",
    var age: String = "",
    var inventory: MutableList<InventoryItem> = mutableListOf()
) : Serializable
