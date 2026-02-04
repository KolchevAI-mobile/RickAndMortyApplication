package com.example.rickandmortyapplication.domain.repository

import androidx.paging.PagingData
import com.example.rickandmortyapplication.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getAllCharacters(): Flow<PagingData<Character>>
}