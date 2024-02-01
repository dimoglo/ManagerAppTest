package net.nomia.common.ui.composable

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.nomia.common.ui.composable.TabRowDefaults.tabHeight
import net.nomia.common.ui.composable.TabRowDefaults.tabWidth
import net.nomia.common.ui.theme.model.disableable

@Composable
fun NomiaScrollableTabRow(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    containerColor: Color = TabRowDefaults.containerColor,
    contentColor: Color = TabRowDefaults.contentColor,
    indicator: @Composable (tabPositions: List<TabPosition>) -> Unit = @Composable { tabPositions ->
        TabRowDefaults.Indicator(
            Modifier
                .tabIndicatorOffset(tabPositions[selectedTabIndex])
                .clip(
                    shape = MaterialTheme.shapes.extraSmall.copy(
                        bottomEnd = CornerSize(0.dp), bottomStart = CornerSize(0.dp)
                    )
                )
        )
    },
    divider: @Composable () -> Unit = @Composable { },
    tabs: @Composable () -> Unit,
) {
    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier,
        edgePadding = 0.dp,
        containerColor = containerColor,
        contentColor = contentColor,
        indicator = indicator,
        divider = divider,
        tabs = tabs
    )
}

@Composable
fun NomiaTextTab(
    modifier: Modifier = Modifier,
    selected: Boolean,
    enabled: Boolean = true,
    text: String,
    onClick: () -> Unit,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    NomiaTab(
        modifier = modifier,
        selected = selected,
        enabled = enabled,
        onClick = onClick,
        interactionSource = interactionSource,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.disableable(enabled),
        )
    }
}

@Composable
fun NomiaTab(
    modifier: Modifier = Modifier,
    selected: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit,
) {
    Tab(
        modifier = Modifier
            .width(tabWidth)
            .height(tabHeight)
            .then(modifier),
        selected = selected,
        enabled = enabled,
        unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        interactionSource = interactionSource,
        onClick = onClick,
    ) {
        content()
    }
}

object TabRowDefaults {
    val tabWidth = 140.dp
    val tabHeight = 48.dp
}
