package app.kingmojang.domain.memo.dto.response

import app.kingmojang.domain.like.domain.MemoLike
import app.kingmojang.domain.memo.domain.Memo
import org.springframework.data.domain.Slice

data class MemosResponse(
    val memos: List<MemoSimpleResponse>,
    val hasNext: Boolean,
    val nextPage: Int
) {
    companion object {
        fun of(memos: Slice<Memo>, memoLikes: Map<Long, MemoLike>): MemosResponse {
            return MemosResponse(
                memos.content.map { m -> MemoSimpleResponse.of(m, memoLikes.containsKey(m.id!!)) }.toList(),
                memos.hasNext(),
                if (memos.hasNext()) memos.nextPageable().pageNumber else -1
            )
        }
    }
}