package com.memeapp.memeflex.data.response

data class AuthResponse(
    val token: String,
    val user: UserResponse
)