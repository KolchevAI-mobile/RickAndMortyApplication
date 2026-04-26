package com.example.rickandmortyapplication.domain.model

data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val image: String,
    val type: String = "",
    val gender: String = "",
    val originName: String = "",
    val locationName: String = ""
)
