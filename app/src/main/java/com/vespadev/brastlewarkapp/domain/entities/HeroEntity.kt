package com.vespadev.brastlewarkapp.domain.entities

data class HeroEntity(
    val id: String,
    val name: String,
    val imageUrl: String,
    val age: Int,
    val weight: Double,
    val height: Double,
    val hair_color: String,
    val professions: List<String>,
    val friends: List<HeroEntity>
)