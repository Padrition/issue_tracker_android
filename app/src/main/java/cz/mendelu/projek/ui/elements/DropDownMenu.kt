package cz.mendelu.projek.ui.elements

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cz.mendelu.projek.R
import cz.mendelu.projek.communication.board.Category
import cz.mendelu.projek.utils.parseColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownCategoryMenu(
    options: List<Category>,
    selectedOption: Category,
    onOptionSelected: (Category) -> Unit,
    modifier: Modifier = Modifier,
){
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = {expanded = !expanded}
    ) {
        OutlinedTextField(
            value = selectedOption.name ?: stringResource(R.string.category_default_name),
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.issue_status)) },
            trailingIcon = {
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = parseColor(selectedOption.color ?: "#123456"),
                unfocusedBorderColor = parseColor(selectedOption.color ?: "#123456"),
                focusedTextColor = parseColor(selectedOption.color ?: "#123456"),
                unfocusedTextColor = parseColor(selectedOption.color ?: "#123456"),
            ),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            modifier = Modifier.fillMaxWidth(),
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { option ->
                if (option.name != null){
                    DropdownMenuItem(
                        text = { Text(option.name!!) },
                        colors = MenuDefaults.itemColors(
                            disabledTextColor = parseColor(option.color ?: "#123456"),
                            textColor = parseColor(option.color ?: "#123456"),
                        ),
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownPriorityMenu(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
){
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = {expanded = !expanded}
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.issue_status)) },
            trailingIcon = {
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            modifier = Modifier.fillMaxWidth(),
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}