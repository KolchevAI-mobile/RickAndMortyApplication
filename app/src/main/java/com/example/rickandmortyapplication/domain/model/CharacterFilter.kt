package com.example.rickandmortyapplication.domain.model

data class CharacterFilter(
    val status: CharacterStatusFilter?,
    val gender: CharacterGenderFilter?
)