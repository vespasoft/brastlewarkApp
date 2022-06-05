package com.vespadev.brastlewarkapp.ui

import com.vespadev.brastlewarkapp.MainCoroutineRule
import com.vespadev.brastlewarkapp.domain.Result
import com.vespadev.brastlewarkapp.domain.usecases.AllHeroesUseCase
import com.vespadev.brastlewarkapp.ui.heroes.HeroesViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HeroesViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainCoroutineRule()

    private val allHeroesUseCase = mockk<AllHeroesUseCase>()
    private lateinit var heroesViewModel: HeroesViewModel

    @Before
    fun setup() = runBlocking {
        coEvery { allHeroesUseCase.invoke(null) } returns
                flowOf(Result.Success(mockk(relaxed = true)))

        heroesViewModel = HeroesViewModel(allHeroesUseCase)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `given heroes flow success then UIState should be success`() =  runTest {
        /* Given */
        /* When */
        heroesViewModel = HeroesViewModel(allHeroesUseCase)

        /* When */
        val uiState = heroesViewModel.uiState.first()
        Assert.assertTrue(uiState is UIState.Success)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `given params to pass as arguments then UIState Result size should be 2`() =  runTest {
        /* Given */
        coEvery { allHeroesUseCase.invoke("Malbin") } returns
                flowOf(Result.Success(listOf(
                    mockk(relaxed = true) {
                        every { name } returns "Malbin Chromerocket"
                    },
                    mockk(relaxed = true) {
                        every { name } returns "Malbin Magnaweaver"
                    }
                )))
        /* When */
        heroesViewModel.onSearchOnChange("Malbin")

        /* When */
        val uiState = heroesViewModel.uiState.first() as UIState.Success
        Assert.assertTrue(uiState.data.size == 2)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `given heroes flow error then UIState should be error`() =  runTest {
        /* Given */
        coEvery { allHeroesUseCase.invoke(null) } returns
                flowOf(Result.Error(message = "This is a generic error!"))
        /* When */
        heroesViewModel = HeroesViewModel(allHeroesUseCase)

        /* When */
        val uiState = heroesViewModel.uiState.first()
        Assert.assertTrue(uiState is UIState.Error)
    }
}