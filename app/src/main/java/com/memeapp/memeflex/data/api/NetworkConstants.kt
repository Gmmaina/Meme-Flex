package com.memeapp.memeflex.api

object NetworkConstants {
    const val BASE_URL = "http://10.0.2.2:8080/" // For Android emulator
    // const val BASE_URL = "http://localhost:8080/" // For device/real IP
    // const val BASE_URL = "https://your-api-domain.com/" // For production

    const val CONNECT_TIMEOUT = 30L
    const val READ_TIMEOUT = 30L
    const val WRITE_TIMEOUT = 30L

    // HTTP Status codes
    const val HTTP_UNAUTHORIZED = 401
    const val HTTP_FORBIDDEN = 403
    const val HTTP_NOT_FOUND = 404
    const val HTTP_CONFLICT = 409
    const val HTTP_INTERNAL_SERVER_ERROR = 500
}