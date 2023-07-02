package com.example.dndcharsheet.inventory
import java.io.Serializable

import java.util.UUID

data class InventoryItem(var id: UUID = UUID.randomUUID(), var quantity: Int = 1, var name: String) : Serializable

