package com.example.rickandmortyapplication.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharacterResponseDto(
    @SerialName("info") val info: InfoDto,
    @SerialName("results") val results: List<CharacterDto>
)
