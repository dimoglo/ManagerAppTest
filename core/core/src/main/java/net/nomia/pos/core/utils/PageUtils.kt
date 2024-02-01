package net.nomia.pos.core.utils

import kotlinx.coroutines.runBlocking
import net.nomia.common.data.model.Page
import net.nomia.common.data.model.PageRequest


inline fun <T> findAllPages(
    pageSize: Long,
    crossinline query: suspend (PageRequest) -> Page<T>,
): Iterator<Page<T>> = object : AbstractIterator<Page<T>>() {
        private var nextPage: Int = 0
        private var hasNext: Boolean = true

        override fun computeNext() {
            if (hasNext) {
                val page = runBlocking {
                    query(PageRequest(page = nextPage, size = pageSize))
                }
                setNext(page)
                if (page.pageNumber + 1 < page.totalPages) {
                    nextPage = page.pageNumber + 1
                } else {
                    hasNext = false
                }
            } else {
                done()
            }
        }
    }
