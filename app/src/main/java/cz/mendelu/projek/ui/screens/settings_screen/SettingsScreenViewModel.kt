package cz.mendelu.projek.ui.screens.settings_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.projek.constants.Languages
import cz.mendelu.projek.utils.DataStoreManager
import cz.mendelu.projek.utils.setLocale
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    @ApplicationContext private val context: Context
): ViewModel() {

    private val _uiState: MutableStateFlow<SettingsScreenUIState> =
        MutableStateFlow(value = SettingsScreenUIState.Idle)
    val uiState : StateFlow<SettingsScreenUIState> get() = _uiState.asStateFlow()

    private val defaultLanguage: Languages = Languages.EN
    private val _language = MutableStateFlow(defaultLanguage)
    val languages: StateFlow<Languages> = _language.asStateFlow()

    init {
        viewModelScope.launch {
            val language = dataStoreManager.language.first()
            _language.value = language ?: Languages.EN
            context.setLocale(language ?: Languages.EN)
        }
    }

    fun updateLanguage(language: Languages){
        viewModelScope.launch {
            dataStoreManager.updateLanguage(language)
            _language.value = language
            context.setLocale(language)
        }
    }

    fun logOut(){
        viewModelScope.launch {
            dataStoreManager.emptyTokens()

            _uiState.update {
                SettingsScreenUIState.LoggedOut
            }
        }
    }

}