package app.kingmojang.fixture

import app.kingmojang.domain.member.dto.request.CreatorInformationRequest
import app.kingmojang.domain.member.dto.response.CreatorInfoResponse

const val CREATOR_INFO_ID = 1L
const val INTRODUCE = "안녕하세요. 오늘은 뭐할까요? 반갑습니다."
const val BROADCAST_LINK = "www.broadcast.link"
const val YOUTUBE = "www.youtube.com"
const val DONATION_LINK = "www.donation.com"
const val BANNER_IMAGE = "www.banner-image.link"
const val IS_FOLLOW = false

fun createCreatorInfoRequest(
    introduce: String = INTRODUCE,
    broadcastLink: String = BROADCAST_LINK,
    youtube: String = YOUTUBE,
    donationLink: String = DONATION_LINK
): CreatorInformationRequest {
    return CreatorInformationRequest(
        introduce,
        broadcastLink,
        youtube,
        donationLink
    )
}

fun createCreatorInfoResponse(
    introduce: String = INTRODUCE,
    bannerImage: String = BANNER_IMAGE,
    broadcastLink: String = BROADCAST_LINK,
    youtube: String = YOUTUBE,
    donationLink: String = DONATION_LINK,
    isFollow: Boolean = IS_FOLLOW
): CreatorInfoResponse {
    return CreatorInfoResponse(
        introduce,
        bannerImage,
        youtube,
        broadcastLink,
        donationLink,
        isFollow
    )
}