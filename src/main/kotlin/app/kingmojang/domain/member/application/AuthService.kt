package app.kingmojang.domain.member.application

import app.kingmojang.domain.authenticationcode.exception.ExpiredAuthCodeException
import app.kingmojang.domain.authenticationcode.exception.NotFoundAuthCodeException
import app.kingmojang.domain.authenticationcode.repository.AuthCodeRepository
import app.kingmojang.domain.member.domain.Member
import app.kingmojang.domain.member.domain.MemberType
import app.kingmojang.domain.member.domain.UserPrincipal
import app.kingmojang.domain.member.dto.request.LoginRequest
import app.kingmojang.domain.member.dto.request.RefreshRequest
import app.kingmojang.domain.member.dto.request.SignupRequest
import app.kingmojang.domain.member.dto.response.TokenResponse
import app.kingmojang.domain.member.exception.*
import app.kingmojang.domain.member.repository.MemberRepository
import app.kingmojang.global.exception.CommonException
import app.kingmojang.global.exception.ErrorCodes.*
import app.kingmojang.global.exception.common.InvalidInputException
import app.kingmojang.global.security.JwtUtils
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val memberRepository: MemberRepository,
    private val authCodeRepository: AuthCodeRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtils: JwtUtils,
) {

    @Transactional
    fun signup(request: SignupRequest): Long {
        validate(request)
        if (isCreatorSignup(request.memberType)) {
            verifyAuthCode(request)
        }
        val member = Member.create(
            request.copy(password = passwordEncoder.encode(request.password))
        )
        return memberRepository.save(member).id!!
    }

    private fun isCreatorSignup(memberType: String) =
        MemberType.CREATOR == MemberType.valueOf(memberType.uppercase())

    @Transactional
    fun login(request: LoginRequest): TokenResponse {
        val username = request.username
        val member = memberRepository.findByUsername(username) ?: throw NotFoundUsernameException(username)
        if (!passwordEncoder.matches(request.password, member.password)) {
            throw InvalidPasswordException()
        }
        return createTokenResponse(member)
    }

    @Transactional
    fun refresh(request: RefreshRequest): TokenResponse {
        val memberId = request.memberId
        val member = memberRepository.findByIdOrNull(memberId) ?: throw NotFoundMemberException(memberId)

        if (member.refreshToken.isValid(request.refreshToken)) {
            return createTokenResponse(member)
        }
        throw CommonException(INVALID_REFRESH_TOKEN)
    }

    private fun createTokenResponse(member: Member): TokenResponse {
        val accessToken = jwtUtils.generateToken(UserPrincipal.create(member))
        val refreshToken = member.generateToken()

        return TokenResponse(accessToken, refreshToken)
    }

    private fun validate(request: SignupRequest) {
        if (!MemberType.isValidate(request.memberType.uppercase())) {
            throw InvalidInputException(request.memberType)
        }
        if (memberRepository.existsByUsername(request.username)) {
            throw SameUsernameAlreadyExistException(request.username)
        }
        if (memberRepository.existsByEmail(request.email)) {
            throw SameEmailAlreadyExistException(request.email)
        }
        if (memberRepository.existsByNicknameAndType(request.nickname, MemberType.valueOf(request.memberType))) {
            throw SameNicknameAlreadyExistException(request.nickname)
        }
    }

    private fun verifyAuthCode(request: SignupRequest) {
        val authCode = authCodeRepository.findByCode(request.authCode)
            ?: throw NotFoundAuthCodeException(request.authCode)
        if (authCode.isNotExpired()) {
            authCode.updateToExpire()
        } else {
            throw ExpiredAuthCodeException(request.authCode)
        }
    }
}