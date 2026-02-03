package com.example.rickandmortyapplication.di

import android.app.Application
import androidx.room.Room
import com.example.rickandmortyapplication.data.local.AppDatabase
import com.example.rickandmortyapplication.data.local.dao.CharacterDao
import com.example.rickandmortyapplication.data.local.dao.RemoteKeysDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "rick_and_morty_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideCharacterDao(db: AppDatabase): CharacterDao {
        return db.characterDao()
    }

    @Provides
    @Singleton
    fun provideRemoteKeysDao(db: AppDatabase): RemoteKeysDao {
        return db.remoteKeysDao()
    }
}