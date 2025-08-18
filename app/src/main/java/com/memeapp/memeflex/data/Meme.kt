package com.memeapp.memeflex.data

import androidx.annotation.DrawableRes
import com.memeapp.memeflex.R

data class Meme(
    @DrawableRes val image: Int,
    val caption: String,
    val likes: Int = 0,
    val downloads: Int = 0,
    val profileName: String,
    @DrawableRes val profileImage: Int = R.drawable.avatar,
    val isLiked: Boolean = false
)

val sampleMemes = listOf(
    Meme(
        image = R.drawable.dog1,
        caption = "When you finally fix the bug",
        likes = 42,
        downloads = 15,
        profileName = "@codeMaster"
    ),
    Meme(
        image = R.drawable.img,
        caption = "Monday mood",
        likes = 128,
        downloads = 64,
        profileName = "@weekendLover"
    ),
    Meme(
        image = R.drawable.img_1,
        caption = "Debugging be like",
        likes = 256,
        downloads = 89,
        profileName = "@devGuru",
        isLiked = true
    ),
    Meme(
        image = R.drawable.img_2,
        caption = "My code vs. production",
        likes = 512,
        downloads = 210,
        profileName = "@releaseManager"
    ),
    Meme(
        image = R.drawable.img_3,
        caption = "Reacting to PR comments",
        likes = 1024,
        downloads = 420,
        profileName = "@seniorDev"
    )
)