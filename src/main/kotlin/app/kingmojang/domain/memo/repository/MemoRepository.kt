package app.kingmojang.domain.memo.repository

import app.kingmojang.domain.memo.domain.Memo
import org.springframework.data.jpa.repository.JpaRepository

interface MemoRepository : JpaRepository<Memo, Long> {

}
