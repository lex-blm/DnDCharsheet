package com.example.dndcharsheet.inventory

import java.util.UUID

data class InventoryItem(var id: UUID = UUID.randomUUID(), var name: String)

