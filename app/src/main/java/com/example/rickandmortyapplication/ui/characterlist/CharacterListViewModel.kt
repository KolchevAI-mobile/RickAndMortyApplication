package com.example.rickandmortyapplication.ui.characterlist

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.rickandmortyapplication.domain.model.Character
import com.example.rickandmortyapplication.domain.model.CharacterFilter
import com.example.rickandmortyapplication.domain.use_case.GetCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _filters = MutableStateFlow(CharacterFilter(status = null, gender = null))
    val filters: StateFlow<CharacterFilter> = _filters

    val characterPagingFlow: Flow<PagingData<Character>> =
        getCharactersUseCase("", CharacterFilter(null, null))
            .cachedIn(viewModelScope)

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onFilterChange(newFilters: CharacterFilter) {
        _filters.value = newFilters
    }
}