package cz.mendelu.projek.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

const val USER_DATASTORE = "user_data"

val Context.preferenceDataStore : DataStore<Preferences> by preferencesDataStore(name = USER_DATASTORE)


class DataStoreManager(val context: Context) {
    companion object{
        val REFRESH_TOKEN = stringPreferencesKey("REFRESH_TOKEN")
        val ACCESS_TOKEN = stringPreferencesKey("ACCESS_TOKEN")
    }

    val accessTokenFlow: Flow<String?> = context.preferenceDataStore.data.map { preferences ->
        preferences[ACCESS_TOKEN]
    }

    val refreshTokenFlow: Flow<String?> = context.preferenceDataStore.data.map { preferences ->
        preferences[REFRESH_TOKEN]
    }

    suspend fun updateTokens(accessToken: String, refreshToken: String){
        context.preferenceDataStore.edit { preferences ->
            preferences[REFRESH_TOKEN] = refreshToken
            preferences[ACCESS_TOKEN] = accessToken
        }
    }
}