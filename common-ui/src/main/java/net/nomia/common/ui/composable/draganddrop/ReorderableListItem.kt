package net.nomia.common.ui.composable.draganddrop

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import net.nomia.common.ui.composable.draganddrop.ReorderableListItemDefaults.DraggedElevation
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.ReorderableLazyListState

@Composable
fun ReorderableListItem(
    state: ReorderableLazyListState,
    key: Any?,
    content: @Composable (State<Dp>) -> Unit,
) {

    ReorderableItem(state = state, key = key) { isDragging ->
        val elevation = animateDpAsState(if (isDragging) DraggedElevation else 0.dp)
        content(elevation)
    }
}

object ReorderableListItemDefaults {
    val DraggedElevation = 8.dp
}
