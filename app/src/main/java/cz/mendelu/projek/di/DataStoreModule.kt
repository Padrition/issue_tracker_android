package cz.mendelu.projek.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import cz.mendelu.projek.utils.DataStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

const val USER_DATASTORE = "user_data"

private val Context.preferenceDataStore by preferencesDataStore(name = USER_DATASTORE)


@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStoreManager {
        return DataStoreManager(context)
    }

}