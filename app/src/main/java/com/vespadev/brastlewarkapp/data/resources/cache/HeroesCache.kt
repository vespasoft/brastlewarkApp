package com.vespadev.brastlewarkapp.data.resources.cache

import com.vespadev.brastlewarkapp.data.models.HeroesResponse

class HeroesCache: Cache<HeroesResponse> {
    var heroes: HeroesResponse = HeroesResponse(listOf())

    override var value: HeroesResponse
        get() = heroes
        set(value) { heroes = value }

    override fun isEmpty(): Boolean =
        heroes.heroes.isEmpty()
}