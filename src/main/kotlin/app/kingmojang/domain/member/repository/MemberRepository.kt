package app.kingmojang.domain.member.repository

import app.kingmojang.domain.member.domain.Member
import app.kingmojang.domain.member.domain.MemberType
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {
    fun findByEmail(email: String): Member?
    fun existsByNicknameAndType(nickname: String, type: MemberType): Boolean
    fun existsByEmail(email: String): Boolean
}