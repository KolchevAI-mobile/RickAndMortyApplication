package com.example.rickandmortyapplication.domain.repository

import com.example.rickandmortyapplication.domain.GetCharacterByIdResult

interface CharacterRepository {
    suspend fun getCharacterById(id: Int): GetCharacterByIdResult
}