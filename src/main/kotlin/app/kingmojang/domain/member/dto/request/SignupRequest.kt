package app.kingmojang.domain.member.dto.request

import app.kingmojang.domain.member.domain.MemberType

data class SignupRequest(
    val username: String,
    val password: String,
    val nickname: String,
    val email: String,
    val isAuthorizedAccount: Boolean,
    val introduce: String?,
    val youtube: String?,
    val broadcastLink: String?,
    val donationLink: String?,
    val memberType: MemberType,
) {
}