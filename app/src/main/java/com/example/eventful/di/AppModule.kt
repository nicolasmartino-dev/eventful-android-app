package com.example.eventful.di

import com.apollographql.apollo3.ApolloClient
import com.example.eventful.data.local.EventDao
import com.example.eventful.data.remote.ApolloClientProvider
import com.example.eventful.data.repository.EventRepositoryImpl
import com.example.eventful.domain.repository.EventRepository
import com.example.eventful.domain.usecase.GetEventDetailsUseCase
import com.example.eventful.domain.usecase.GetEventsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApolloClient(): ApolloClient {
        return ApolloClientProvider.provideApolloClient()
    }

    @Provides
    @Singleton
    fun provideEventRepository(
        apolloClient: ApolloClient,
        eventDao: EventDao
    ): EventRepository {
        return EventRepositoryImpl(apolloClient, eventDao)
    }

    @Provides
    @Singleton
    fun provideGetEventsUseCase(repository: EventRepository): GetEventsUseCase {
        return GetEventsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetEventDetailsUseCase(repository: EventRepository): GetEventDetailsUseCase {
        return GetEventDetailsUseCase(repository)
    }
}
