package com.vespadev.brastlewarkapp.di

import com.vespadev.brastlewarkapp.data.repositories.HeroesRepository
import com.vespadev.brastlewarkapp.data.resources.api.Api
import com.vespadev.brastlewarkapp.data.resources.cache.HeroesCache
import com.vespadev.brastlewarkapp.domain.FlowUseCase
import com.vespadev.brastlewarkapp.domain.entities.HeroEntity
import com.vespadev.brastlewarkapp.domain.repositories.IHeroesRepository
import com.vespadev.brastlewarkapp.domain.usecases.AllHeroesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@Suppress("unused")
@InstallIn(SingletonComponent::class)
class HeroesModule {

    @Provides
    @Singleton
    fun provideHeroesRepository(heroesApi: Api): IHeroesRepository =
        HeroesRepository(heroesApi, HeroesCache())

    @Provides
    @Singleton
    fun provideAllHeroesUseCase(heroesRepository: HeroesRepository): FlowUseCase<String, List<HeroEntity>> =
        AllHeroesUseCase(heroesRepository)

}