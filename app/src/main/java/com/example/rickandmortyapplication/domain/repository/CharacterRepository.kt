package com.example.rickandmortyapplication.domain.repository

import androidx.paging.PagingData
import com.example.rickandmortyapplication.domain.model.Character
import com.example.rickandmortyapplication.domain.model.CharacterFilter
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getAllCharacters(): Flow<PagingData<Character>>

    suspend fun getCharacterById(id: Int): Character

    fun getCharacters(
        query: String,
        filters: CharacterFilter
    ): Flow<PagingData<Character>>

}