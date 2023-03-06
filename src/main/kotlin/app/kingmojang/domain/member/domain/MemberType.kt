package app.kingmojang.domain.member.domain

enum class MemberType(val value: String) {
    USER("ROLE_USER"), CREATOR("ROLE_CREATOR"), ADMIN("ROLE_ADMIN");

    companion object {
        fun isValidate(type: String): Boolean {
            return (MemberType.values().find { it.name == type } != null)
        }
    }
}