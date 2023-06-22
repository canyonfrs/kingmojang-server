package app.kingmojang.annotation

import app.kingmojang.domain.member.domain.UserPrincipal
import app.kingmojang.fixture.createMember
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithSecurityContextFactory


class WithMockCustomUserSecurityContextFactory : WithSecurityContextFactory<WithMockCustomUser> {
    override fun createSecurityContext(annotation: WithMockCustomUser): SecurityContext {
        val context = SecurityContextHolder.createEmptyContext()
        val principal = UserPrincipal.create(createMember(memberId = annotation.memberId, memberType = annotation.type))
        context.authentication = UsernamePasswordAuthenticationToken(principal, null, principal.authorities)
        return context
    }
}