package com.vespadev.brastlewarkapp.domain

import com.vespadev.brastlewarkapp.data.repositories.HeroesRepository
import com.vespadev.brastlewarkapp.domain.usecases.AllHeroesUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Test

class AllHeroesUseCaseTest {
    private val heroesRepository = mockk<HeroesRepository>()
    private var allHeroesUseCase: AllHeroesUseCase = AllHeroesUseCase(heroesRepository)

    @Test
    fun `given not empty heroes without params then the result should be success`() = runBlocking {
        /* Given */
        val params = ""
        coEvery { heroesRepository.heroes(params) } returns flowOf(
            Result.Success(
                listOf(
                    mockk(relaxed = true),
                    mockk(relaxed = true),
                    mockk(relaxed = true)
                )
            )
        )

        /* When */
        val useCaseInvoked = allHeroesUseCase.invoke(params)

        /* Then */
        useCaseInvoked.collect {
            assert(it is Result.Success)
        }
    }
}