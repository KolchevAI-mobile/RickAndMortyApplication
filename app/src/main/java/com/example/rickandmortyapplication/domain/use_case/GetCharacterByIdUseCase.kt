package com.example.rickandmortyapplication.domain.use_case

import com.example.rickandmortyapplication.domain.GetCharacterByIdResult
import com.example.rickandmortyapplication.domain.repository.CharacterRepository
import javax.inject.Inject

class GetCharacterByIdUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(id: Int): GetCharacterByIdResult = repository.getCharacterById(id)
}
