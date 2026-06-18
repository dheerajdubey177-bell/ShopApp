package com.example.shop.data.local

object LocationData {
    val indiaStates = mapOf(
        "Chhattisgarh" to listOf("Bhilai", "Raipur", "Bilaspur", "Durg"),
        "Maharashtra" to listOf("Pune", "Mumbai", "Nagpur", "Nashik"),
        "Delhi" to listOf("New Delhi", "North Delhi", "South Delhi"),
        "Karnataka" to listOf("Bangalore", "Mysore", "Hubli"),
        "West Bengal" to listOf("Kolkata", "Howrah", "Durgapur")
    )

    fun getStates() = indiaStates.keys.toList().sorted()
    fun getCities(state: String) = indiaStates[state] ?: emptyList()
}
