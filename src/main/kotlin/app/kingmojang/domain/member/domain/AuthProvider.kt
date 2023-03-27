package app.kingmojang.domain.member.domain

enum class AuthProvider(val value: String) {
    LOCAL("local"), GOOGLE("google"), NAVER("naver"), KAKAO("kakao"), TWITCH("twitch");
}