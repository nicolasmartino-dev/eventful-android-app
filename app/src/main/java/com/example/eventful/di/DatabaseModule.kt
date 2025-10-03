package com.example.eventful.di

import android.app.Application
import androidx.room.Room
import com.example.eventful.data.local.EventDao
import com.example.eventful.data.local.EventfulDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideEventfulDatabase(app: Application): EventfulDatabase {
        return Room.databaseBuilder(
            app,
            EventfulDatabase::class.java,
            "eventful_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideEventDao(db: EventfulDatabase): EventDao {
        return db.eventDao()
    }
}
