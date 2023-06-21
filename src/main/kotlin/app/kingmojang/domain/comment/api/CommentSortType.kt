package app.kingmojang.domain.comment.api

import org.springframework.data.domain.Sort

enum class CommentSortType(
    private val property: String,
    private val direction: Sort.Direction,
) {
    CREATED_AT_DESC("createdAt", Sort.Direction.DESC),
    CREATED_AT_ASC("createdAt", Sort.Direction.ASC),
    LIKE_COUNT_DESC("likeCount", Sort.Direction.DESC);

    fun getSort(): Sort {
        return Sort.by(this.direction, this.property)
    }

    companion object {
        fun of(sort: String): CommentSortType {
            if (CommentSortType.values().map { it.toString() }.contains(sort.uppercase()).not())
                return CREATED_AT_ASC
            return CommentSortType.valueOf(sort.uppercase())
        }
    }
}