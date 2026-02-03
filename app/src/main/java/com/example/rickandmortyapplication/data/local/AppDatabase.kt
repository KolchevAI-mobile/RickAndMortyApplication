package com.example.rickandmortyapplication.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.rickandmortyapplication.data.local.dao.CharacterDao
import com.example.rickandmortyapplication.data.local.dao.RemoteKeysDao
import com.example.rickandmortyapplication.data.local.entity.CharacterEntity
import com.example.rickandmortyapplication.data.local.entity.RemoteKeysEntity


@Database(
    entities = [CharacterEntity::class, RemoteKeysEntity::class],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase: RoomDatabase() {
    abstract fun characterDao(): CharacterDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}