package com.eventful.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>): Unit

    @Query("SELECT * FROM events")
    fun getEvents(): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE id = :id")
    suspend fun getEventById(id: String): EventEntity?

    @Query("SELECT * FROM events WHERE id = :id")
    fun getEventByIdFlow(id: String): Flow<EventEntity?>

    @Query("DELETE FROM events")
    suspend fun clearEvents(): Unit
}
