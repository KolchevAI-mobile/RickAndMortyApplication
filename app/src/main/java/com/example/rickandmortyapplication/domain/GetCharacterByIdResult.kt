package com.example.rickandmortyapplication.domain

import com.example.rickandmortyapplication.domain.error.GetCharacterError
import com.example.rickandmortyapplication.domain.model.Character

sealed class GetCharacterByIdResult {
    data class Success(val character: Character) : GetCharacterByIdResult()
    data class Failure(val error: GetCharacterError) : GetCharacterByIdResult()
}
