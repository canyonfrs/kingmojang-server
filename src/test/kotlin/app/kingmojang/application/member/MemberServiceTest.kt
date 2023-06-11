package app.kingmojang.application.member

import app.kingmojang.domain.member.application.MemberService
import app.kingmojang.domain.member.domain.MemberType
import app.kingmojang.domain.member.repository.MemberRepository
import app.kingmojang.fixture.*
import app.kingmojang.global.util.S3Utils
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class MemberServiceTest : BehaviorSpec({
    val memberRepository = mockk<MemberRepository>()
    val s3Utils = mockk<S3Utils>()
    val memberService = MemberService(memberRepository, BCryptPasswordEncoder(), s3Utils)

    Given("중복되지 않은 닉네임이 주어진 경우") {
        every { memberRepository.existsByNicknameAndType(NICKNAME, MemberType.USER) } returns false
        When("해당 닉네임이 존재하는지 확인하면") {

            Then("false가 반환된다") {
                memberService.existsNickname(NICKNAME, "USER") shouldBe false
            }
        }
    }

    Given("중복된 닉네임이 주어진 경우") {
        every { memberRepository.existsByNicknameAndType(NICKNAME, MemberType.USER) } returns true
        When("해당 닉네임이 존재하는지 확인하면") {

            Then("true가 반환된다") {
                memberService.existsNickname(NICKNAME, "USER") shouldBe true
            }
        }
    }

    Given("중복되지 않은 이메일이 주어진 경우") {
        every { memberRepository.existsByEmail(EMAIL) } returns false
        When("해당 이메일이 존재하는지 확인하면") {

            Then("false가 반환된다") {
                memberService.existsEmail(EMAIL) shouldBe false
            }
        }
    }

    Given("중복된 이메일이 주어진 경우") {
        every { memberRepository.existsByEmail(EMAIL) } returns true
        When("해당 이메일이 존재하는지 확인하면") {

            Then("true가 반환된다") {
                memberService.existsEmail(EMAIL) shouldBe true
            }
        }
    }

})
