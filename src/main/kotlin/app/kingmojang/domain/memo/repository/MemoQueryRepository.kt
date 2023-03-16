package app.kingmojang.domain.memo.repository

import app.kingmojang.domain.like.domain.QMemoLike.*
import app.kingmojang.domain.member.domain.QMember.member
import app.kingmojang.domain.memo.domain.QMemo.*
import app.kingmojang.domain.memo.dto.response.MemoDto
import app.kingmojang.domain.memo.dto.response.QMemoDto
import app.kingmojang.global.common.request.NoOffsetPageRequest
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class MemoQueryRepository(
    private val queryFactory: JPAQueryFactory,
) {
    fun findMemosWrittenByMember(request: NoOffsetPageRequest, username: String): List<MemoDto> {
        val size = request.size.toLong()
        val builder = BooleanBuilder()
        builder.and(memo.updatedAt.lt(request.updateAt))

        return queryFactory
            .select(createQMemoDto(Expressions.FALSE))
            .from(memo)
            .join(memo.writer, member)
            .where(
                builder.and(memo.writer.username.eq(username))
            )
            .orderBy(memo.updatedAt.desc())
            .limit(size)
            .fetch()
    }

    fun findMemosInUpdatedOrder(size: Int): List<MemoDto> {
        return queryFactory
            .select(createQMemoDto(Expressions.FALSE))
            .from(memo)
            .join(memo.writer, member)
            .orderBy(memo.updatedAt.desc())
            .limit(size.toLong())
            .fetch()
    }

    fun findByIdOrNull(id: Long): MemoDto? {
        return queryFactory
            .select(createQMemoDto(Expressions.FALSE))
            .from(memo)
            .join(memo.writer, member)
            .where(memo.id.eq(id))
            .fetchOne()
    }

    fun findByIdOrNullWithUsername(id: Long, username: String): MemoDto? {
        return queryFactory
            .select(createQMemoDto(existsMemoLike(id, username)))
            .from(memo)
            .join(memo.writer, member)
            .where(memo.id.eq(id))
            .fetchOne()
    }

    private fun existsMemoLike(memoId: Long, username: String): BooleanExpression =
        if (JPAExpressions
                .selectFrom(memoLike)
                .where(memoLike.memo.id.eq(memoId).and(memoLike.member.username.eq(username)))
                .fetchOne() == null
        ) Expressions.FALSE
        else Expressions.TRUE


    private fun createQMemoDto(isLike:BooleanExpression) = QMemoDto(
        memo.id,
        memo.title,
        memo.writer.nickname,
        memo.content,
        memo.likeCount,
        memo.commentCount,
        isLike,
        memo.createdAt,
        memo.updatedAt,
        memo.font.name,
        memo.font.style,
        memo.font.size
    )
}