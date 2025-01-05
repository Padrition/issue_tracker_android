package cz.mendelu.projek.di

import cz.mendelu.projek.communication.auth.AuthRemoteRepositoryImpl
import cz.mendelu.projek.utils.AuthHelper
import cz.mendelu.projek.utils.DataStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthHelperModule {

    @Provides
    @Singleton
    fun provideAuthHelper(
        dataStoreManager: DataStoreManager,
        repository: AuthRemoteRepositoryImpl,
    ): AuthHelper{
        return AuthHelper(
            dataStoreManager, repository
        )
    }
}