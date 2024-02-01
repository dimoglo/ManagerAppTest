package net.nomia.common.ui.model

import androidx.paging.compose.LazyPagingItems

sealed interface ItemCollections<T> {
    data class Paging<T : Any>(val value: LazyPagingItems<T>) : ItemCollections<T>
    data class Items<T : Any>(val value: List<T>) : ItemCollections<T>
}

fun <T> ItemCollections<T>.isEmpty(): Boolean =
    when (this) {
        is ItemCollections.Paging -> this.value.itemCount == 0
        is ItemCollections.Items -> this.value.isEmpty()
    }

fun <T> ItemCollections<T>.size(): Int =
    when (this) {
        is ItemCollections.Paging -> this.value.itemCount
        is ItemCollections.Items -> this.value.size
    }

fun <T : Any> itemCollectionsOf(elements: LazyPagingItems<T>) = ItemCollections.Paging(elements)
fun <T : Any> itemCollectionsOf(elements: List<T>) = ItemCollections.Items(elements)
