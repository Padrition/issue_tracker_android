package cz.mendelu.projek.ui.screens.add_issue_screen

import java.io.Serializable

sealed class AddIssueScreenUIState(): Serializable {
    object Loading: AddIssueScreenUIState()
    class Error(var error: AddIssueScreenError): AddIssueScreenUIState()
    class Loaded(var data: AddIssueScreenData): AddIssueScreenUIState()
    class DataChanged(var data: AddIssueScreenData): AddIssueScreenUIState()
    object Added: AddIssueScreenUIState()
    object cannotBeNull: AddIssueScreenUIState()
}