package app.kingmojang.domain.memo.repository

import app.kingmojang.domain.memo.domain.Memo
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface MemoRepository : JpaRepository<Memo, Long> {

    @EntityGraph(attributePaths = ["writer"])
    fun findMemoWithWriterByIdAndDeletedFalse(id: Long): Memo?

    @EntityGraph(attributePaths = ["writer"])
    fun findAllWithWriterByWriterIdAndDeletedFalse(writerId: Long, pageable: Pageable): Slice<Memo>

    @EntityGraph(attributePaths = ["writer"])
    fun findAllWithWriterByWriterIdInAndDeletedFalse(writerIds: List<Long>, pageable: Pageable): Slice<Memo>

    @EntityGraph(attributePaths = ["writer"])
    fun findAllWithWriterByDeletedFalse(pageable: Pageable): Slice<Memo>
}
