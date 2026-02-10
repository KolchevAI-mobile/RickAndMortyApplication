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
import com.example.rickandmortyapplication.domain.model.CharacterFilter
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

    override fun getCharacters(
        query: String,
        filters: CharacterFilter
    ): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                object : androidx.paging.PagingSource<Int, Character>() {

                    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
                        return try {
                            val page = params.key ?: 1

                            val response = api.getAllCharacters(
                                page = page,
                                name = query.ifBlank { null },
                                status = filters.status,
                                gender = filters.gender
                            )

                            val characters = response.results.map { dto ->
                                dto.toCharacterEntity().toCharacter()
                            }

                            val nextKey = if (response.info.next == null) null else page + 1
                            val prevKey = if (page == 1) null else page - 1

                            LoadResult.Page(
                                data = characters,
                                prevKey = prevKey,
                                nextKey = nextKey
                            )
                        } catch (e: Exception) {
                            LoadResult.Error(e)
                        }
                    }

                    override fun getRefreshKey(state: androidx.paging.PagingState<Int, Character>): Int? {
                        return state.anchorPosition?.let { anchorPosition ->
                            val anchorPage = state.closestPageToPosition(anchorPosition)
                            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
                        }
                    }
                }
            }
        ).flow
    }

}