package app.kingmojang.fixture

import org.springframework.data.domain.PageRequest

const val PAGE = 0
const val DEFAULT_PAGINATION_SIZE = 10
const val MEMO_PAGINATION_SIZE = 10
const val COMMENT_PAGINATION_SIZE = 20

fun createPageRequest(page: Int = PAGE, size: Int = DEFAULT_PAGINATION_SIZE): PageRequest {
    return PageRequest.of(page, size)
}