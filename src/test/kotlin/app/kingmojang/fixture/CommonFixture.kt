package app.kingmojang.fixture

import app.kingmojang.global.common.request.CommonPageRequest

const val SIZE = 10L
const val PAGE = 0L

fun createCommonPageRequest(
    size: Long = SIZE,
    page: Long = PAGE
): CommonPageRequest {
    return CommonPageRequest(size, page)
}