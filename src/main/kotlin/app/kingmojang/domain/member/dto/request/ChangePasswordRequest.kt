package app.kingmojang.domain.member.dto.request

data class ChangePasswordRequest(
    val oldPassword: String,
    val newPassword: String,
)
