@file:OptIn(ExperimentalMaterial3Api::class)

package net.nomia.common.ui.composable.draganddrop

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import net.nomia.common.ui.composable.NomiaBasicScrollableScaffold
import net.nomia.common.ui.theme.paddings
import org.burnoutcrew.reorderable.ReorderableLazyListState
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@Composable
fun NomiaDragAndDropScaffold(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    onMove: (Int, Int) -> Unit,
    canDragOver: (Int, Int) -> Boolean,
    onDragEnd: (Int, Int) -> Unit,
    content: LazyListScope.(ReorderableLazyListState) -> Unit
) {

    NomiaBasicScrollableScaffold(
        modifier = modifier,
        title = title,
        navigationIcon = navigationIcon,
        actions = actions,
        snackbarHost = snackbarHost,
    ) {
        val reorderableState = rememberReorderableLazyListState(
            onMove = { from, to -> onMove(from.index, to.index) },
            canDragOver = { draggedOver, dragging -> canDragOver(draggedOver.index, dragging.index) },
            onDragEnd = onDragEnd,
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .padding(contentPadding)
                .reorderable(reorderableState),
            content = { content(reorderableState) },
            state = reorderableState.listState,
            contentPadding = PaddingValues(
                top = it.calculateTopPadding(),
                bottom = MaterialTheme.paddings.medium,
            ),
        )
    }
}
