package edu.utap.mytrivia.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import edu.utap.mytrivia.data.local.MyTriviaDatabase
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object LocalModule {

    @Singleton
    @Provides
    fun provideTriviaDatabase(
        application: Application
    ): MyTriviaDatabase {
        return MyTriviaDatabase.getDatabaseInstance(application)
    }

}