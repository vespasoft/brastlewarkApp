package com.vespadev.brastlewarkapp.data.repositories

import com.vespadev.brastlewarkapp.data.animals
import com.vespadev.brastlewarkapp.data.filterHeroes
import com.vespadev.brastlewarkapp.data.mapToHeroEntity
import com.vespadev.brastlewarkapp.data.models.HeroesResponse
import com.vespadev.brastlewarkapp.data.resolveError
import com.vespadev.brastlewarkapp.data.resources.api.Api
import com.vespadev.brastlewarkapp.data.resources.cache.Cache
import com.vespadev.brastlewarkapp.domain.Result
import com.vespadev.brastlewarkapp.domain.entities.HeroEntity
import com.vespadev.brastlewarkapp.domain.repositories.IHeroesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class HeroesRepository @Inject constructor(
    private val heroesApi: Api,
    private val heroesCache: Cache<HeroesResponse>
): IHeroesRepository {

    override fun heroes(params: String?): Flow<Result<List<HeroEntity>>> = flow {
        if (heroesCache.isEmpty()) {
            try {
                var response = heroesApi.heroes()
                response.heroes.map { it.thumbnail = animals().random() }
                heroesCache.value = response
                emit(Result.Success(response.heroes.filterHeroes(params).mapToHeroEntity()))
            } catch (exception: Exception) {
                emit(Result.Error(message = exception.resolveError().message.toString()))
            }
        } else {
            emit(Result.Success(heroesCache.value.heroes.filterHeroes(params).mapToHeroEntity()))
        }
    }
}