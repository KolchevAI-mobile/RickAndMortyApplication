package com.example.rickandmortyapplication.data.mapper

import com.example.rickandmortyapplication.domain.model.CharacterFilter
import com.example.rickandmortyapplication.domain.model.CharacterGenderFilter
import com.example.rickandmortyapplication.domain.model.CharacterStatusFilter

data class ApiCharacterFilter(
    val status: String?,
    val gender: String?
)

fun CharacterFilter.toApiQuery(): ApiCharacterFilter = ApiCharacterFilter(
    status = status?.toApiValue(),
    gender = gender?.toApiValue()
)

private fun CharacterStatusFilter.toApiValue(): String = when (this) {
    CharacterStatusFilter.ALIVE -> "alive"
    CharacterStatusFilter.DEAD -> "dead"
}

private fun CharacterGenderFilter.toApiValue(): String = when (this) {
    CharacterGenderFilter.MALE -> "male"
    CharacterGenderFilter.FEMALE -> "female"
}
