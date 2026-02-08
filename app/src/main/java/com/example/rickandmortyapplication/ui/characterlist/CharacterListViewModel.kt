package com.example.rickandmortyapplication.ui.characterlist

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.rickandmortyapplication.domain.model.Character
import com.example.rickandmortyapplication.domain.use_case.GetCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase
) : ViewModel() {

    val characterPagingFlow: Flow<PagingData<Character>> = getCharactersUseCase()
}