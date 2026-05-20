package com.example.rickandmortyapplication.data.remote

import com.example.rickandmortyapplication.data.remote.dto.CharacterResponseDto
import com.example.rickandmortyapplication.data.remote.dto.InfoDto
import retrofit2.HttpException
import kotlin.coroutines.cancellation.CancellationException

internal suspend fun RickAndMortyApi.loadCharacterPage(
    page: Int,
    name: String? = null,
    status: String? = null,
    gender: String? = null
): CharacterResponseDto {
    return try {
        getAllCharacters(page = page, name = name, status = status, gender = gender)
    } catch (e: CancellationException) {
        throw e
    } catch (e: HttpException) {
        if (e.code() == 404) emptyCharacterPage() else throw e
    }
}

internal fun emptyCharacterPage(): CharacterResponseDto =
    CharacterResponseDto(
        info = InfoDto(count = 0, pages = 0, next = null, prev = null),
        results = emptyList()
    )

internal fun pageKeys(page: Int, hasNextPage: Boolean): Pair<Int?, Int?> {
    val prevKey = if (page == 1) null else page - 1
    val nextKey = if (hasNextPage) page + 1 else null
    return prevKey to nextKey
}
