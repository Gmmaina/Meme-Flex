package com.memeapp.memeflex.api

import com.memeapp.memeflex.data.requests.*
import com.memeapp.memeflex.data.responses.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Health check
    @GET("/")
    suspend fun getApiStatus(): Response<Map<String, String>>

    @GET("health")
    suspend fun getHealthCheck(): Response<Map<String, Any>>

    // Authentication
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    // User endpoints
    @GET("users/me")
    suspend fun getCurrentUser(): Response<UserResponse>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") userId: String): Response<UserResponse>

    @GET("users/{id}/memes")
    suspend fun getUserMemes(
        @Path("id") userId: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Response<FeedResponse>

    // Meme endpoints
    @POST("memes")
    suspend fun createMeme(@Body request: CreateMemeRequest): Response<MemeResponse>

    @GET("memes/{id}")
    suspend fun getMemeById(@Path("id") memeId: String): Response<MemeResponse>

    @PUT("memes/{id}")
    suspend fun updateMeme(
        @Path("id") memeId: String,
        @Body request: UpdateMemeRequest
    ): Response<SuccessResponse>

    @DELETE("memes/{id}")
    suspend fun deleteMeme(@Path("id") memeId: String): Response<SuccessResponse>

    @POST("memes/{id}/like")
    suspend fun likeMeme(@Path("id") memeId: String): Response<SuccessResponse>

    @DELETE("memes/{id}/like")
    suspend fun unlikeMeme(@Path("id") memeId: String): Response<SuccessResponse>

    @POST("memes/{id}/download")
    suspend fun trackMemeDownload(@Path("id") memeId: String): Response<SuccessResponse>

    // Feed endpoints (public)
    @GET("feed")
    suspend fun getFeed(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
        @Query("sort") sort: String = "recent" // recent, popular
    ): Response<FeedResponse>

    @GET("feed/search")
    suspend fun searchMemes(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Response<FeedResponse>

    @GET("feed/tags/{tag}")
    suspend fun getMemesByTag(
        @Path("tag") tag: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Response<FeedResponse>

    // Update
    @PUT("users/me/password")
    suspend fun updatePassword(@Body request: UpdatePasswordRequest): Response<SuccessResponse>

    @PUT("users/me/profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<UserResponse>
}