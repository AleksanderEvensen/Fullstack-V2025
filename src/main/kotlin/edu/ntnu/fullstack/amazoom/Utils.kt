package edu.ntnu.fullstack.amazoom

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse


object Utils {
    fun SearchParams(map: Map<String, String>): String {
        val searchParams = map.map { (key, value) -> "$key=${urlEncode(value)}" }
        return searchParams.joinToString("&")
    }

    fun urlEncode(value: String): String {
        return java.net.URLEncoder.encode(value, "UTF-8");
    }

    fun addToastToResponse(response: HttpServletResponse, type: ToastType, message: String) {
        val toastParams = SearchParams(mapOf(
            "type" to type.toString(),
            "message" to message
        ))
        val toastCookie = Cookie("toast", toastParams)
        toastCookie.apply {
            maxAge = 1
            path = "/"
            setAttribute("SameSite", "Lax")
        }
        response.addCookie(toastCookie)
    }
}

enum class ToastType {
    Success,
    Error,
    Info,

    ;
    override fun toString(): String {
        return name.lowercase()
    }
}