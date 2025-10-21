package com.example.eventful.di

import com.apollographql.apollo.ApolloClient
import com.example.eventful.data.local.EventDao
import com.example.eventful.data.local.EventLocalDataSource
import com.example.eventful.data.local.EventLocalDataSourceImpl
import com.example.eventful.data.remote.ApolloClientProvider
import com.example.eventful.data.remote.EventRemoteDataSource
import com.example.eventful.data.remote.EventRemoteDataSourceImpl
import com.example.eventful.data.repository.EventRepositoryImpl
import com.example.eventful.domain.repository.EventRepository
import com.example.eventful.domain.usecase.GetEventDetailsUseCase
import com.example.eventful.domain.usecase.GetEventsUseCase
import com.example.eventful.domain.usecase.LoadMoreEventsUseCase
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
    fun provideEventRemoteDataSource(apolloClient: ApolloClient): EventRemoteDataSource {
        return EventRemoteDataSourceImpl(apolloClient)
    }

    @Provides
    @Singleton
    fun provideEventLocalDataSource(eventDao: EventDao): EventLocalDataSource {
        return EventLocalDataSourceImpl(eventDao)
    }

    @Provides
    @Singleton
    fun provideEventRepository(
        remoteDataSource: EventRemoteDataSource,
        localDataSource: EventLocalDataSource
    ): EventRepository {
        return EventRepositoryImpl(remoteDataSource, localDataSource)
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

    @Provides
    @Singleton
    fun provideLoadMoreEventsUseCase(repository: EventRepository): LoadMoreEventsUseCase {
        return LoadMoreEventsUseCase(repository)
    }
}
