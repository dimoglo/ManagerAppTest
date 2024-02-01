package net.nomia.core.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    minWidth: Dp,
    contentPaddingValues: PaddingValues = PaddingValues(),
    contents: List<@Composable () -> Unit>,
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxWidth()
    ) {

        val countColumn by remember(this.maxWidth) {
            derivedStateOf {
                (this.maxWidth / minWidth).toInt().takeIf { it > 0 } ?: 1
            }
        }

        val modifierForItem = remember(countColumn) {
            if (countColumn == 1) {
                Modifier.fillMaxWidth()
            } else {
                Modifier.width(this.maxWidth / countColumn)
            }
        }

        val listItemsContent = contents
            .withIndex()
            .groupBy { it.index % countColumn }
            .map { entryIndexedValue -> entryIndexedValue.value.map { it.value } }

        Row(modifier = Modifier.fillMaxWidth()) {

            listItemsContent.forEach { itemRow ->

                Column(modifier = modifierForItem) {

                    itemRow.forEach { itemColumn ->

                        Box(modifier = Modifier.padding(contentPaddingValues)) {
                            itemColumn()
                        }
                    }
                }
            }
        }
    }
}
