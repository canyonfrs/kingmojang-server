package app.kingmojang.global.security.oauth2

import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpHeaders
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.reactive.function.client.WebClient

class TwitchUserInfoFinder {
    companion object {
        private const val TWITCH_USER_INFO_URL = "https://api.twitch.tv/helix/users"
        private const val TOKEN_TYPE = "Bearer"
        private const val CLIENT_ID_HEADER = "Client-id"

        private val webClient: WebClient = WebClient.builder()
            .baseUrl(TWITCH_USER_INFO_URL)
            .build()

        fun getUserInformation(oAuth2User: OAuth2User, request: OAuth2UserRequest): MutableMap<String, Any> {
            val id = oAuth2User.attributes["sub"]
            val token = request.accessToken.tokenValue
            val clientId = request.clientRegistration.clientId

            return webClient.get().uri { uriBuilder ->
                uriBuilder.queryParam("id", id).build()
            }.headers { header ->
                header.set(HttpHeaders.AUTHORIZATION, "$TOKEN_TYPE $token")
                header.set(CLIENT_ID_HEADER, clientId)
            }
                .retrieve()
                .bodyToMono(object : ParameterizedTypeReference<MutableMap<String, Any>>() {})
                .block() ?: mutableMapOf()
        }
    }
}