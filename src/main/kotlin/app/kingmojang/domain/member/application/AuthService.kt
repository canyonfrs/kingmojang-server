package app.kingmojang.domain.member.application

import app.kingmojang.domain.member.domain.Member
import app.kingmojang.domain.member.domain.MemberType
import app.kingmojang.domain.member.domain.UserDetailsImpl
import app.kingmojang.domain.member.dto.request.LoginRequest
import app.kingmojang.domain.member.dto.request.RefreshRequest
import app.kingmojang.domain.member.dto.request.SignupRequest
import app.kingmojang.domain.member.dto.response.TokenResponse
import app.kingmojang.domain.member.exception.SameEmailAlreadyExistException
import app.kingmojang.domain.member.exception.SameNicknameAlreadyExistException
import app.kingmojang.domain.member.exception.SameUsernameAlreadyExistException
import app.kingmojang.domain.member.repository.MemberRepository
import app.kingmojang.global.exception.CommonException
import app.kingmojang.global.exception.ErrorCodes.*
import app.kingmojang.global.exception.common.InvalidInputException
import app.kingmojang.global.security.JwtUtils
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val jwtUtils: JwtUtils,
) {

    @Transactional
    fun signup(request: SignupRequest): Long {
        validate(request)
        val member = Member.create(
            request.copy(password = passwordEncoder.encode(request.password))
        )
        return memberRepository.save(member).id!!
    }

    @Transactional
    fun login(request: LoginRequest): TokenResponse {
        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(request.username, request.password))

        val member = memberRepository.findByUsername(request.username) ?: throw CommonException(NOT_FOUND_USERNAME)

        val accessToken = jwtUtils.generateToken(UserDetailsImpl(member))
        val refreshToken = member.generateToken()

        return TokenResponse(accessToken, refreshToken)
    }

    @Transactional
    fun refresh(request: RefreshRequest): TokenResponse {
        val member = memberRepository.findByUsername(request.username) ?: throw CommonException(NOT_FOUND_USERNAME)

        if (member.refreshToken.isValid(request.refreshToken)) {
            val accessToken = jwtUtils.generateToken(UserDetailsImpl(member))
            val refreshToken = member.generateToken()

            return TokenResponse(accessToken, refreshToken)
        }
        throw CommonException(INVALID_REFRESH_TOKEN)
    }

    private fun validate(request: SignupRequest) {
        if (!MemberType.isValidate(request.memberType)) {
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
}