package app.kingmojang.domain.member.dto.response

import app.kingmojang.domain.member.domain.CreatorInformation

data class CreatorInfoResponse(
    val introduce: String,
    val bannerImage: String,
    val youtube: String,
    val broadcastLink: String,
    val donationLink: String,
    val isFollow: Boolean,
) {
    companion object {
        fun of(creatorInformation: CreatorInformation, isFollow: Boolean): CreatorInfoResponse {
            return CreatorInfoResponse(
                introduce = creatorInformation.introduce ?: "",
                bannerImage = creatorInformation.bannerImage ?: "",
                youtube = creatorInformation.youtube ?: "",
                broadcastLink = creatorInformation.broadcastLink ?: "",
                donationLink = creatorInformation.donationLink ?: "",
                isFollow = isFollow
            )
        }
    }
}