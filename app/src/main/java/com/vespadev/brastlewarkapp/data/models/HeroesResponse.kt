package com.vespadev.brastlewarkapp.data.models

import com.google.gson.annotations.SerializedName

data class HeroesResponse(
    @SerializedName("Brastlewark")
    val heroes: List<Hero> = listOf()
)