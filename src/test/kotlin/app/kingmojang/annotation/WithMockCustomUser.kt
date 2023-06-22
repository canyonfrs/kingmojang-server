package app.kingmojang.annotation

import app.kingmojang.domain.member.domain.MemberType
import app.kingmojang.fixture.MEMBER_ID
import org.springframework.security.test.context.support.WithSecurityContext

@Retention(AnnotationRetention.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory::class)
annotation class WithMockCustomUser(
    val memberId: Long = MEMBER_ID,
    val type: MemberType = MemberType.CREATOR
)
