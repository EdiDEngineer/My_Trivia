package edu.utap.mytrivia.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories
import edu.utap.mytrivia.data.Repository
import edu.utap.mytrivia.data.RepositoryImpl

@Module
@InstallIn(ActivityComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindRepository(repository: RepositoryImpl): Repository

}