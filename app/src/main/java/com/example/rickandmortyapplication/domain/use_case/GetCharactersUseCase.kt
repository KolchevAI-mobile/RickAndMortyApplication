package com.example.rickandmortyapplication.domain.use_case

import androidx.paging.PagingData
import androidx.room.Query
import com.example.rickandmortyapplication.domain.repository.CharacterRepository
import com.example.rickandmortyapplication.domain.model.Character
import com.example.rickandmortyapplication.domain.model.CharacterFilter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCharactersUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    operator fun invoke(query: String, filters: CharacterFilter): Flow<PagingData<Character>> {
        return repository.getAllCharacters()
    }
}