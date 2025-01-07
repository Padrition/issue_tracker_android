package cz.mendelu.projek.ui.screens.board_settings

import java.io.Serializable

sealed class BoardSettingsScreenUIState(): Serializable {
    object Idle: BoardSettingsScreenUIState()
    object Loading: BoardSettingsScreenUIState()
    class Loaded(var data: BoardSettingsScreenData): BoardSettingsScreenUIState()
    class Error(var error: BoardSettingScreenError): BoardSettingsScreenUIState()
    object Deleted: BoardSettingsScreenUIState()
}