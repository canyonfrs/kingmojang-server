package app.kingmojang.application.member

import app.kingmojang.domain.authenticationcode.repository.AuthCodeRepository
import app.kingmojang.domain.member.application.AuthService
import app.kingmojang.domain.member.domain.MemberType
import app.kingmojang.domain.member.exception.InvalidPasswordException
import app.kingmojang.domain.member.exception.SameEmailAlreadyExistException
import app.kingmojang.domain.member.exception.SameNicknameAlreadyExistException
import app.kingmojang.domain.member.exception.SameUsernameAlreadyExistException
import app.kingmojang.domain.member.repository.MemberRepository
import app.kingmojang.fixture.*
import app.kingmojang.global.exception.CommonException
import app.kingmojang.global.security.JwtUtils
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

class AuthServiceTest : BehaviorSpec({
    val memberRepository = mockk<MemberRepository>()
    val authCodeRepository = mockk<AuthCodeRepository>()
    val passwordEncoder = BCryptPasswordEncoder()
    val jwtUtils = mockk<JwtUtils>()

    val authService =
        AuthService(memberRepository, authCodeRepository, passwordEncoder, jwtUtils)

    Given("정상적인 일반 유저 회원가입 요청이 들어오는 경우") {
        every { memberRepository.existsByUsername(any()) } returns false
        every { memberRepository.existsByEmail(any()) } returns false
        every { memberRepository.existsByNicknameAndType(any(), MemberType.USER) } returns false
        every { memberRepository.save(any()) } returns createMember()

        When("해당 요청으로 회원가입을 하면") {
            val actual = authService.signup(createSignupRequest(memberType = MemberType.USER))

            Then("회원가입이 완료된다") {
                actual shouldBe MEMBER_ID
            }
        }
    }

    Given("중복된 아이디 정보로 회원가입 요청이 들어오는 경우") {
        every { memberRepository.existsByUsername(any()) } returns true
        every { memberRepository.existsByEmail(any()) } returns false
        every { memberRepository.existsByNicknameAndType(any(), MemberType.USER) } returns false

        When("해당 요청으로 회원가입을 하면") {
            Then("예외가 발생한다") {
                shouldThrow<SameUsernameAlreadyExistException> {
                    authService.signup(createSignupRequest(memberType = MemberType.USER))
                }
            }
        }
    }

    Given("중복된 이메일 정보로 회원가입 요청이 들어오는 경우") {
        every { memberRepository.existsByUsername(any()) } returns false
        every { memberRepository.existsByEmail(any()) } returns true
        every { memberRepository.existsByNicknameAndType(any(), MemberType.USER) } returns false

        When("해당 요청으로 회원가입을 하면") {
            Then("예외가 발생한다") {
                shouldThrow<SameEmailAlreadyExistException> {
                    authService.signup(createSignupRequest(memberType = MemberType.USER))
                }
            }
        }
    }

    Given("중복된 닉네임 정보로 회원가입 요청이 들어오는 경우") {
        every { memberRepository.existsByUsername(any()) } returns false
        every { memberRepository.existsByEmail(any()) } returns false
        every { memberRepository.existsByNicknameAndType(any(), MemberType.USER) } returns true

        When("해당 요청으로 회원가입을 하면") {
            Then("예외가 발생한다") {
                shouldThrow<SameNicknameAlreadyExistException> {
                    authService.signup(createSignupRequest(memberType = MemberType.USER))
                }
            }
        }
    }

    Given("정상적인 일반 유저 로그인 요청이 들어오는 경우") {
        every { memberRepository.findByUsername(any()) } returns createMember()
        every { jwtUtils.generateToken(any()) } returns ACCESS_TOKEN

        When("해당 요청으로 로그인을 하면") {
            val actual = authService.login(createLoginRequest())

            Then("로그인이 완료된다") {
                actual.accessToken shouldBe createTokenResponse().accessToken
            }
        }
    }

    Given("잘못된 비밀번호로 로그인 요청이 들어오는 경우") {
        every { memberRepository.findByUsername(any()) } returns createMember()

        When("해당 요청으로 로그인을 하면") {
            Then("예외가 발생한다") {
                shouldThrow<InvalidPasswordException> {
                    authService.login(createLoginRequest(password = "INVALID_PASSWORD"))
                }
            }
        }
    }

    Given("정상적으로 액세스토큰을 갱신하는 요청이 들어오는 경우") {
        val member = createMember()
        every { memberRepository.findByIdOrNull(any()) } returns member
        every { jwtUtils.generateToken(any()) } returns ACCESS_TOKEN

        When("해당 요청으로 액세스토큰을 갱신하면") {
            val actual = authService.refresh(createRefreshRequest())

            Then("액세스토큰이 갱신된다") {
                actual.accessToken shouldBe createTokenResponse().accessToken
            }
        }
    }

    Given("잘못된 리프레시 토큰으로 액세스토큰을 갱신하는 요청이 들어오는 경우") {
        val member = createMember()
        val invalidRefreshToken = UUID.randomUUID()
        every { memberRepository.findByIdOrNull(any()) } returns member

        When("해당 요청으로 액세스토큰을 갱신하면") {
            Then("예외가 발생한다") {
                shouldThrow<CommonException> {
                    authService.refresh(createRefreshRequest(refreshToken = invalidRefreshToken))
                }
            }
        }
    }

    afterTest {
        clearAllMocks(answers = false)
    }
})