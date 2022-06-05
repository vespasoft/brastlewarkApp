package com.vespadev.brastlewarkapp.data

import com.vespadev.brastlewarkapp.data.repositories.HeroesRepository
import com.vespadev.brastlewarkapp.data.resources.api.Api
import com.vespadev.brastlewarkapp.data.resources.cache.HeroesCache
import com.vespadev.brastlewarkapp.domain.Result
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class HeroesRepositoryTest {
    private val heroesApi = mockk<Api>(relaxed = true)
    private val heroesCache = mockk<HeroesCache>(relaxed = true)
    private var heroesRepository: HeroesRepository = HeroesRepository(heroesApi, heroesCache)

    @Test
    fun `given not empty heroes then the result should be success`() = runBlocking {
        /* Given */
        coEvery { heroesCache.isEmpty() } returns true
        coEvery { heroesApi.heroes() } returns
            mockk(relaxed = true) {
                every { heroes } returns  listOf(
                    mockk(relaxed = true)
                )
            }
        /* When */
        val result = heroesRepository.heroes("")

        /* Then*/
        result.collect {
            assert(it is Result.Success)
        }
    }

    @Test
    fun `given an exception calling to heroes then the result should be Error`() = runBlocking {
        /* Given */
        coEvery { heroesCache.isEmpty() } returns true
        coEvery { heroesApi.heroes() } throws Exception()
        /* When */
        val result = heroesRepository.heroes("")
        /* Then*/
        result.collect {
            assert(it is Result.Error)
        }
    }

    @Test
    fun `given an exception with cache then the result should be Success`() = runBlocking {
        /* Given */
        coEvery { heroesApi.heroes() } throws Exception()
        coEvery { heroesCache.isEmpty() } returns false
        coEvery { heroesCache.heroes } returns
                mockk(relaxed = true) {
                    every { heroes } returns  listOf(
                        mockk(relaxed = true)
                    )
                }
        /* When */
        val result = heroesRepository.heroes("")
        /* Then*/
        result.collect {
            assert(it is Result.Success)
        }
    }
}