package app.kingmojang.annotation

import app.kingmojang.domain.member.domain.MemberType
import org.springframework.security.test.context.support.WithSecurityContext

@Retention(AnnotationRetention.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory::class)
annotation class WithMockCustomUser(
    val type: MemberType = MemberType.CREATOR
)
