package app.kingmojang.domain.member.domain

import app.kingmojang.domain.member.dto.request.CreatorInformationRequest
import jakarta.persistence.*

@Entity
class CreatorInformation(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "creator_information_id")
    val id: Long? = null,

    @Lob
    @Column
    var introduce: String?,

    @Column(name = "banner_image")
    var bannerImage: String? = null,

    var youtube: String?,

    @Column(name = "broadcast_link")
    var broadcastLink: String?,

    @Column(name = "donation_link")
    var donationLink: String?,
) {
    companion object {
        fun create(request: CreatorInformationRequest): CreatorInformation {
            return CreatorInformation(
                introduce = request.introduce,
                youtube = request.youtube,
                broadcastLink = request.broadcastLink,
                donationLink = request.donationLink
            )
        }
    }

    fun update(request: CreatorInformationRequest): CreatorInformation {
        this.introduce = request.introduce
        this.youtube = request.youtube
        this.broadcastLink = request.broadcastLink
        this.donationLink = request.donationLink
        return this
    }
}