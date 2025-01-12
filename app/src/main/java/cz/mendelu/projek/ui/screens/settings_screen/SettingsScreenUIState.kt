package cz.mendelu.projek.ui.screens.settings_screen

import java.io.Serializable

sealed class SettingsScreenUIState(): Serializable {
    object Idle: SettingsScreenUIState()
    object LoggedOut: SettingsScreenUIState()
}