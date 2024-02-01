package net.nomia.common.ui.model

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items

@ExperimentalFoundationApi
fun <T : Any> LazyGridScope.items(
    lazyPagingItems: LazyPagingItems<T>,
    itemContent: @Composable LazyGridItemScope.(value: T?) -> Unit,
) {
    items(count = lazyPagingItems.itemCount) { index ->
        itemContent(lazyPagingItems[index])
    }
}

fun <T : Any> LazyListScope.items(
    itemCollections: ItemCollections<T>,
    key: ((item: T) -> Any)? = null,
    itemContent: @Composable LazyItemScope.(value: T?) -> Unit,
) {
    when (itemCollections) {
        is ItemCollections.Paging -> items(
            items = itemCollections.value,
            key = key,
            itemContent = itemContent
        )

        is ItemCollections.Items -> items(
            items = itemCollections.value,
            key = key,
            itemContent = itemContent
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun <T : Any> LazyGridScope.items(
    itemCollections: ItemCollections<T>,
    itemContent: @Composable LazyGridItemScope.(value: T?) -> Unit,
) {
    when (itemCollections) {
        is ItemCollections.Paging -> items(
            lazyPagingItems = itemCollections.value,
            itemContent = itemContent
        )

        is ItemCollections.Items -> items(
            items = itemCollections.value,
            itemContent = itemContent
        )
    }
}

fun String.lastIndexOfUppercaseLetter(): Int {
    var occurrenceIndex = this.length
    this.forEachIndexed { index, ch ->
        if (Character.isUpperCase(ch)) {
            occurrenceIndex = index
        }
    }
    return occurrenceIndex
}
