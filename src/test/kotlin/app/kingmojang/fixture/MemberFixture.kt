package app.kingmojang.fixture

import app.kingmojang.domain.member.domain.AuthProvider
import app.kingmojang.domain.member.domain.MemberType
import app.kingmojang.domain.member.dto.request.LoginRequest
import app.kingmojang.domain.member.dto.request.RefreshRequest
import app.kingmojang.domain.member.dto.request.SignupRequest
import app.kingmojang.domain.member.dto.response.TokenResponse
import java.util.*

const val MEMBER_ID = 1L
const val USERNAME = "proto_seo"
const val EMAIL = "proto_seo@naver.com"
const val NICKNAME = "캬옹"
const val PASSWORD = "password"
const val AUTH_CODE = 12345678
const val ACCESS_TOKEN = "valid-token"
val REFRESH_TOKEN: UUID = UUID.randomUUID()

fun createSignupRequest(
    username: String = USERNAME,
    password: String = PASSWORD,
    nickname: String = NICKNAME,
    email: String = EMAIL,
    provider: AuthProvider = AuthProvider.LOCAL,
    memberType: MemberType = MemberType.CREATOR,
    authCode: Int = AUTH_CODE
): SignupRequest {
    return SignupRequest(
        username,
        password,
        nickname,
        email,
        provider.toString(),
        memberType.toString(),
        authCode
    )
}

fun createLoginRequest(
    username: String = USERNAME,
    password: String = PASSWORD,
): LoginRequest {
    return LoginRequest(username, password)
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