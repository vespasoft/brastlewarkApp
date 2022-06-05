package com.vespadev.brastlewarkapp.domain.repositories

import com.vespadev.brastlewarkapp.domain.Result
import com.vespadev.brastlewarkapp.domain.entities.HeroEntity
import kotlinx.coroutines.flow.Flow

interface IHeroesRepository {
    fun heroes(params: String?): Flow<Result<List<HeroEntity>>>
}