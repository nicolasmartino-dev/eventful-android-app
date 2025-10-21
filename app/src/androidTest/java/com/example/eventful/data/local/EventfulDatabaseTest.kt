package com.example.eventful.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.eventful.data.mapper.EventMapper
import com.example.eventful.domain.model.Event
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class EventfulDatabaseTest {

    private lateinit var database: EventfulDatabase
    private lateinit var eventDao: EventDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            EventfulDatabase::class.java
        ).allowMainThreadQueries().build()
        eventDao = database.eventDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndGetEvents() = runTest {
        // Given
        val events = listOf(
            Event(
                id = "1",
                title = "Test Event 1",
                startTime = "2024-01-01T10:00:00Z",
                description = "Test Description 1"
            ),
            Event(
                id = "2",
                title = "Test Event 2",
                startTime = "2024-01-02T10:00:00Z",
                description = "Test Description 2"
            )
        )
        val eventEntities = events.map { EventMapper.run { it.toEventEntity() } }

        // When
        eventDao.insertEvents(eventEntities)
        val retrievedEvents = eventDao.getEvents().first().map { EventMapper.run { it.toEvent() } }

        // Then
        assertEquals(2, retrievedEvents.size)
        assertEquals(events[0], retrievedEvents[0])
        assertEquals(events[1], retrievedEvents[1])
    }

    @Test
    fun insertAndGetEventById() = runTest {
        // Given
        val event = Event(
            id = "test-id",
            title = "Test Event",
            startTime = "2024-01-01T10:00:00Z",
            description = "Test Description"
        )
        val eventEntity = EventMapper.run { event.toEventEntity() }

        // When
        eventDao.insertEvents(listOf(eventEntity))
        val retrievedEvent = eventDao.getEventById("test-id")

        // Then
        assertNotNull(retrievedEvent)
        assertEquals(event, EventMapper.run { retrievedEvent.toEvent() })
    }

    @Test
    fun getEventByIdReturnsNullForNonExistentEvent() = runTest {
        // When
        val retrievedEvent = eventDao.getEventById("non-existent-id")

        // Then
        assertNull(retrievedEvent)
    }

    @Test
    fun clearEventsRemovesAllEvents() = runTest {
        // Given
        val events = listOf(
            Event(id = "1", title = "Event 1", startTime = "2024-01-01T10:00:00Z"),
            Event(id = "2", title = "Event 2", startTime = "2024-01-02T10:00:00Z")
        )
        val eventEntities = events.map { EventMapper.run { it.toEventEntity() } }
        eventDao.insertEvents(eventEntities)

        // When
        eventDao.clearEvents()
        val retrievedEvents = eventDao.getEvents().first()

        // Then
        assertTrue(retrievedEvents.isEmpty())
    }

    @Test
    fun insertEventsWithSameIdReplacesExisting() = runTest {
        // Given
        val originalEvent = Event(
            id = "1",
            title = "Original Event",
            startTime = "2024-01-01T10:00:00Z",
            description = "Original Description"
        )
        val updatedEvent = Event(
            id = "1",
            title = "Updated Event",
            startTime = "2024-01-01T10:00:00Z",
            description = "Updated Description"
        )

        val originalEntity = EventMapper.run { originalEvent.toEventEntity() }
        val updatedEntity = EventMapper.run { updatedEvent.toEventEntity() }

        // When
        eventDao.insertEvents(listOf(originalEntity))
        eventDao.insertEvents(listOf(updatedEntity))
        val retrievedEvents = eventDao.getEvents().first().map { EventMapper.run { it.toEvent() } }

        // Then
        assertEquals(1, retrievedEvents.size)
        assertEquals(updatedEvent, retrievedEvents[0])
    }
}
