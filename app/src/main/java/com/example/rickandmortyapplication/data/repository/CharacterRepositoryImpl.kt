package com.example.rickandmortyapplication.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.rickandmortyapplication.data.local.AppDatabase
import com.example.rickandmortyapplication.data.mapper.toCharacter
import com.example.rickandmortyapplication.data.mapper.toCharacterEntity
import com.example.rickandmortyapplication.data.remote.CharacterRemoteMediator
import com.example.rickandmortyapplication.data.remote.RickAndMortyApi
import com.example.rickandmortyapplication.domain.model.Character
import com.example.rickandmortyapplication.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val api: RickAndMortyApi,
    private val database: AppDatabase
) : CharacterRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getAllCharacters(): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = CharacterRemoteMediator(database, api),
            pagingSourceFactory = { database.characterDao().getPagingSource() }
        ).flow.map { pagingData ->
            pagingData.map { it.toCharacter() }
        }
    }

    override suspend fun getCharacterById(id: Int): Character {
        val dto = api.getCharacterById(id)
        val entity = dto.toCharacterEntity()
        return entity.toCharacter()
    }

}