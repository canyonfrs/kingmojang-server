package app.kingmojang.domain.comment.dto.response

import app.kingmojang.domain.comment.domain.Comment
import app.kingmojang.domain.like.domain.CommentLike
import org.springframework.data.domain.Page

data class CommentsResponse(
    val comments: List<CommentResponse>,
    val totalCount: Long,
    val hasNext: Boolean,
    val nextPage: Int,
) {
    companion object {
        fun of(comments: Page<Comment>, commentLikes: Map<Long, CommentLike>): CommentsResponse {
            return CommentsResponse(
                comments.content.map { c -> CommentResponse.of(c, commentLikes.containsKey(c.id!!)) },
                comments.totalElements,
                comments.hasNext(),
                if (comments.hasNext()) comments.nextPageable().pageNumber else -1
            )
        }
    }
}