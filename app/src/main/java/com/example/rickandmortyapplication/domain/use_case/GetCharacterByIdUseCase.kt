package com.example.rickandmortyapplication.domain.use_case

import com.example.rickandmortyapplication.domain.repository.CharacterRepository
import javax.inject.Inject
import com.example.rickandmortyapplication.domain.model.Character

class GetCharacterByIdUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(id: Int): Character {
        return repository.getCharacterById(id)
    }
}
