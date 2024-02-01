package net.nomia.common.ui.composable

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import net.nomia.common.ui.previews.ThemePreviews
import net.nomia.common.ui.theme.NomiaThemeMaterial3
import net.nomia.common.ui.theme.model.ArrowDropUp
import net.nomia.common.ui.theme.paddings

@Composable
fun <T> NomiaDropdownMenu(
    modifier: Modifier = Modifier,
    items: List<T>,
    selectedItem: T? = null,
    onItemClick: (item: T) -> Unit,
    convertToString: @Composable (item: T) -> String,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    onExpanded: (() -> Unit)? = null,
    itemText: @Composable (item: T) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var itemSize by remember { mutableStateOf(Size.Zero) }
    var selectedValue by remember { mutableStateOf<T?>(null) }

    if (selectedItem != null && items.contains(selectedItem)) {
        selectedValue = selectedItem
    } else if (!items.contains(selectedValue)) {
        selectedValue = null
    }

    val trailingIcon: @Composable () -> Unit = {
        Icon(
            imageVector = if (expanded) {
                ArrowDropUp
            } else {
                Icons.Filled.ArrowDropDown
            },
            contentDescription = null
        )
    }
    val interactionSource = remember { MutableInteractionSource() }

    if (interactionSource.collectIsPressedAsState().value) {
        expanded = true
        onExpanded?.invoke()
    }

    Column(
        modifier = Modifier.onGloballyPositioned { layoutCoordinates ->
            itemSize = layoutCoordinates.size.toSize()
        }
    ) {
        OutlinedTextField(
            value = selectedValue?.let { convertToString(it) } ?: "",
            onValueChange = {},
            modifier = modifier,
            readOnly = true,
            trailingIcon = trailingIcon,
            interactionSource = interactionSource,
            label = label,
            enabled = enabled
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(with(LocalDensity.current) { itemSize.width.toDp() })
        ) {
            if (items.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = MaterialTheme.paddings.large),
                    contentAlignment = Alignment.Center
                ) {
                    NomiaSpinner(
                        size = NomiaSpinnerDefaults.dropdownSize,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = { itemText(item) },
                        onClick = {
                            expanded = false
                            selectedValue = item
                            onItemClick(item)
                        },
                    )
                }
            }
        }
    }
}

@Composable
@ThemePreviews
fun NomiaDropdownMenuPreview() {
    NomiaThemeMaterial3 {
        Surface {
            Box(
                modifier = Modifier
                    .height(400.dp)
                    .padding(horizontal = 24.dp)
            ) {
                NomiaDropdownMenu(
                    items = listOf("Hello, Hi, Nice"),
                    onItemClick = {},
                    convertToString = { it },
                    label = { Text(text = "label") }
                ) {
                    Text(text = it)
                }
            }
        }
    }
}
