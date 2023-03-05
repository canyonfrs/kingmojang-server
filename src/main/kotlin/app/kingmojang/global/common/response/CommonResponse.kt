package app.kingmojang.global.common.response

import com.fasterxml.jackson.annotation.JsonInclude

data class CommonResponse<T>(
    val status: ResponseStatus,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val message: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val data: T?
){
    companion object{
        fun of(status: ResponseStatus): CommonResponse<Void> {
            return CommonResponse(status, null, null)
        }

        fun of(status: ResponseStatus, message: String): CommonResponse<Void> {
            return CommonResponse(status, message, null)
        }

        fun <T> of(status: ResponseStatus, data: T): CommonResponse<T> {
            return CommonResponse(status, null, data)
        }
    }
}