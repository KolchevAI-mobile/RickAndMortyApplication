package com.example.rickandmortyapplication.data.remote


import com.example.rickandmortyapplication.data.remote.dto.CharacterResponseDto
import retrofit2.http.GET
import retrofit2.http.Query


interface RickAndMortyApi {
    @GET("character")
    suspend fun getAllCharacters(
        @Query("page") page: Int
    ): CharacterResponseDto
}