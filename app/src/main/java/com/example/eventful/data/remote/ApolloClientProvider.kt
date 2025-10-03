package com.example.eventful.data.remote

import com.apollographql.apollo3.ApolloClient

object ApolloClientProvider {
    private const val BASE_URL = "https://eventful-graphql-api-67457810649.us-central1.run.app/graphql"

    fun provideApolloClient(): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl(BASE_URL)
            .build()
    }
}
