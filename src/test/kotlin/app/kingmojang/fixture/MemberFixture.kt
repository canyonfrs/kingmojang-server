package app.kingmojang.fixture

import app.kingmojang.domain.member.domain.AuthProvider
import app.kingmojang.domain.member.domain.Member
import app.kingmojang.domain.member.domain.MemberType
import app.kingmojang.domain.member.domain.RefreshToken
import app.kingmojang.domain.member.dto.request.LoginRequest
import app.kingmojang.domain.member.dto.request.RefreshRequest
import app.kingmojang.domain.member.dto.request.SignupRequest
import app.kingmojang.domain.member.dto.response.TokenResponse
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.time.LocalDateTime
import java.util.*

const val MEMBER_ID = 1L
const val EMAIL = "proto_seo@naver.com"
const val NICKNAME = "캬옹"
const val PASSWORD = "password"
const val AUTH_CODE = 12345678
const val ACCESS_TOKEN = "valid-token"
const val INVALID_ACCESS_TOKEN = "invalid-token"
const val PROFILE_IMAGE = "https://profile-image.com"
val REFRESH_TOKEN: UUID = UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
val MEMBER_CREATED_AT: LocalDateTime = LocalDateTime.now()

fun createSignupRequest(
    password: String = PASSWORD,
    nickname: String = NICKNAME,
    email: String = EMAIL,
    provider: AuthProvider = AuthProvider.LOCAL,
    memberType: MemberType = MemberType.CREATOR,
    authCode: Int = AUTH_CODE
): SignupRequest {
    return SignupRequest(
        password,
        nickname,
        email,
        provider.toString(),
        memberType.toString(),
        authCode.toString()
    )
}

fun createLoginRequest(
    email: String = EMAIL,
    password: String = PASSWORD,
): LoginRequest {
    return LoginRequest(email, password)
}

fun createRefreshRequest(
    refreshToken: UUID = REFRESH_TOKEN,
    memberId: Long = MEMBER_ID
): RefreshRequest {
    return RefreshRequest(refreshToken.toString(), memberId)
}

fun createTokenResponse(
    accessToken: String = ACCESS_TOKEN,
    refreshToken: UUID = REFRESH_TOKEN
): TokenResponse {
    return TokenResponse(accessToken, refreshToken.toString())
}

fun createMember(
    memberId: Long = MEMBER_ID,
    password: String = PASSWORD,
    nickname: String = NICKNAME,
    email: String = EMAIL,
    profileImage: String = PROFILE_IMAGE,
    provider: AuthProvider = AuthProvider.LOCAL,
    memberType: MemberType = MemberType.CREATOR,
    createdAt: LocalDateTime = MEMBER_CREATED_AT
): Member {
    val refreshToken = RefreshToken(REFRESH_TOKEN.toString(), LocalDateTime.now().plusYears(1))
    return Member(
        id = memberId,
        password = BCryptPasswordEncoder().encode(password),
        nickname = nickname,
        email = email,
        profileImage = profileImage,
        refreshToken = refreshToken,
        provider = provider,
        type = memberType,
        createdAt = createdAt
    )
}