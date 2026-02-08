package com.example.rickandmortyapplication.ui.characterdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.rickandmortyapplication.domain.model.Character
import com.example.rickandmortyapplication.domain.use_case.GetCharacterByIdUseCase
import com.example.rickandmortyapplication.ui.naviagtion.Screen
import com.example.rickandmortyapplication.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCharacterById: GetCharacterByIdUseCase
) : ViewModel() {

    private val args = savedStateHandle.toRoute<Screen.CharacterDetail>()
    private val characterId: Int = args.id

    private val _state = MutableStateFlow<UiState<Character>>(UiState.Loading)
    val state: StateFlow<UiState<Character>> = _state

    init {
        loadCharacter()
    }

    fun loadCharacter() {
        viewModelScope.launch {
            _state.value = UiState.Loading
            try {
                val character: Character = getCharacterById(characterId)
                _state.value = UiState.Success(character)
            } catch (e: Exception) {
                _state.value = UiState.Error(
                    e.localizedMessage ?: "Не удалось загрузить персонажа"
                )
            }
        }
    }
}
