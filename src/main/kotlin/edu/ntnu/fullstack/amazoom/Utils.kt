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

    fun addToastToResponse(
        response: HttpServletResponse,
        type: ToastType,
        message: String,
        description: String? = null,
        duration: Int? = null
    ) {

        val options = mutableMapOf(
            "type" to type.toString(),
            "message" to message
        )
        description?.let { options["description"] = it }
        duration?.let { options["duration"] = it.toString() }

        val toastParams = SearchParams(options)
        val toastCookie = Cookie("toast", toastParams)
        toastCookie.apply {
            maxAge = 100
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