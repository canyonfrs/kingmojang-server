package app.kingmojang.domain.authenticationcode.repository

import app.kingmojang.domain.authenticationcode.domain.AuthenticationCode
import org.springframework.data.jpa.repository.JpaRepository

interface AuthCodeRepository : JpaRepository<AuthenticationCode, Long> {
    fun existsByCodeAndIsExpired(code: Int, isExpired: Boolean): Boolean
    fun findByCode(code: Int): AuthenticationCode?
}