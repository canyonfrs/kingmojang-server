package app.kingmojang.domain.comment.dto.response

import app.kingmojang.domain.comment.domain.Reply
import app.kingmojang.domain.like.domain.ReplyLike
import org.springframework.data.domain.Page

data class RepliesResponse(
    val replies: List<ReplyResponse>,
    val totalCount: Long,
    val hasNext: Boolean,
    val nextPage: Int,
) {
    companion object {
        fun of(replies: Page<Reply>, replyLikes: Map<Long, ReplyLike>): RepliesResponse {
            return RepliesResponse(
                replies.content.map { r -> ReplyResponse.of(r, replyLikes.containsKey(r.id!!)) },
                replies.totalElements,
                replies.hasNext(),
                if (replies.hasNext()) replies.nextPageable().pageNumber else -1
            )
        }
    }
}
