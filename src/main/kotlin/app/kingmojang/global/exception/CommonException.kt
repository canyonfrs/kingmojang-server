package app.kingmojang.global.exception

open class CommonException(
    open val codeBook: ErrorCodes,
    override val message: String = "",
    override val cause: Throwable? = null,
) : RuntimeException(message, cause) {
    open fun messageArguments(): Collection<String>? = null

    open fun details(): Any? = null
}