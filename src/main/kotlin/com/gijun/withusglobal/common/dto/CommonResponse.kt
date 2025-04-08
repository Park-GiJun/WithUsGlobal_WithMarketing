package com.gijun.withusglobal.common.dto

data class CommonResponse<T>(
    val success: Boolean,
    val message: String? = null,
    val data: T? = null
) {
    companion object {
        fun <T> success(data: T, message: String? = null): CommonResponse<T> {
            return CommonResponse(true, message, data)
        }
        
        fun <T> error(message: String): CommonResponse<T> {
            return CommonResponse(false, message, null)
        }
    }
}
