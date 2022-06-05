package com.vespadev.brastlewarkapp.domain.usecases

import com.vespadev.brastlewarkapp.domain.FlowUseCase
import com.vespadev.brastlewarkapp.domain.Result
import com.vespadev.brastlewarkapp.domain.entities.HeroEntity
import com.vespadev.brastlewarkapp.domain.repositories.IHeroesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AllHeroesUseCase @Inject constructor(
    private val heroesRepository: IHeroesRepository
) : FlowUseCase<String?, List<HeroEntity>>(Dispatchers.IO){

    override fun execute(parameters: String?): Flow<Result<List<HeroEntity>>> =
        heroesRepository.heroes(parameters)

}