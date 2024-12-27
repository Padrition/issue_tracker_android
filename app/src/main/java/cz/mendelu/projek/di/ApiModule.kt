package cz.mendelu.projek.di

import cz.mendelu.projek.communication.auth.AuthAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideAuthAPI(retrofit: Retrofit):AuthAPI{
        return retrofit.create(AuthAPI::class.java)
    }

}