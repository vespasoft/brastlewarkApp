package com.vespadev.brastlewarkapp.ui.heroes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vespadev.brastlewarkapp.domain.entities.HeroEntity
import com.vespadev.brastlewarkapp.domain.usecases.AllHeroesUseCase
import com.vespadev.brastlewarkapp.ui.UIState
import com.vespadev.brastlewarkapp.ui.toUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HeroesViewModel @Inject constructor(
    private val allHeroesUseCase: AllHeroesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState<List<HeroEntity>>>(UIState.Idle)
    val uiState: StateFlow<UIState<List<HeroEntity>>> = _uiState

    private val _expandedHeroIdsList = MutableStateFlow(listOf<String>())
    val expandedHeroIdsList: StateFlow<List<String>> get() = _expandedHeroIdsList

    init {
        fetchData()
    }

    fun onCardArrowClicked(cardId: String) {
        _expandedHeroIdsList.value = _expandedHeroIdsList.value.toMutableList().also { list ->
            if (list.contains(cardId)) list.remove(cardId) else list.add(cardId)
        }
    }

    fun onSearchOnChange(newValue: String) {
        viewModelScope.launch {
            allHeroesUseCase.invoke(newValue)
                .map { it.toUIState() }
                .collect {
                    _uiState.value = it
                }
        }
    }

    private fun fetchData(params: String? = null) {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            allHeroesUseCase.invoke(params)
                .map { it.toUIState() }
                .collect {
                    _uiState.value = it
                }
        }
    }
}