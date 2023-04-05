package app.kingmojang.domain.member.dto.request

data class CreatorInformationRequest(
    val introduce: String?,
    val broadcastLink: String?,
    val youtube: String?,
    val donationLink: String?,
) {
}