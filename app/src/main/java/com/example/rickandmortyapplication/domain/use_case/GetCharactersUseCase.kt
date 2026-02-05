package com.example.rickandmortyapplication.domain.use_case

import androidx.paging.PagingData
import com.example.rickandmortyapplication.domain.repository.CharacterRepository
import com.example.rickandmortyapplication.domain.model.Character
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCharactersUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    operator fun invoke(): Flow<PagingData<Character>> {
        return repository.getAllCharacters()
    }
}