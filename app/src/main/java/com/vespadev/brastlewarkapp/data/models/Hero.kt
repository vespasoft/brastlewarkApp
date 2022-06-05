package com.vespadev.brastlewarkapp.data.models

data class Hero(
    val id: String = "",
    val name: String = "",
    var thumbnail: String = "",
    val age: Int,
    val weight: Double,
    val height: Double,
    val hair_color: String = "",
    val professions: List<String> = listOf(),
    val friends: List<String> = listOf()
)