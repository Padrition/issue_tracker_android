package cz.mendelu.projek.ui.screens.issue_screen

import java.io.Serializable

sealed class IssueScreenUIState(): Serializable {
    object Loading: IssueScreenUIState()
    class Loaded(var data: IssueScreenData): IssueScreenUIState()
    class Error(var error: IssueScreenError): IssueScreenUIState()
    class DataChanged(var data: IssueScreenData):IssueScreenUIState()
}