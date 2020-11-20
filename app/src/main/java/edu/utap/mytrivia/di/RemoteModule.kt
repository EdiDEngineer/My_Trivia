package edu.utap.mytrivia.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import edu.utap.mytrivia.data.remote.TriviaApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RemoteModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class InterceptorOkHttpClient

    @Singleton
    @Provides
    fun provideTriviaApi(
        @InterceptorOkHttpClient okHttpClient: OkHttpClient
    ): TriviaApi {
        return TriviaApi.create(okHttpClient)
    }

    @InterceptorOkHttpClient
    @Provides
    fun provideInterceptorOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().retryOnConnectionFailure(true)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

}