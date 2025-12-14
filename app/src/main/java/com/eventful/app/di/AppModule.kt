package com.eventful.app.di

import com.apollographql.apollo.ApolloClient
import com.eventful.app.data.local.EventDao
import com.eventful.app.data.local.EventLocalDataSource
import com.eventful.app.data.remote.ApolloClientProvider
import com.eventful.app.data.remote.EventRemoteDataSource
import com.eventful.app.data.repository.EventRepositoryImpl
import com.eventful.app.domain.repository.EventRepository
import com.eventful.app.domain.usecase.GetEventDetailsUseCase
import com.eventful.app.domain.usecase.GetEventsUseCase
import com.eventful.app.domain.usecase.LoadMoreEventsUseCase
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
