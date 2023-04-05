package app.kingmojang.domain.member.repository

import app.kingmojang.domain.member.domain.CreatorInformation
import org.springframework.data.jpa.repository.JpaRepository

interface CreatorInfoRepository : JpaRepository<CreatorInformation, Long> {
}